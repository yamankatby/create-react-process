package Process.models;

import Utilities.ConfigFile;
import Utilities.Constant;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Process {

    public Constant name;
    public ProcessFile view;
    public ProcessFile types;
    public ProcessFile actions;
    public ProcessFile reducers;
    public ProcessFile sagas;
    public ProcessFile apis;

    public Process(String name) {
        this.name = new Constant("Process Name", name);
        view = new ProcessFile(ConfigFile.rootPath.concat(String.format("/src/processes/%s/views/%s.tsx", this.name.camelCase, this.name.pascalCase)));
        types = new ProcessFile(ConfigFile.rootPath.concat(String.format("/src/processes/%s/logic/types.ts", this.name.camelCase)));
        actions = new ProcessFile(ConfigFile.rootPath.concat(String.format("/src/processes/%s/logic/actions.ts", this.name.camelCase)));
        reducers = new ProcessFile(ConfigFile.rootPath.concat(String.format("/src/processes/%s/logic/reducers.ts", this.name.camelCase)));
        sagas = new ProcessFile(ConfigFile.rootPath.concat(String.format("/src/processes/%s/logic/sagas.ts", this.name.camelCase)));
        apis = new ProcessFile(ConfigFile.rootPath.concat(String.format("/src/processes/%s/logic/apis.ts", this.name.camelCase)));
    }

    public void createViewFileIfNotExists() throws IOException {
        String template = ConfigFile.fetchTemplate("view-file");
        template = name.format(template);
        view.createIfNotExists(template);
    }

    public void createTypesFileIfNotExists() throws IOException {
        String template = ConfigFile.fetchTemplate("types-file");
        template = name.format(template);
        types.createIfNotExists(template);
    }

    public void createActionsFileIfNotExists() throws IOException {
        String template = ConfigFile.fetchTemplate("actions-file");
        template = name.format(template);
        actions.createIfNotExists(template);
    }

    public void createReducersFileIfNotExists() throws IOException {
        String template = ConfigFile.fetchTemplate("reducers-file");
        template = name.format(template);
        reducers.createIfNotExists(template);
    }

    public void createSagasFileIfNotExists() throws IOException {
        String template = ConfigFile.fetchTemplate("sagas-file");
        template = name.format(template);
        sagas.createIfNotExists(template);
    }

    public void createAPIsFileIfNotExists() throws IOException {
        String template = ConfigFile.fetchTemplate("apis-file");
        template = name.format(template);
        apis.createIfNotExists(template);
    }

    @NotNull
    public static Process createProcess(String name) throws IOException {
        Process process = new Process(name);
        process.createViewFileIfNotExists();
        process.createTypesFileIfNotExists();
        process.createActionsFileIfNotExists();
        process.createReducersFileIfNotExists();
        process.createSagasFileIfNotExists();
        process.createAPIsFileIfNotExists();
        return process;
    }

    @NotNull
    public static Process[] fetchProcesses() {
        File file = new File(ConfigFile.rootPath.concat("/src/processes/"));
        String[] directories = file.list((dir, name) -> new File(dir, name).isDirectory());
        ArrayList<Process> processes = new ArrayList<>();
        assert directories != null;
        for (String directory : directories) {
            String name = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(directory), ' ');
            processes.add(new Process(name));
        }
        return processes.toArray(new Process[0]);
    }

    @Override
    public String toString() {
        return name.raw;
    }
}
