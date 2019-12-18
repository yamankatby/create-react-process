public class ProcessNamesManager {

    private String rootPath;
    private String processName;

    protected ProcessNamesManager(String rootPath, String processName) {
        this.rootPath = rootPath;
        this.processName = processName;
    }

    public String getProcessName() {
        return processName.replace(" ", "");
    }

    public String getProcessPath() {
        return rootPath.concat("/src/processes/" + getProcessName());
    }

    public String getLogicRootPath() {
        return getProcessPath().concat("/logic");
    }

    public String getViewsRootPath() {
        return getProcessPath().concat("/views");
    }

    public String getViewPath() {
        return getViewsRootPath().concat(String.format("/%s.tsx", getProcessName()));
    }

    public String getTypesPath() {
        return getLogicRootPath().concat("/types.ts");
    }

    public String getActionsPath() {
        return getLogicRootPath().concat("/actions.ts");
    }

    public String getReducersPath() {
        return getLogicRootPath().concat("/reducers.ts");
    }

    public String getSagasPath() {
        return getLogicRootPath().concat("/sagas.ts");
    }

    public String getApiPath() {
        return getLogicRootPath().concat("/apis.ts");
    }
}
