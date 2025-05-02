package cn.huohuas001.huHoBot.netEvent;

import cn.huohuas001.huHoBot.HuHoBot;

public class DelAllowList extends EventRunner {
    @Override
    public boolean run() {
        String XboxId = body.getString("xboxid");
        HuHoBot.getInstance().getServer().removeWhitelist(XboxId);
        String name = HuHoBot.getInstance().getServer().getName();
        respone(name + "已接受删除名为" + XboxId + "的白名单请求", "success");
        return true;
    }
}
