package cn.huohuas001.huHoBot.netEvent;

import cn.huohuas001.huHoBot.HuHoBot;

public class AddAllowList extends EventRunner {
    @Override
    public boolean run() {
        String XboxId = body.getString("xboxid");
        HuHoBot.getInstance().getServer().addWhitelist(XboxId);
        String name = HuHoBot.getInstance().getServer().getName();
        respone(name + "已接受添加名为" + XboxId + "的白名单请求", "success");
        return true;
    }
}
