package cn.huohuas001.huHoBot.gameEvent;

import cn.huohuas001.huHoBot.HuHoBot;
import cn.huohuas001.huHoBot.config.BotConfig;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import com.alibaba.fastjson2.JSONObject;

public class onChat implements Listener{
    private final HuHoBot plugin;

    public onChat(HuHoBot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        String message = event.getMessage();
        String playerName = event.getPlayer().getName();
        BotConfig config = HuHoBot.CONFIG;
        String format = config.getChatFormat().getFromGame();
        String prefix = config.getChatFormat().getPostPrefix();
        boolean isPostChat = config.getChatFormat().isPostChat();
        String serverId = config.getServerId();

        if (message.startsWith(prefix) && isPostChat) {
            JSONObject body = new JSONObject();
            body.put("serverId", serverId);
            String formated = format.replace("{name}", playerName).replace("{msg}", message.substring(prefix.length()));
            body.put("msg", formated);
            HuHoBot.getClientManager().getClient().sendMessage("chat", body);
        }
    }
}
