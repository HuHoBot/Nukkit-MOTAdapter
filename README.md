# HuHoBot Nukkit-MOT Adapter

[![GitHub Release](https://img.shields.io/github/v/release/HuHoBot/Nukkit-MOTAdapter?style=for-the-badge)](https://github.com/HuHoBot/Nukkit-MOTAdapter/releases)
[![License](https://img.shields.io/github/license/HuHoBot/Nukkit-MOTAdapter?style=for-the-badge)](https://github.com/HuHoBot/Nukkit-MOTAdapter/blob/main/LICENSE)
[![Build Status](https://img.shields.io/github/actions/workflow/status/HuHoBot/Nukkit-MOTAdapter/release.yml?style=for-the-badge)](https://github.com/HuHoBot/Nukkit-MOTAdapter/actions)

新一代Minecraft服务器管理机器人解决方案，突破传统机器人框架限制，提供更安全稳定的交互体验。

## 🌟 核心优势

| 特性     | 传统方案             | HuHoBot           |
|--------|------------------|-------------------|
| 账号安全   | ❌ 需要实体QQ号，存在封号风险 | ✅ 无QQ第三方客户端依赖，零风控 |
| 部署复杂度  | ❌ 需搭建完整机器人框架     | ✅ 即装即用，一键绑定       |
| 服务器兼容性 | ❌ 部分面板服不支持       | ✅ 全平台兼容，有网即用      |
| 协议更新影响 | ❌ 需要频繁适配新协议      | ✅ 协议无关设计，相对稳定     |

## 🚀 功能特性

### 核心功能

- **无缝绑定**：通过WebSocket实现服务器与控制端即时绑定
- **跨平台支持**：适配Nukkit-MOT全版本
- **智能风控规避**：基于事件驱动的非侵入式通信协议

### 功能列表

<details>
<summary>📜 点我查看</summary>

| 命令     | 描述             |
|--------|----------------|
| /添加白名单 | 向服务器内添加一个白名单   |
| /删除白名单 | 向服务器内删除一个白名单   |
| /绑定    | 绑定服务器          |
| /设置名称  | 设置自己在本群群服互通的名称 |
| /发信息   | 群服互通向服务器内发送消息  |
| /执行命令  | 向服务器发送执行命令的请求  |
| /查白名单  | 查询服务器内置白名单     |
| /查在线   | 查询服务器在线名单      |
| /在线服务器 | 查询在线服务器        |
| /执行    | 执行自定义指令        |
| /管理员执行 | 以管理员身份运行自定义内容  |

</details>

### 进阶功能

- **扩展API**
  - 自定义命令系统(详见下文)

## 📥 安装指南

### 环境要求

- Java 17+ Runtime
- **任意支持的 Nukkit-MOT 核心**

### 快速开始

1. **访问 GitHub Releases 页面**：
  - 打开浏览器，访问 [HuHoBot-Nukkit-MOTAdapter Releases](https://github.com/HuHoBot/Nukkit-MOTAdapter/releases)
  - 下载最新版本的 `HuHoBot-vx.x.x-Nukkit-MOT.jar` 文件

2. **放置插件文件**：
  - 将下载的 `HuHoBot-vx.x.x-Nukkit-MOT.jar` 文件放入服务器的 `plugins` 目录中

3. **重启服务器**：
  - 重启你的 Minecraft 服务器以加载新插件

4. **添加机器人**
- [加入交流群](http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=N6tVRxGR8sDwYHBx9YgAhkqRTp1gseyk&authKey=M7Fd3Op6nHjXjSXefBtHBPqIq2wtX8AfufZor9DrfRrJkhyS2rohkt7iuOSwbtn8&noverify=0&group_code=1005746321) 点击HuHo_Bot头像并添加到群聊

5. **完成绑定**：
- 按照控制台提示完成服务器与机器人的绑定操作

### 高级配置（可选）

- 如果需要自定义功能，请参考`⚙️ 配置示例`进行详细设置

---

#### 注意事项：

- 确保服务器已正确安装 Java 17+ 运行时环境
- 插件兼容所有支持的 Nukkit-MOT 核心版本，具体版本请参考官方文档

## ⚙️ 配置示例

```yaml
#不用管
serverId: null
#不用管
hashKey: null

chatFormat:
  from_game: "<{name}> {msg}" #服内消息转发到群内时的文本
  from_group: "群:<{nick}> {msg}" #群内消息转发到服内时的文本
  post_chat: true #是否在群内发送消息到服务器内
  post_prefix: "" #群内消息转发到服内时的前缀

motd:
  server_ip: "play.easecation.net" #使用"/查在线"时的Motd图片地址（改成你的进服地址）
  server_port: 19132 #使用"/查在线"时的Motd图片端口（改成你的进服端口）
  api: "https://motdbe.blackbe.work/status_img?host={server_ip}:{server_port}" #使用"/查在线"时的Motd图片地址Api（无特殊需求勿动）（必须返回图片）
  text: "共{online}人在线" #使用"/查在线"时的Motd文本，可使用PlaceholderAPI，留空不显示
  output_online_list: true #是否显示在线列表
  post_img: true #是否显示Motd图片

#自定义执行命令
customCommand:
  - key: "加白名" #执行关键词，可使用"/执行 关键词 参数1 参数2"来执行自定义命令
    command: "whitelist add &1" #&1为参数占位符，第一个参数为&1，第二个&2，以此类推
    permission: 0 #0是普通权限，大于0则为管理员权限

  - key: "管理加白名"
    command: "whitelist add &1"
    permission: 1

```

## ❓ 常见问题

<details>
<summary>🤔 需要准备QQ号吗？</summary>
完全不需要！本方案采用全新的通信协议，彻底摆脱对第三方聊天平台的依赖。
</details>

<details>
<summary>🛡️ 支持哪些服务器版本？</summary>
✅ 理论支持所有Nukkit-MOT系核心
</details>

<details>
<summary>🔧 如何更新配置？</summary>
支持热重载配置：<code>/huhobot reload</code>
</details>

<details>
<summary>💬 关于群服消息互通</summary>
当调用`/发信息`时，可以在`五分钟内`回复`5次`该消息，机器人会优先选择最新的消息进行回复
<br/>
如果没有找到可以回复的消息，则无法发送消息
</details>

<details>
<summary>🌐 查在线显示其他服务器信息？</summary>
请修改配置文件中的 <code>motdUrl</code> 字段为你的服务器地址  
示例：<code>motdUrl: "play.yourserver.com:19132"</code>
</details>

<details>
<summary>🔍 查在线无反应怎么办？</summary>
排查步骤：  

1. 检查连接状态，使用 <code>/huhobot reconnect</code> 重连

2. 尝试清空 motdUrl 字段：<code>"motdUrl": ""</code>

</details>

<details>
<summary>⌨️ 执行命令无响应？</summary>
注意命令格式区别：  

- <code>/执行 加白</code> → 用于自定义指令回调

- <code>/执行命令 list</code> → 向控制台发送命令

</details>

<details>
<summary>👥 允许玩家自助加白名单？</summary>
请按上文配置文件示例配置customCommand字段

使用方式：<code>/执行 加白 "玩家ID"</code>（带空格参数需加引号）

</details>

<details>
<summary>👮 如何设置管理员？</summary>
在群内使用指令：  
<code>/管理帮助</code> → 查看管理指令列表
</details>

<details>
<summary>🏰 是否支持多个服务器？</summary>
当前版本每个群仅支持绑定一个服务器，多服务器绑定功能正在开发中  
如需管理多服务器，建议为每个服务器创建独立群组
</details>

### 配置自定义命令

#### 通过配置文件设置

在 `config.yml` 文件中，你可以通过 `customCommand` 字段来定义自定义命令。每个自定义命令包含以下属性：

- **key**：触发命令的关键词（字符串）
- **command**：实际执行的服务器命令（字符串）
- **permission**：权限级别（整数）

示例配置如下：

```yaml
customCommand:
  - key: "加白名" #执行关键词，可使用"/执行 关键词 参数1 参数2"来执行自定义命令
    command: "whitelist add &1" #&1为参数占位符，第一个参数为&1，第二个&2，以此类推
    permission: 0 #0是普通权限，大于0则为管理员权限

  - key: "管理加白名"
    command: "whitelist add &1"
    permission: 1
```

## 📄 开源协议

[GNU General Public License v3.0](LICENSE) - 自由使用、修改和分发，但需遵守以下条款：

- **开源义务**：任何衍生作品必须保持开源
- **相同许可**：修改后的版本必须使用相同许可证
- **版权声明**：必须保留原始版权声明

完整协议文本请查看 [LICENSE](LICENSE) 文件

## 🤝 参与贡献

欢迎提交PR或通过[Discussions](https://github.com/HuHoBot/Nukkit-MOTAdapter/discussions)提出建议

