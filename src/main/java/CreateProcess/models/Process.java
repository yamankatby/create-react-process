package CreateProcess.models;

import java.io.IOException;

public class Process {

    public static void createIfNotExist(String rootPath, String name, boolean view, boolean types, boolean actions, boolean reducers, boolean sagas) throws IOException {
        ProcessName processName = new ProcessName(rootPath, name);
        if (view) ProcessFile.createIfNotExist(processName, processName.viewPath, "view");
        if (types) ProcessFile.createIfNotExist(processName, processName.typesPath, "types");
        if (actions) ProcessFile.createIfNotExist(processName, processName.actionsPath, "actions");
        if (reducers) ProcessFile.createIfNotExist(processName, processName.reducersPath, "reducers");
        if (sagas) ProcessFile.createIfNotExist(processName, processName.sagasPath, "sagas");
    }
}
