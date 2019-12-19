package CreateProcess.models;

import org.apache.commons.lang.WordUtils;
import org.jetbrains.annotations.NotNull;

public class ProcessName {

    public String rootPath;
    public String processName;

    public String raw;
    public String pascalCase;
    public String camelCase;
    public String snakeCase;
    public String snakeAllCapsCase;
    public String kebabCase;

    public String processRootPath;
    public String viewsRootPath;
    public String logicRootPath;

    public String viewPath;
    public String typesPath;
    public String actionsPath;
    public String reducersPath;
    public String sagasPath;

    protected ProcessName(@NotNull String rootPath, @NotNull String processName) {
        this.rootPath = rootPath;
        this.processName = processName;

        raw = WordUtils.capitalize(processName.replace(".", " ").replace("-", " ").replace("_", " ").toLowerCase());
        pascalCase = raw.replace(" ", "");
        camelCase = Character.toLowerCase(pascalCase.charAt(0)) + pascalCase.substring(1);
        snakeCase = raw.toLowerCase().replace(" ", "_");
        snakeAllCapsCase = snakeCase.toUpperCase();
        kebabCase = raw.toLowerCase().replace(" ", "-");

        processRootPath = rootPath.concat("/src/processes/").concat(this.camelCase);
        viewsRootPath = processRootPath.concat("/views");
        logicRootPath = processRootPath.concat("/logic");

        viewPath = viewsRootPath.concat(String.format("/%s.tsx", pascalCase));
        typesPath = logicRootPath.concat("/types.ts");
        actionsPath = logicRootPath.concat("/actions.ts");
        reducersPath = logicRootPath.concat("/reducers.ts");
        sagasPath = logicRootPath.concat("/sagas.ts");
    }
}
