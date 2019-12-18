import java.io.IOException;

public class Process {

    private String path;
    private String name;

    protected Process(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public void create() throws IOException {
        ProcessName processName = new ProcessName(path, name);
        new ProcessFile(processName, processName.viewPath, "view").createIfNotExist();
        new ProcessFile(processName, processName.typesPath, "types").createIfNotExist();
        new ProcessFile(processName, processName.actionsPath, "actions").createIfNotExist();
        new ProcessFile(processName, processName.reducersPath, "reducers").createIfNotExist();
        new ProcessFile(processName, processName.sagasPath, "sagas").createIfNotExist();
    }
}
