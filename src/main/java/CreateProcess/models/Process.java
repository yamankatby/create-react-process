package CreateProcess.models;

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
        Process process = new Process(rootPath, processName);
        TemplatesManager templatesManager = new TemplatesManager();
        process.createFileIfNotExist(templatesManager.templates.get("view"));
        process.createFileIfNotExist(templatesManager.templates.get("types"));
        process.createFileIfNotExist(templatesManager.templates.get("actions"));
        process.createFileIfNotExist(templatesManager.templates.get("reducers"));
        process.createFileIfNotExist(templatesManager.templates.get("sagas"));
    }
}
