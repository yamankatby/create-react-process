package Action.models;

import Process.models.Process;
import Utilities.ConfigFile;
import Utilities.Constant;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Action {

    private Process process;
    private Constant name;

    private boolean isResultAction;
    private boolean hasReducer;
    private boolean hasSaga;
    private boolean hasAPI;

    private ArrayList<ActionParam> params;

    public Action(Process process, String name, boolean hasReducer, boolean hasSaga, boolean hasAPI, ArrayList<ActionParam> params) {
        this.process = process;
        this.name = new Constant("Action Name", name);
        this.hasReducer = hasReducer;
        this.hasSaga = hasSaga;
        this.hasAPI = hasAPI;
        this.params = params;
    }

    private void createActionType() throws FileNotFoundException {
        String template = ConfigFile.fetchTemplate("action-type");
        if (template.equals("")) return;
        template = process.name.format(template);
        template = name.format(template);

        Pattern pattern = Pattern.compile("(?<=export\\senum\\sActionTypes\\s\\{)(.|\\n)*?(?=})");
        Matcher matcher = pattern.matcher(process.types.content);
        if (!matcher.find()) return;
        String actionTypes = matcher.group();

        process.types.content = process.types.content.replaceAll(pattern.pattern(), "\n\t" + actionTypes.trim() + "\n\t" + template + "\n");
    }

    private void createActionInterface() throws FileNotFoundException {
        String template = ConfigFile.fetchTemplate("action-interface");
        if (template.equals("")) return;
        template = process.name.format(template);
        template = name.format(template);
        template = template.replace("$BaseActionInterface", isResultAction ? "AppResultAction" : "AppAction");

        StringBuilder actionParams = new StringBuilder();
        for (ActionParam actionParam : params) {
            actionParams.append("\n\t").append(actionParam.getName()).append(": ").append(actionParam.getType()).append(";");
        }
        template = template.replace("$ActionParams", actionParams.toString().trim());

        process.types.content = process.types.content.replace("export type Action", template + "\n\n" + "export type Action");
        createActionInterfaceLink();
    }

    private void createActionInterfaceLink() throws FileNotFoundException {
        String template = ConfigFile.fetchTemplate("action-interface-link");
        if (template.equals("")) return;
        template = process.name.format(template);
        template = name.format(template);
        template = template.replace("$ActionInterface", name.pascalCase + "Action");

        Pattern pattern = Pattern.compile("(?<=export\\stype\\sAction\\s=)(.|\\n)*?(?=;)");
        Matcher matcher = pattern.matcher(process.types.content);
        if (!matcher.find()) return;
        String actionInterfacesLinks = matcher.group();

        process.types.content = process.types.content.replaceAll(pattern.pattern(), actionInterfacesLinks + "\n\t" + template);
    }

    public void createActionTypes() throws IOException {
        process.createTypesFileIfNotExists();
        createActionType();
        if (params.size() > 0) createActionInterface();
    }

    public void createAction() throws IOException {
        process.createActionsFileIfNotExists();
        String template = ConfigFile.fetchTemplate("action");
        if (template.equals("")) return;
        template = process.name.format(template);
        template = name.format(template);

        String actionInterface = params.size() > 0 ? name.pascalCase + "Action" : isResultAction ? "AppResultAction" : "AppAction";
        template = template.replace("$ActionInterface", actionInterface);

        StringBuilder paramsWithTypes = new StringBuilder(isResultAction ? "hasError: boolean," : "");
        StringBuilder params = new StringBuilder(isResultAction ? "\n\thasError," : "");
        for (ActionParam actionParam : this.params) {
            paramsWithTypes.append(actionParam.getName()).append(": ").append(actionParam.getType()).append(",");
            params.append("\n\t").append(actionParam.getName()).append(",");
        }
        template = template.replace("$ActionParamsWithTypes", paramsWithTypes.toString().trim());
        template = template.replace("$ActionParams", params.toString().trim());

        process.actions.content = process.actions.content.replaceAll("$", "\n" + template);
    }

    public void createReducer() throws IOException {
        process.createReducersFileIfNotExists();
        String template = ConfigFile.fetchTemplate("reducer");
        if (template.equals("")) return;
        template = process.name.format(template);
        template = name.format(template);

        process.reducers.content = process.reducers.content.replaceAll("default:\\n\\t{3}return\\sstate;", template + "\n\t\tdefault:\n\t\t\treturn state;");
    }

    public void createSaga() throws IOException {
        process.createSagasFileIfNotExists();
        String template = ConfigFile.fetchTemplate("saga");
        if (template.equals("")) return;
        template = process.name.format(template);
        template = name.format(template);

        if (params.size() == 0) {
            template = template.replace("action: $ActionInterface", "");
            template = template.replace("\nconst { $params } = action;", "");
        } else {
            template = template.replace("$ActionInterface", name.pascalCase + "Action");
            StringBuilder params = new StringBuilder();
            for (ActionParam actionParam : this.params) {
                params.append(actionParam.getName()).append(",");
            }
            template = template.replace("$params", params.toString());
        }

        if (hasAPI) template = createSagaRequest(template);
        process.sagas.content = process.sagas.content.replace("export default [", template + "\n\nexport default [");
        createSagaLink();
    }

    private String createSagaRequest(String saga) throws FileNotFoundException {
        String template = ConfigFile.fetchTemplate("saga-request");
        if (template.equals("")) return saga;
        template = process.name.format(template);
        template = name.format(template);
        return saga.replaceAll("(?<=try\\s\\{)(.|\\n)*?(?=})", "\n" + template + "\n\t");
    }

    private void createSagaLink() throws FileNotFoundException {
        String template = ConfigFile.fetchTemplate("saga-link");
        if (template.equals("")) return;
        template = process.name.format(template);
        template = name.format(template);

        Pattern pattern = Pattern.compile("(?<=export\\sdefault\\s\\[)(.|\\n)*?(?=];)");
        Matcher matcher = pattern.matcher(process.sagas.content);
        if (!matcher.find()) return;
        String sagasLinks = matcher.group();

        process.sagas.content = process.sagas.content.replaceAll("(?<=export\\sdefault\\s\\[)(.|\\n)*?(?=];)", "\n" + sagasLinks.trim() + template + "\n");
    }

    public void createAPI() throws IOException {
        process.createAPIsFileIfNotExists();
        String template = ConfigFile.fetchTemplate("api");
        if (template.equals("")) return;
        template = process.name.format(template);
        template = name.format(template);

        process.apis.content = process.apis.content.replaceAll("$", "\n" + template);
    }

    public void execute() throws IOException {
        createActionTypes();
        createAction();
        if (hasReducer) createReducer();
        if (hasSaga) createSaga();
        if (hasAPI) createAPI();
        process.reflect();
    }
}
