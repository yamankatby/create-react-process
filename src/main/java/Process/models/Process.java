package Process.models;

import Action.models.Action;
import Action.models.ActionParam;
import Utilities.ConfigFile;
import Utilities.Constant;

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
        name.format(template);
        view.createIfNotExists(template);
    }

    public void createTypesFileIfNotExists() throws IOException {
        String template = ConfigFile.fetchTemplate("types-file");
        name.format(template);
        types.createIfNotExists(template);
    }

    public void createActionsFileIfNotExists() throws IOException {
        String template = ConfigFile.fetchTemplate("actions-file");
        name.format(template);
        actions.createIfNotExists(template);
    }

    public void createReducersFileIfNotExists() throws IOException {
        String template = ConfigFile.fetchTemplate("reducers-file");
        name.format(template);
        reducers.createIfNotExists(template);
    }

    public void createSagasFileIfNotExists() throws IOException {
        String template = ConfigFile.fetchTemplate("sagas-file");
        name.format(template);
        sagas.createIfNotExists(template);
    }

    public void createAPIsFileIfNotExists() throws IOException {
        String template = ConfigFile.fetchTemplate("apis-file");
        name.format(template);
        apis.createIfNotExists(template);
    }

    public void reflect() throws IOException {
        view.reflect();
        types.reflect();
        actions.reflect();
        reducers.reflect();
        sagas.reflect();
        apis.reflect();
    }

    public static void createProcess(String name) throws IOException {
        Process process = new Process(name);
        process.createViewFileIfNotExists();
        process.createTypesFileIfNotExists();
        process.createActionsFileIfNotExists();
        process.createReducersFileIfNotExists();
        process.createSagasFileIfNotExists();
        process.createAPIsFileIfNotExists();

        ArrayList<ActionParam> actionParams = new ArrayList<>();
        actionParams.add(new ActionParam("username", "string"));
        actionParams.add(new ActionParam("profile", "Profile"));
        actionParams.add(new ActionParam("isLoggedIn", "boolean"));
        Action action = new Action(process, "fetch profile", false, true, true, actionParams);
        action.execute();

        Action actionf = new Action(process, "fetch profile result", true, false, false, new ArrayList<>());
        actionf.execute();
    }
}
