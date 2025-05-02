package cn.huohuas001.huHoBot.netEvent;


public class RunCommand extends EventRunner {
    @Override
    public boolean run() {
        String command = body.getString("cmd");
        runCommand(command);
        return true;
    }
}
