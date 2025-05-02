package cn.huohuas001.huHoBot.netEvent;

import cn.huohuas001.huHoBot.HuHoBot;
import cn.huohuas001.huHoBot.config.BotConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomRun extends EventRunner {
    private final HuHoBot plugin = HuHoBot.getInstance();

    public Map<String, BotConfig.CustomCommand> putCommandMap() {
        BotConfig config = getConfig();
        List<BotConfig.CustomCommand> commands = config.getCustomCommands();

        // 初始化 commandMap
        Map<String,BotConfig.CustomCommand> commandMap = new HashMap<>();
        for (BotConfig.CustomCommand cmd : commands) {
            // 基础空值检查
            if (cmd.getKey() == null || cmd.getKey().isEmpty()) {
                continue;
            }

            commandMap.put(cmd.getKey(), cmd);
        }
        return commandMap;
    }

    @Override
    public boolean run() {
        String keyWord = body.getString("key");
        List<String> param = body.getList("runParams", String.class);

        Map<String, BotConfig.CustomCommand> commandMap = putCommandMap();
        // 测试查找功能
        BotConfig.CustomCommand result = commandMap.get(keyWord);
        if (result == null) {

            return false;
        } else {
            String command = result.getCommand();
            for (int i = 0; i < param.size(); i++) {
                int replaceNum = i + 1;
                command = command.replace("&" + replaceNum, param.get(i));
            }
            if (result.getPermission() > 0) {
                respone("权限不足，若您是管理员，请使用/管理员执行", "error");
                return false;
            }
            runCommand(command);
        }

        return true;
    }
}
