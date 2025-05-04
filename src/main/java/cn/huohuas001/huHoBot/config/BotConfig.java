package cn.huohuas001.huHoBot.config;

import cn.huohuas001.huHoBot.HuHoBot;
import cn.huohuas001.huHoBot.tools.PackId;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BotConfig {
    private final Config config;
    private final static int CurrentVersion = 2;

    // 根配置项
    @Getter private String serverId;
    @Getter private String hashKey;
    @Getter private final ChatFormatConfig chatFormat;
    @Getter private final MotdConfig motd;
    @Getter private List<CustomCommand> customCommands;
    @Getter private Integer version;

    public BotConfig() {
        HuHoBot.getInstance().saveResource("config.yml");
        config = new Config(
                new File(HuHoBot.getInstance().getDataFolder(), "config.yml"),
                Config.YAML,
                new ConfigSection() {{
                    // 默认配置
                    put("serverId", PackId.getPackID());
                    put("hashKey", null);
                    put("chatFormat", new ConfigSection() {{
                        put("from_game", "<{name}> {msg}");
                        put("from_group", "群:<{nick}> {msg}");
                    }});
                    put("motd", new ConfigSection() {{
                        put("server_ip", "play.easecation.net");
                        put("server_port", 19132);
                        put("api", "https://motdbe.blackbe.work/status_img?host={server_ip}:{server_port}");
                        put("text", "共{online}人在线");
                        put("output_online_list", true);
                        put("post_img", true);
                    }});
                    put("customCommand", Arrays.asList(
                            new ConfigSection() {{
                                put("key", "加白名");
                                put("command", "whitelist add &1");
                                put("permission", 0);
                            }},
                            new ConfigSection() {{
                                put("key", "管理加白名");
                                put("command", "whitelist add &1");
                                put("permission", 1);
                            }}
                    ));
                    put("version", 1);
                }}
        );

        // 初始化配置项
        serverId = config.getString("serverId");
        hashKey = config.getString("hashKey");
        chatFormat = new ChatFormatConfig(config.getSection("chatFormat"));
        motd = new MotdConfig(config.getSection("motd"));
        customCommands = config.getMapList("customCommand").stream()
                .map(section -> new CustomCommand((ConfigSection) section))
                .collect(Collectors.toList());
        version = config.getInt("version", 0); // 带默认值读取

        if (version < CurrentVersion) {
            migrateToV2();
        }
    }

    // region 内部配置类
    @Getter
    public static class ChatFormatConfig {
        private final String fromGame;
        private final String fromGroup;
        private final boolean postChat;
        private final String postPrefix;

        public ChatFormatConfig(ConfigSection section) {
            fromGame = section.getString("from_game", "<{name}> {msg}");
            fromGroup = section.getString("from_group", "群:<{nick}> {msg}");
            postChat = section.getBoolean("post_chat", true);  // 新增布尔型配置项，默认true
            postPrefix = section.getString("post_prefix", ""); // 新增字符串型配置项，默认空字符串
        }
    }

    @Getter
    public static class MotdConfig {
        private final String serverIp;
        private final int serverPort;
        private final String api;
        private final String text;
        private final boolean outputOnlineList;
        private final boolean postImg;

        public MotdConfig(ConfigSection section) {
            serverIp = section.getString("server_ip", "play.easecation.net");
            serverPort = section.getInt("server_port", 19132);
            api = section.getString("api", "https://motdbe.blackbe.work/status_img?host={server_ip}:{server_port}");
            text = section.getString("text", "共{online}人在线");
            outputOnlineList = section.getBoolean("output_online_list", true);
            postImg = section.getBoolean("post_img", true);
        }
    }

    @Getter
    public static class CustomCommand {
        private final String key;
        private final String command;
        private final int permission;

        public CustomCommand(ConfigSection section) {
            key = section.getString("key");
            command = section.getString("command");
            permission = section.getInt("permission", 0);
        }
    }
    // endregion

    public BotConfig setServerId(String serverId) {
        this.serverId = serverId;
        config.set("serverId", serverId); // 同步更新配置
        return this; // 支持链式调用
    }

    public BotConfig setHashKey(String hashKey) {
        this.hashKey = hashKey;
        config.set("hashKey", hashKey);
        return this;
    }

    // 保存配置
    public void save() {
        config.set("serverId", serverId);
        config.set("hashKey", hashKey);
        config.save();
    }

    // 新增迁移方法
    private void migrateToV2() {
        // 处理 chatFormat 配置升级
        ConfigSection chatFormatSection = config.getSection("chatFormat");
        if (!chatFormatSection.exists("post_chat")) {
            chatFormatSection.put("post_chat", true); // 添加默认值
        }
        if (!chatFormatSection.exists("post_prefix")) {
            chatFormatSection.put("post_prefix", ""); // 添加默认值
        }

        // 更新版本号
        config.set("version", 2);
        config.save(); // 立即保存迁移结果

        version = 2; // 更新内存中的版本号
    }
}
