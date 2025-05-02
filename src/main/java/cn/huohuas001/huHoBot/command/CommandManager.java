package cn.huohuas001.huHoBot.command;

import cn.huohuas001.huHoBot.HuHoBot;
import cn.huohuas001.huHoBot.netEvent.bindRequest;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;

public class CommandManager extends PluginCommand<HuHoBot> {
    protected HuHoBot plugin = HuHoBot.getInstance();

    public CommandManager() {
        super("huhobot", HuHoBot.getInstance());
        this.setDescription("HuHoBot的控制命令");
        this.getCommandParameters().clear();

        this.getCommandParameters().put("pattern1", new CommandParameter[]{
                CommandParameter.newEnum("enum1", false, new CommandEnum("reload", "reconnect", "disconnect")),
        });
        this.getCommandParameters().put("pattern2", new CommandParameter[]{
                CommandParameter.newEnum("enum2", false, new String[]{"bind"}),
                CommandParameter.newType("bindCode", true, CommandParamType.MESSAGE)
        });
    }

    private void onReload(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                sender.sendMessage(TextFormat.DARK_RED + "你没有足够的权限.");
                return;
            }
        }

        if (HuHoBot.getInstance().reloadBotConfig()) {
            sender.sendMessage(TextFormat.AQUA + "重载机器人配置文件成功.");
        }
    }

    private void onReconnect(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                sender.sendMessage(TextFormat.DARK_RED + "你没有足够的权限.");
                return;
            }
        }

        if (HuHoBot.getInstance().reconnect()) {
            sender.sendMessage(TextFormat.GOLD + "重连机器人成功.");
        } else {
            sender.sendMessage(TextFormat.DARK_RED + "重连机器人失败：已在连接状态.");
        }

    }

    private void onDisconnect(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                sender.sendMessage(TextFormat.DARK_RED + "你没有足够的权限.");
                return;
            }
        }

        if (HuHoBot.getInstance().disConnectServer()) {
            sender.sendMessage(TextFormat.GOLD + "已断开机器人连接.");
        }
    }

    private void onBind(CommandSender sender, String[] args) {
        bindRequest obj = plugin.bindRequestObj;
        if (obj.confirmBind(args[1])) {
            sender.sendMessage(TextFormat.GOLD + "已向服务器发送确认绑定请求，请等待服务端下发配置文件.");
        } else {
            sender.sendMessage(TextFormat.DARK_RED + "绑定码错误，请重新输入.");
        }
    }

    private void onHelp(CommandSender sender, String[] args) {
        sender.sendMessage(TextFormat.AQUA + "HuHoBot 操作相关命令");
        sender.sendMessage(TextFormat.GOLD + ">" + TextFormat.DARK_GRAY + "/huhobot reload - 重载配置文件");
        sender.sendMessage(TextFormat.GOLD + ">" + TextFormat.DARK_GRAY + "/huhobot reconnect - 重新连接服务器");
        sender.sendMessage(TextFormat.GOLD + ">" + TextFormat.DARK_GRAY + "/huhobot disconnect - 断开服务器连接");
        sender.sendMessage(TextFormat.GOLD + ">" + TextFormat.DARK_GRAY + "/huhobot bind <bindCode:string> - 确认绑定");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("使用/huhobot help来获取更多详情");
            return false;
        }
        switch (args[0]) {
            case "reload":
                onReload(sender, args);
                break;
            case "reconnect":
                onReconnect(sender, args);
                break;
            case "disconnect":
                onDisconnect(sender, args);
                break;
            case "bind":
                onBind(sender, args);
                break;
            case "help":
                onHelp(sender, args);
                break;
            default:
                sender.sendMessage(TextFormat.DARK_RED + "使用/huhobot help来获取更多详情");
        }
        return true;
    }
}
