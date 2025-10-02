package cn.huohuas001.huHoBot;

import cn.huohuas001.huHoBot.command.CommandManager;
import cn.huohuas001.huHoBot.config.BotConfig;
import cn.huohuas001.huHoBot.gameEvent.onChat;
import cn.huohuas001.huHoBot.netEvent.*;
import cn.huohuas001.huHoBot.tools.ConsoleSender;
import cn.huohuas001.huHoBot.tools.PackId;
import cn.nukkit.lang.LangCode;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.TextFormat;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuHoBot extends PluginBase {
    @Getter
    public static HuHoBot instance;
    @Getter
    public static LangCode serverLangCode;
    @Getter
    public static BotConfig CONFIG;

    public static PluginLogger logger;

    @Getter
    public static WebsocketClientManager clientManager; //Websocket客户端
    public bindRequest bindRequestObj;
    private final Map<String, EventRunner> eventList = new HashMap<>(); //事件列表


    @Override
    public void onLoad() {
        instance = this;
        CONFIG = new BotConfig();
        logger = getLogger();

        //初始化命令
        this.getServer().getCommandMap().register("huhobot", new CommandManager());

        //生成serverId
        if (CONFIG.getServerId().isEmpty() || CONFIG.getServerId() == "null") {
            CONFIG.setServerId(PackId.getPackID());
            CONFIG.save();
        }

        //初始化事件
        totalRegEvent();

    }

    @Override
    public void onEnable() {

        //连接
        clientManager = new WebsocketClientManager();
        clientManager.connectServer();

        //注册事件
        this.getServer().getPluginManager().registerEvents(new onChat(this), this);
        sendBindMessage();
        logger.info("HuHoBot Loaded. By HuoHuas001");
    }

    public boolean isNotBind() {
        return CONFIG.getHashKey().isEmpty() || CONFIG.getHashKey() == "null";
    }

    public void sendBindMessage() {
        if (isNotBind()) {
            String serverId = CONFIG.getServerId();
            String message = "服务器尚未在机器人进行绑定，请在群内输入\"/绑定 " + serverId + "\"";
            logger.warning(message);
        }
    }

    /**
     * 重载插件配置文件
     *
     * @return 是否重载成功
     */
    public boolean reloadBotConfig() {
        CONFIG = new BotConfig();
        return true;
    }

    /**
     * 注册Websocket事件
     *
     * @param eventName 事件名称
     * @param event     事件对象
     */
    private void registerEvent(String eventName, EventRunner event) {
        eventList.put(eventName, event);
    }

    /**
     * 统一事件注册
     */
    private void totalRegEvent() {
        registerEvent("sendConfig", new SendConfig());
        registerEvent("shaked", new Shaked());
        registerEvent("chat", new Chat());
        registerEvent("add", new AddAllowList());
        registerEvent("delete", new DelAllowList());
        registerEvent("cmd", new RunCommand());
        registerEvent("queryList", new QueryAllowList());
        registerEvent("queryOnline", new QueryOnline());
        registerEvent("shutdown", new ShutDown());
        registerEvent("run", new CustomRun());
        registerEvent("runAdmin", new CustomRunAdmin());
        registerEvent("heart", new Heart());
        bindRequestObj = new bindRequest();
        registerEvent("bindRequest", bindRequestObj);
    }

    /**
     * 当收到Websocket消息时的回调
     *
     * @param data 回调数据
     */
    public void onWsMsg(JSONObject data) {
        JSONObject header = data.getJSONObject("header");
        JSONObject body = data.getJSONObject("body");

        String type = header.getString("type");
        String packId = header.getString("id");

        EventRunner event = eventList.get(type);
        if (event != null) {
            event.EventCall(packId, body);
        } else {
            logger.error(TextFormat.DARK_RED + "在处理消息是遇到错误: 未知的消息类型");
            logger.error(TextFormat.DARK_RED + "此错误具有不可容错性!请检查插件是否为最新!");
            logger.info(TextFormat.AQUA + "正在断开连接...");
            clientManager.shutdownClient();
        }

    }

    /**
     * 运行命令
     *
     * @param command 命令
     * @param packId  消息包ID
     */
    public void runCommand(String command, String packId) {
        //String sendCmdMsg = ServerManager.sendCmd(command, true, true);
        ConsoleSender sender = new ConsoleSender(getServer().getConsoleSender());
        getServer().dispatchCommand(sender, command);
        clientManager.getClient().respone("已执行.\n" + sender.getOutput(), "success", packId);

    }

    /**
     * 重连HuHoBot服务器
     *
     * @return 是否连接成功
     */
    public boolean reconnect() {
        if (clientManager.isOpen()) {
            return false;
        }
        clientManager.connectServer();
        return true;
    }

    /**
     * 断连HuHoBot服务器
     *
     * @return 是否断连成功
     */
    public boolean disConnectServer() {
        clientManager.setShouldReconnect(false);
        return clientManager.shutdownClient();
    }

    public void setHashKey(String hashKey){
        CONFIG.setHashKey(hashKey);
        CONFIG.save();
        reloadBotConfig();
    }

}
