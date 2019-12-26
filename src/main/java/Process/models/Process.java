package Process.models;

import Action.models.Action;
import Utilities.ConfigFile;
import Utilities.Constant;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Process {

    private String name;
    private Constant nameConstant;

    public String viewPath;
    public String typesPath;
    public String actionsPath;
    public String reducersPath;
    public String sagasPath;
    public String apisPath;

    public Process(String name) {
        this.name = name;
        this.nameConstant = new Constant("Process Name", name);

        this.viewPath = ConfigFile.rootPath.concat(String.format("/src/processes/%s/views/%s.tsx", nameConstant.camelCase, nameConstant.pascalCase));
        this.typesPath = ConfigFile.rootPath.concat(String.format("/src/processes/%s/logic/types.ts", nameConstant.camelCase));
        this.actionsPath = ConfigFile.rootPath.concat(String.format("/src/processes/%s/logic/actions.ts", nameConstant.camelCase));
        this.reducersPath = ConfigFile.rootPath.concat(String.format("/src/processes/%s/logic/reducers.ts", nameConstant.camelCase));
        this.sagasPath = ConfigFile.rootPath.concat(String.format("/src/processes/%s/logic/sagas.ts", nameConstant.camelCase));
        this.apisPath = ConfigFile.rootPath.concat(String.format("/src/processes/%s/logic/apis.ts", nameConstant.camelCase));
    }

    private void createFileIfNotExists(String path, ConfigFile.BreakPoint breakPoint) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        if (!file.createNewFile()) return;

        String fileTemplate = ConfigFile.fetchTemplate(breakPoint);
        fileTemplate = nameConstant.format(fileTemplate);
        if (fileTemplate.equals("")) return;

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(fileTemplate);
        }
    }

    public void createViewFile() throws IOException {
        createFileIfNotExists(viewPath, ConfigFile.BreakPoint.VIEW_FILE);
    }

    public void createTypesFile() throws IOException {
        createFileIfNotExists(typesPath, ConfigFile.BreakPoint.TYPES_FILE);
    }

    public void createActionsFile() throws IOException {
        createFileIfNotExists(actionsPath, ConfigFile.BreakPoint.ACTIONS_FILE);
    }

    public void createReducersFile() throws IOException {
        createFileIfNotExists(reducersPath, ConfigFile.BreakPoint.REDUCERS_FILE);
    }

    public void createSagasFile() throws IOException {
        createFileIfNotExists(sagasPath, ConfigFile.BreakPoint.SAGAS_FILE);
    }

    public void createAPIsFile() throws IOException {
        createFileIfNotExists(apisPath, ConfigFile.BreakPoint.APIS_FILE);
    }

    public static void createProcess(String name) throws IOException {
        Process process = new Process(name);
        process.createViewFile();
        process.createTypesFile();
        process.createActionsFile();
        process.createReducersFile();
        process.createSagasFile();
        process.createAPIsFile();

        Action clearAll = new Action("fdsafads", "clear all", true, true, true, true, new ArrayList<>(), new ArrayList<>());
        Action some = new Action("fdsafads", "do some else", true, true, true, true, new ArrayList<>(), new ArrayList<>());
        Action clearasdfAll = new Action("fdsafads", "another al", true, true, true, true, new ArrayList<>(), new ArrayList<>());
        Action somffe = new Action("fdsafads", "asldjf", true, true, true, true, new ArrayList<>(), new ArrayList<>());
        Action cledsfarAll = new Action("fdsafads", "fdsaas", true, true, true, true, new ArrayList<>(), new ArrayList<>());
        Action asdf = new Action("fdsafads", "aakjdkd", true, true, true, true, new ArrayList<>(), new ArrayList<>());
        clearAll.execute();
        some.execute();
        clearasdfAll.execute();
        somffe.execute();
        cledsfarAll.execute();
        asdf.execute();
    }
}
