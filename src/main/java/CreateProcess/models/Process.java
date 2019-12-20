package CreateProcess.models;

import ConfigFile.models.ConfigFile;
import ConfigFile.models.TheVariable;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Process {

    public String rootPath;
    public String processName;

    public Process(@NotNull String rootPath, @NotNull String processName) {
        this.rootPath = rootPath;
        this.processName = processName;
    }

    public void createFileIfNotExist(@NotNull Template template) throws IOException {
        template.replace("Process Name", processName);
        File file = new File(rootPath + template.path);
        boolean mkdirs = file.getParentFile().mkdirs();
        if (!file.createNewFile() || template.content == null) {
            return;
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(template.content);
        }
    }

    public static void createIfNotExist(String rootPath, String processName) throws IOException {
        ConfigFile configFile = new ConfigFile(rootPath);
        configFile.getProcess("create-new-process").execute(new TheVariable(processName));
    }
}
