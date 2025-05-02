package cn.huohuas001.huHoBot.netEvent;

import cn.huohuas001.huHoBot.HuHoBot;
import cn.huohuas001.huHoBot.WsClient;
import cn.huohuas001.huHoBot.config.BotConfig;
import com.alibaba.fastjson2.JSONObject;

public class EventRunner {
    String packId;
    JSONObject body;

    void respone(String msg, String type) {
        WsClient client = HuHoBot.getClientManager().getClient();
        client.respone(msg, type, packId);
    }

    void sendMessage(String type, JSONObject body) {
        WsClient client = HuHoBot.getClientManager().getClient();
        client.sendMessage(type, body, packId);
    }

    BotConfig getConfig() {
        return HuHoBot.getCONFIG();
    }

    void runCommand(String command) {
        HuHoBot.getInstance().runCommand(command, packId);
    }

    public boolean EventCall(String packId, JSONObject body) {
        this.packId = packId;
        this.body = body;
        return run();
    }

    boolean run() {
        return true;
    }

    ;
}
