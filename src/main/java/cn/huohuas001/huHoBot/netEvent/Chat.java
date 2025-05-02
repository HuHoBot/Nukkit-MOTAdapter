package cn.huohuas001.huHoBot.netEvent;

import cn.huohuas001.huHoBot.HuHoBot;
import cn.huohuas001.huHoBot.config.BotConfig;

public class Chat extends EventRunner {
    @Override
    public boolean run() {
        String nick = body.getString("nick");
        String msg = body.getString("msg");
        BotConfig config = this.getConfig();
        String message = config.getChatFormat().getFromGroup().replace("{nick}", nick).replace("{msg}", msg);
        HuHoBot.getInstance().getServer().broadcastMessage(message);
        return true;
    }
}
