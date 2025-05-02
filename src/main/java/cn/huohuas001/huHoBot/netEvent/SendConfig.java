package cn.huohuas001.huHoBot.netEvent;

import cn.huohuas001.huHoBot.HuHoBot;
import cn.huohuas001.huHoBot.config.BotConfig;
import cn.nukkit.utils.TextFormat;

public class SendConfig extends EventRunner {
    private final HuHoBot plugin = HuHoBot.getInstance();

    @Override
    public boolean run() {
        String hashKey = body.getString("hashKey");
        plugin.setHashKey(hashKey);
        plugin.getLogger().info(TextFormat.GOLD + "配置文件已接受.");
        plugin.reloadBotConfig();
        plugin.getLogger().info(TextFormat.GOLD + "自动断开连接以刷新配置文件...");
        HuHoBot.getClientManager().shutdownClient();
        return true;
    }
}
