package cn.huohuas001.huHoBot.netEvent;

import cn.huohuas001.huHoBot.HuHoBot;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.TextFormat;

public class ShutDown extends EventRunner {
    private final PluginLogger logger = HuHoBot.getInstance().getLogger();

    @Override
    public boolean run() {
        logger.error(TextFormat.DARK_RED + "服务端命令断开连接 原因:" + body.getString("msg"));
        logger.error(TextFormat.DARK_RED + "此错误具有不可容错性!请检查插件配置文件!");
        logger.warning(TextFormat.GOLD + "正在断开连接...");
        HuHoBot.getClientManager().setShouldReconnect(false);
        HuHoBot.getClientManager().shutdownClient();
        return true;
    }
}