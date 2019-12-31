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

    private ArrayList<ActionParam> params = new ArrayList<>();

    public Action(Process process, String name) {
        this.process = process;
        this.name = new Constant("Action name", name);
    }

    public void setResultAction(boolean resultAction) {
        this.name = new Constant("Action name", this.name.raw + " result");
        this.isResultAction = resultAction;
    }

    public void setHasReducer(boolean hasReducer) {
        this.hasReducer = hasReducer;
    }

    public void setHasSaga(boolean hasSaga) {
        this.hasSaga = hasSaga;
    }

    public void setHasAPI(boolean hasAPI) {
        this.hasAPI = hasAPI;
    }

    public void setParams(ArrayList<ActionParam> params) {
        this.params = params;
    }

    public Action(Process process, String name, boolean hasReducer, boolean hasSaga, boolean hasAPI, ArrayList<ActionParam> params) {
        this.process = process;
        this.name = new Constant("Action Name", name);
        this.hasReducer = hasReducer;
        this.hasSaga = hasSaga;
        this.hasAPI = hasAPI;
        this.params = params;
    }

    public Action(Process process, String name, boolean isResultAction, boolean hasReducer, boolean hasSaga, boolean hasAPI, ArrayList<ActionParam> params) {
        this(process, name, hasReducer, hasSaga, hasAPI, params);
        this.isResultAction = isResultAction;
    }

    private void createActionType() throws FileNotFoundException {
        String template = ConfigFile.fetchTemplate("action-type");
        if (template.equals("")) return;
        template = process.name.format(template);
        template = name.format(template);

        process.types.setContent(process.types.content.replaceAll("((?<=export\\senum\\sActionTypes\\s\\{\\n)(.|\\n)*?(?=}))", "$1\t" + template + "\n"));
    }

    private void createActionInterface() throws FileNotFoundException {
        String template = ConfigFile.fetchTemplate("action-interface");
        if (template.equals("")) return;
        template = process.name.format(template);
        template = name.format(template);

        StringBuilder actionParams = new StringBuilder();
        for (ActionParam actionParam : params) {
            actionParams.append("\n\t").append(actionParam.getName()).append(": ").append(actionParam.getType()).append(";");
        }
        template = template.replace("$BaseActionInterface", isResultAction ? "AppResultAction" : "AppAction");
        template = template.replace("$ActionParams", actionParams.toString().trim());

        process.types.setContent(process.types.content.replace("export type Action", template + "\n\n" + "export type Action"));
        createActionInterfaceLink();
    }

    private void createActionInterfaceLink() throws FileNotFoundException {
        String template = ConfigFile.fetchTemplate("action-interface-link");
        if (template.equals("")) return;
        template = process.name.format(template);
        template = name.format(template);
        template = template.replace("$ActionInterface", name.pascalCase + "Action");

        process.types.setContent(process.types.content.replaceAll("((?<=export\\stype\\sAction\\s=)(.|\\n)*?(?=;))", "$1\n\t" + template));
    }

    public void createActionTypes() throws IOException {
        process.createTypesFileIfNotExists();
        createActionType();
        if (params.size() > 0) createActionInterface();
        process.types.save();
    }

    public void createAction() throws IOException {
        process.createActionsFileIfNotExists();
        String template = ConfigFile.fetchTemplate("action");
        if (template.equals("")) return;
        template = process.name.format(template);
        template = name.format(template);

        String actionInterface = params.size() > 0 ? name.pascalCase + "Action" : isResultAction ? "AppResultAction" : "AppAction";
        template = template.replace("$ActionInterface", actionInterface);
        if (actionInterface.equals("AppResultAction")) template = "import { AppResultAction } from '';\n" + template;
        else if (actionInterface.equals("AppAction")) template = "import { AppAction } from '';\n" + template;
        else template = "import { " + name.pascalCase + "Action } from './types';\n" + template;

        StringBuilder paramsWithTypes = new StringBuilder(isResultAction ? "hasError: boolean, " : "");
        StringBuilder params = new StringBuilder(isResultAction ? "\n\thasError, " : "");
        for (ActionParam actionParam : this.params) {
            paramsWithTypes.append(actionParam.getName()).append(": ").append(actionParam.getType()).append(", ");
            params.append("\n\t").append(actionParam.getName()).append(", ");
        }
        template = template.replace("$ActionParamsWithTypes", paramsWithTypes.toString().trim());
        template = template.replace("$ActionParams", params.toString().trim());

        process.actions.setContent(process.actions.content.replaceAll("$", "\n" + template));
        process.actions.save();
    }

    public void createReducer() throws IOException {
        process.createReducersFileIfNotExists();
        String template = ConfigFile.fetchTemplate("reducer");
        if (template.equals("")) return;
        template = process.name.format(template);
        template = name.format(template);

        Pattern pattern = Pattern.compile("((import\\s)(.|\\n)*from.*?;)");
        Matcher matcher = pattern.matcher(template);
        while (matcher.find()) {
            process.reducers.insertAnImport(matcher.group());
        }
        template = template.substring(template.indexOf('\n') + 1);

        process.reducers.setContent(process.reducers.content.replaceAll("(default:\\n\\t{3}return\\sstate;)", template + "\n\t\t$1"));
        process.reducers.save();
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
            process.sagas.insertAnImport("import { " + name.pascalCase + "Action" + " } from './types';");
            template = template.replace("$ActionInterface", name.pascalCase + "Action");
            StringBuilder params = new StringBuilder();
            for (ActionParam param : this.params) {
                params.append(param.getName()).append(", ");
            }
            template = template.replace("$params", params.toString().trim());
        }
        if (hasAPI) template = createSagaRequest(template);

        process.sagas.setContent(process.sagas.content.replace("export default [", template + "\n\nexport default ["));

        createSagaLink();
        process.sagas.save();
    }

    private String createSagaRequest(String saga) throws FileNotFoundException {
        String template = ConfigFile.fetchTemplate("saga-request");
        if (template.equals("")) return saga;
        template = process.name.format(template);
        template = name.format(template);

        Pattern pattern = Pattern.compile("((import\\s)(.|\\n)*from.*?;)");
        Matcher matcher = pattern.matcher(template);
        while (matcher.find()) {
            process.sagas.insertAnImport(matcher.group());
        }
        template = template.substring(template.indexOf('\n') + 1);
        template = template.substring(template.indexOf('\n') + 1);

        return saga.replaceAll("(?<=try\\s\\{)(.|\\n)*?(?=})", "\n" + template + "\n\t");
    }

    private void createSagaLink() throws FileNotFoundException {
        String template = ConfigFile.fetchTemplate("saga-link");
        if (template.equals("")) return;
        template = process.name.format(template);
        template = name.format(template);

        Pattern pattern = Pattern.compile("((import\\s)(.|\\n)*from.*?;)");
        Matcher matcher = pattern.matcher(template);
        while (matcher.find()) {
            process.sagas.insertAnImport(matcher.group());
        }
        template = template.substring(template.indexOf('\n') + 1);
        template = template.substring(template.indexOf('\n') + 1);

        process.sagas.setContent(process.sagas.content.replaceAll("((?<=export\\sdefault\\s\\[)(.|\\n)*?(?=];))", "$1\n" + template));
    }

    public void createAPI() throws IOException {
        process.createAPIsFileIfNotExists();
        String template = ConfigFile.fetchTemplate("api");
        if (template.equals("")) return;
        template = process.name.format(template);
        template = name.format(template);

        process.apis.setContent(process.apis.content.replaceAll("$", "\n" + template));
        process.apis.save();
    }

    public void create() throws IOException {
        createActionTypes();
        createAction();
        if (hasReducer) createReducer();
        if (hasSaga) createSaga();
        if (hasAPI) createAPI();
    }
}
