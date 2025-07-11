package cn.huohuas001.huHoBot.netEvent;

import cn.huohuas001.huHoBot.HuHoBot;
import cn.huohuas001.huHoBot.timeTask.SendHeart;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.TextFormat;


public class Shaked extends EventRunner {
    private final HuHoBot plugin = HuHoBot.getInstance();
    private final PluginLogger logger = plugin.getLogger();

    private void shakedProcess() {
        HuHoBot.getClientManager().setShouldReconnect(true);
        HuHoBot.getClientManager().cancelCurrentTask();
        HuHoBot.getClientManager().setAutoDisConnectTask();
        HuHoBot.getInstance().getServer().getScheduler().scheduleDelayedRepeatingTask(new SendHeart(HuHoBot.getInstance()), 0, 5 * 20);
    }

    @Override
    public boolean run() {
        int code = body.getInteger("code");
        String msg = body.getString("msg");
        switch (code) {
            case 1:
                logger.info("与服务端握手成功.");
                shakedProcess();
                break;
            case 2:
                logger.info("握手完成!,附加消息:" + msg);
                shakedProcess();
                break;
            case 3:
                logger.error(TextFormat.DARK_RED + "握手失败，客户端密钥错误.");
                HuHoBot.getClientManager().setShouldReconnect(false);
                break;
            case 6:
                logger.info("与服务端握手成功，服务端等待绑定...");
                shakedProcess();
                break;
            default:
                logger.error(TextFormat.DARK_RED + "握手失败，原因" + msg);
        }
        return true;
    }
}
