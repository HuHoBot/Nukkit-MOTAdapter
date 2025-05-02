package cn.huohuas001.huHoBot.netEvent;

import cn.huohuas001.huHoBot.HuHoBot;
import cn.nukkit.Player;
import com.alibaba.fastjson2.JSONObject;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class QueryOnline extends EventRunner {
    @Override
    public boolean run() {
        //获取motd Config
        String server_ip = getConfig().getMotd().getServerIp();
        int server_port = getConfig().getMotd().getServerPort();
        String api = getConfig().getMotd().getApi();
        String text = getConfig().getMotd().getText();
        boolean output_online_list = getConfig().getMotd().isOutputOnlineList();
        boolean post_img = getConfig().getMotd().isPostImg();

        StringBuilder onlineNameString = new StringBuilder();
        int onlineSize = -1;
        Map<UUID, Player> onlinePlayers = HuHoBot.getInstance().getServer().getOnlinePlayers();
        if (output_online_list && !onlinePlayers.isEmpty()) {
            onlinePlayers.values().forEach(player -> {
                onlineNameString.append(player.getName()).append("\n");
            });
        } else if (output_online_list) {
            onlineNameString.append("\n当前没有在线玩家\n");
        }

        onlineNameString.append(text.replace("{online}", String.valueOf(onlineSize)));


        // 构造JSON对象
        JSONObject list = new JSONObject();
        list.put("msg", onlineNameString);
        list.put("url", server_ip + ":" + server_port);
        list.put("imgUrl", api.replace("{server_ip}", server_ip).replace("{server_port}", String.valueOf(server_port)));
        list.put("post_img", post_img);
        list.put("serverType", "bedrock");
        JSONObject rBody = new JSONObject();
        rBody.put("list", list);

        //返回消息
        sendMessage("queryOnline", rBody);
        return true;
    }
}
