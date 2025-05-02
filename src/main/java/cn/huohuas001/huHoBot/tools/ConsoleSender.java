package cn.huohuas001.huHoBot.tools;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.lang.CommandOutputContainer;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.GameRule;
import cn.nukkit.network.protocol.types.CommandOutputMessage;
import cn.nukkit.permission.Permission;
import cn.nukkit.permission.PermissionAttachment;
import cn.nukkit.permission.PermissionAttachmentInfo;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.MainLogger;
import lombok.Getter;

import java.util.Iterator;
import java.util.Map;

public class ConsoleSender implements CommandSender {
    private final ConsoleCommandSender console;
    @Getter
    public final StringBuilder output = new StringBuilder();

    public ConsoleSender(ConsoleCommandSender console){
        this.console = console;
    }

    @Override
    public void sendMessage(String message) {
        message = this.getServer().getLanguage().translateString(message);
        String[] var2 = message.trim().split("\n");
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String line = var2[var4];
            if (line.length() > 0) {
                this.output.append(line).append("\n");
            }
            MainLogger.getLogger().info(line);
        }

    }

    @Override
    public void sendMessage(TextContainer message) {
        this.sendMessage(this.getServer().getLanguage().translate(message));
    }

    @Override
    public void sendCommandOutput(CommandOutputContainer container) {
        if (this.getLocation().getLevel().getGameRules().getBoolean(GameRule.SEND_COMMAND_FEEDBACK)) {
            Iterator var2 = container.getMessages().iterator();

            while(var2.hasNext()) {
                CommandOutputMessage msg = (CommandOutputMessage)var2.next();
                String text = this.getServer().getLanguage().translate(new TranslationContainer(msg.getMessageId(), msg.getParameters()));
                this.sendMessage(text);
            }
        }
    }

    @Override
    public Server getServer() {
        return console.getServer();
    }

    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isPermissionSet(String s) {
        return console.isPermissionSet(s);
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return console.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(String s) {
        return console.hasPermission(s);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return console.hasPermission(permission);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return console.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s) {
        return console.addAttachment(plugin, s);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, Boolean aBoolean) {
        return console.addAttachment(plugin, s, aBoolean);
    }

    @Override
    public void removeAttachment(PermissionAttachment permissionAttachment) {
        console.removeAttachment(permissionAttachment);
    }

    @Override
    public void recalculatePermissions() {
        console.recalculatePermissions();
    }

    @Override
    public Map<String, PermissionAttachmentInfo> getEffectivePermissions() {
        return console.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean b) {

    }
}
