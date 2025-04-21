package wat.inz.kolektorlogow.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import rikka.shizuku.Shizuku;

public class CommandExecutor {
    private String logcatCommand;

    public void setLogcatCommand(String logcatCommand) {
        this.logcatCommand = logcatCommand;
    }

    public void cleanBuffer() throws IOException {
        executeCommand("logcat -c");
    }

    public BufferedReader gatherLogs(PermissionManager permissionManager) throws IOException {
        Process process = getProcess(permissionManager.getADBcheck(), logcatCommand);
        return new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

    private Process getProcess(boolean adb, String command) throws IOException {
        return adb ? executeShizukuCommand(command) : executeCommand(command);
    }

    private Process executeShizukuCommand(String command) throws IOException {
        return Shizuku.newProcess(command.split(" "), null, null);
    }

    private Process executeCommand(String command) throws IOException {
        return Runtime.getRuntime().exec(command);
    }
}
