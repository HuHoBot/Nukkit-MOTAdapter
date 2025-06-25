package cn.huohuas001.huHoBot;

import cn.huohuas001.config.ServerConfig;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.scheduler.ServerScheduler;
import cn.nukkit.scheduler.TaskHandler;
import com.alibaba.fastjson2.JSONObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.utils.TextFormat;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class WebsocketClientManager {
    private static WsClient client; //Websocket客户端
    private static String websocketUrl = ServerConfig.WS_SERVER_URL;
    private final int RECONNECT_DELAY = 5; // 重连延迟时间，单位为秒
    private final int MAX_RECONNECT_ATTEMPTS = 5; // 最大重连尝试次数
    private final HuHoBot plugin;
    private final PluginLogger logger;
    private final ServerScheduler scheduler;
    private int ReconnectAttempts = 0;
    private boolean shouldReconnect = true; // 控制是否重连的变量
    private TaskHandler currentTask;
    private TaskHandler autoDisConnectTask;

    public WebsocketClientManager() {
        plugin = HuHoBot.getInstance();
        logger = plugin.getLogger();
        scheduler = plugin.getServer().getScheduler();
    }

    /**
     * 设置是否应该重连
     *
     * @param shouldReconnect 是否应该重连
     */
    public void setShouldReconnect(boolean shouldReconnect) {
        this.shouldReconnect = shouldReconnect;
    }

    /**
     * 客户端自动重连循环
     */
    private void autoReconnect() {
        synchronized (this) {
            ReconnectAttempts++;
            if (ReconnectAttempts >= MAX_RECONNECT_ATTEMPTS) {
                logger.warning(" 重连尝试已达到最大次数，将不再尝试重新连接。");
                cancelCurrentTask();
                return;
            }
            if (!shouldReconnect) {
                cancelCurrentTask();
                return;
            }
            logger.info(" 正在尝试重新连接,这是第(" + ReconnectAttempts + "/" + MAX_RECONNECT_ATTEMPTS + ")次连接");
            this.connectServer();
        }
    }

    public void cancelCurrentTask() {
        if (currentTask != null) {
            currentTask.cancel();
            currentTask = null;
            ReconnectAttempts = 0;
        }
    }

    public WsClient getClient() {
        return client;
    }

    public boolean shutdownClient() {
        if (client != null && client.isOpen()) {
            client.close();
            return true;
        }
        return false;
    }

    public void autoDisConnectClient() {
        logger.info("连接超时，已自动重连");
        shutdownClient();
    }

    public void setAutoDisConnectTask() {
        if (autoDisConnectTask == null) {
            autoDisConnectTask = scheduler.scheduleDelayedTask(HuHoBot.getInstance(),this::autoDisConnectClient, 6 * 60 * 60 * 20);
        } else {
            autoDisConnectTask.cancel();
            autoDisConnectTask = null;
            setAutoDisConnectTask();
        }
    }

    /**
     * 连接HuHoBot服务器
     */
    public boolean connectServer() {
        logger.info(" 正在连接服务端...");
        try {
            URI uri = new URI(websocketUrl);
            if (client == null || !client.isOpen()) {
                client = new WsClient(uri, this);
                setShouldReconnect(true); // 设置是否重连
                client.connect();
            }
            return true;
        } catch (URISyntaxException e) {
            logger.error(TextFormat.DARK_RED + e.getStackTrace().toString());
        } catch (Exception e) {
            logger.error("连接HuHoBot失败: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private SSLContext createCloudflareSSLContext() throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new TrustManager[]{
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        // 添加调试信息
                /*logger.info("接受服务器证书: " +
                    (chain != null && chain.length > 0 ? chain[0].getSubjectDN() : "无证书"));*/
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        }, new java.security.SecureRandom());

        // 设置协议版本
        context.getDefaultSSLParameters().setProtocols(new String[]{"TLSv1.2", "TLSv1.3"});
        return context;
    }

    public boolean isOpen() {
        return client.isOpen();
    }

    public void sendHeart() {
        client.sendMessage("heart", new JSONObject());
    }

    public void clientReconnect() {
        if (shouldReconnect && currentTask == null) {
            currentTask = scheduler.scheduleRepeatingTask(HuHoBot.getInstance(),this::autoReconnect, this.RECONNECT_DELAY * 20);
        }
    }
}
