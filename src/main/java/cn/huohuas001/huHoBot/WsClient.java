package cn.huohuas001.huHoBot;


import cn.huohuas001.huHoBot.config.BotConfig;
import cn.huohuas001.huHoBot.tools.PackId;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.Config;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import cn.nukkit.utils.TextFormat;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


public class WsClient extends WebSocketClient {
    private final Map<String, CompletableFuture<JSONObject>> responseFutureList = new HashMap<>();
    private HuHoBot plugin;
    private PluginLogger logger;
    private final WebsocketClientManager clientManager;


    /*public WsClient(URI serverUri, WebsocketClientManager clientManager,
                    Map<String, String> headers, SSLContext sslContext) {
        super(serverUri, new Draft_6455(), headers, 10000); // 增加超时到10秒

        try {
            if (sslContext != null) {
                SSLSocketFactory factory = sslContext.getSocketFactory();
                SSLSocket socket = (SSLSocket) factory.createSocket();

                // 强制启用TLS 1.2/1.3
                socket.setEnabledProtocols(new String[]{"TLSv1.2", "TLSv1.3"});

                // 可选：设置支持的密码套件
                socket.setEnabledCipherSuites(new String[]{
                        "TLS_AES_128_GCM_SHA256",
                        "TLS_AES_256_GCM_SHA384",
                        "TLS_CHACHA20_POLY1305_SHA256",
                        "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
                        "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256"
                });

                this.setSocket(socket);
            }
        } catch (Exception e) {
            throw new RuntimeException("创建SSL socket失败", e);
        }
        this.plugin = HuHoBot.getInstance();
        this.logger = plugin.getLogger();
        this.clientManager = clientManager;
    }*/



    public WsClient(URI serverUri, WebsocketClientManager clientManager) {
        super(serverUri);
        this.plugin = HuHoBot.getInstance();
        this.logger = plugin.getLogger();
        this.clientManager = clientManager;
    }

    @Override
    public void onOpen(ServerHandshake _da) {
        logger.info("服务端连接成功.");
        this.shakeHand();
    }

    @Override
    public void onMessage(String message) {
        //logger.info("Received: " + message);
        JSONObject jsonData = JSON.parseObject(message);
        JSONObject header = jsonData.getJSONObject("header");
        String packId = header.getString("id");

        if (responseFutureList.containsKey(packId)) {
            CompletableFuture<JSONObject> responseFuture = responseFutureList.get(packId);
            if (responseFuture != null && !responseFuture.isDone()) {
                responseFuture.complete(jsonData);
            }
            responseFutureList.remove(packId);
        } else {
            plugin.onWsMsg(jsonData);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        logger.error(TextFormat.DARK_RED + "连接已断开,错误码:" + code + " 错误信息:" + reason);
        clientManager.clientReconnect();
    }

    @Override
    public void onError(Exception ex) {
        logger.error(TextFormat.DARK_RED + "连接发生错误!错误信息:" + ex.getMessage());
        clientManager.clientReconnect();
    }



    /**
     * 向服务端发送一条消息
     *
     * @param type 消息类型
     * @param body 消息数据
     */
    public void sendMessage(String type, JSONObject body) {
        String newPackId = PackId.getPackID();
        sendMessage(type, body, newPackId);
    }

    /**
     * 向服务端发送一条消息
     *
     * @param type   消息类型
     * @param body   消息数据
     * @param packId 消息Id
     */
    public void sendMessage(String type, JSONObject body, String packId) {
        JSONObject data = new JSONObject();
        JSONObject header = new JSONObject();
        header.put("type", type);
        header.put("id", packId);
        data.put("header", header);
        data.put("body", body);
        if (this.isOpen()) {
            this.send(data.toJSONString());
        }
    }

    /**
     * 向服务端发送一条消息并获取返回值
     *
     * @param type 消息类型
     * @param body 消息数据
     * @return 消息回报体
     */
    public CompletableFuture<JSONObject> sendRequestAndAwaitResponse(String type, JSONObject body) {
        String newPackId = PackId.getPackID();
        return sendRequestAndAwaitResponse(type, body, newPackId);
    }

    /**
     * 向服务端发送一条消息并获取返回值
     *
     * @param type   消息类型
     * @param body   消息数据
     * @param packId 消息Id
     * @return 消息回报体
     */
    public CompletableFuture<JSONObject> sendRequestAndAwaitResponse(String type, JSONObject body, String packId) {
        if (this.isOpen()) {
            //打包数据并发送
            JSONObject data = new JSONObject();
            JSONObject header = new JSONObject();
            header.put("type", type);
            header.put("id", packId);
            data.put("header", header);
            data.put("body", body);
            this.send(data.toJSONString());

            //存储回报
            CompletableFuture<JSONObject> responseFuture = new CompletableFuture<>();
            responseFutureList.put(packId, responseFuture);

            return responseFuture;
        } else {
            throw new IllegalStateException("WebSocket connection is not open.");
        }
    }

    /**
     * 向服务端发送一条回报
     *
     * @param msg  回报消息
     * @param type 回报类型：success|error
     */
    public void respone(String msg, String type) {
        String newPackId = PackId.getPackID();
        this.respone(msg, type, newPackId);
    }

    /**
     * 向服务端发送一条回报
     *
     * @param msg    回报消息
     * @param type   回报类型：success|error
     * @param packId 回报Id
     */
    public void respone(String msg, String type, String packId) {
        JSONObject body = new JSONObject();
        body.put("msg", msg);
        sendMessage(type, body, packId);
    }

    /**
     * 向服务端握手
     */
    private void shakeHand() {
        BotConfig config = HuHoBot.CONFIG;
        JSONObject body = new JSONObject();
        body.put("serverId", config.getServerId());
        if (config.getHashKey().isEmpty()) {
            body.put("hashKey", "");
        } else {
            body.put("hashKey", config.getHashKey());
        }
        body.put("name", plugin.getServer().getName());
        body.put("version", plugin.getDescription().getVersion());
        body.put("platform", "nukkit");
        sendMessage("shakeHand", body);
    }
}