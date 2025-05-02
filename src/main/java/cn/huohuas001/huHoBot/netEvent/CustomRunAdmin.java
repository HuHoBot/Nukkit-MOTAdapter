package cn.huohuas001.huHoBot.netEvent;

import cn.huohuas001.huHoBot.HuHoBot;
import cn.huohuas001.huHoBot.config.BotConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomRunAdmin extends EventRunner {
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
            runCommand(command);
        }

        return true;
    }
}
