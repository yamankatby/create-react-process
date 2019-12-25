package Action.models;

import Process.models.Process;
import Utilities.ConfigFile;
import Utilities.Constant;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Action {

    private String processName;
    private String actionName;

    private boolean hasResultAction;
    private boolean hasReducer;
    private boolean hasSaga;
    private boolean hasAPI;

    private ArrayList<ActionParam> params;
    private ArrayList<ActionParam> resultActionParams;

    private Process process;
    private Constant processNameConstant;
    private Constant actionNameConstant;
    private Constant resultActionNameConstant;

    public Action(String processName, String actionName, boolean hasResultAction, boolean hasReducer, boolean hasSaga, boolean hasAPI, ArrayList<ActionParam> params, ArrayList<ActionParam> resultActionParams) {
        this.processName = processName;
        this.actionName = actionName;
        this.hasResultAction = hasResultAction;
        this.hasReducer = hasReducer;
        this.hasSaga = hasSaga;
        this.hasAPI = hasAPI;
        this.params = params;
        this.resultActionParams = resultActionParams;

        this.process = new Process(processName);
        this.processNameConstant = new Constant("Process Name", processName);
        this.actionNameConstant = new Constant("Action Name", actionName);
        this.resultActionNameConstant = new Constant("Action Name", actionName + " Result");
    }

    public void createActionTypes() throws IOException {
        process.createTypesFile();
        String actionTypeTemplate = ConfigFile.fetchTemplate(ConfigFile.BreakPoint.ACTION_TYPE);
        String actionInterfaceTemplate = ConfigFile.fetchTemplate(ConfigFile.BreakPoint.ACTION_INTERFACE);
        String actionInterfaceLinkTemplate = ConfigFile.fetchTemplate(ConfigFile.BreakPoint.ACTION_INTERFACE_LINK);

        File file = new File(process.typesPath);
        String fileContent = new Scanner(file).useDelimiter("\\Z").next();

        fileContent = fileContent.replace("// @-next-type", actionTypeTemplate.concat("// @-next-type"));
        fileContent = fileContent.replace("// @-next-interface", actionInterfaceTemplate.concat("// @-next-interface"));
        fileContent = fileContent.replace("// @-next-link", actionInterfaceLinkTemplate.concat("// @-next-link"));

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(fileContent);
        }
    }

    public void createResultActionTypes() throws IOException {

    }

    public void createAction() throws IOException {
        createActionTypes();
    }

    public void createResultAction() throws IOException {

    }

    public void createReducer() throws IOException {

    }

    public void createSaga() throws IOException {

    }

    public void createAPI() throws IOException {

    }

    public void execute() throws IOException {
        createAction();
        if (hasResultAction) createResultAction();
        if (hasReducer) createReducer();
        if (hasSaga) createSaga();
        if (hasAPI) createAPI();
    }
}
