package cn.huohuas001.huHoBot.timeTask;

import cn.huohuas001.huHoBot.HuHoBot;
import cn.nukkit.scheduler.PluginTask;



import cn.nukkit.scheduler.PluginTask;

/**
 * author: MagicDroidX
 * ExamplePlugin Project
 */
public class SendHeart extends PluginTask<HuHoBot> {

    public SendHeart(HuHoBot owner) {
        super(owner);
    }

    @Override
    public void onRun(int currentTick) {
        HuHoBot plugin = HuHoBot.getInstance();
        //plugin.getClientManager().sendHeart();
        //this.getOwner().getLogger().info("I've run on tick " + currentTick);
    }
}
