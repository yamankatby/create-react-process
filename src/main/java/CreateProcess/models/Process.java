package CreateProcess.models;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;

public class Process {

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

    public Process(@NotNull String rootPath, @NotNull String processName) {
        this.rootPath = rootPath;
        this.processName = processName;

        raw = WordUtils.capitalize(processName.replaceAll("\\s+", " ").replace(".", " ").replace("-", " ").replace("_", " ").toLowerCase());
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

    public void createIfNotExist(boolean view, boolean types, boolean actions, boolean reducers, boolean sagas) throws IOException {
        if (view) createFileIfNotExist(viewPath, "view");
        if (types) createFileIfNotExist(typesPath, "types");
        if (actions) createFileIfNotExist(actionsPath, "actions");
        if (reducers) createFileIfNotExist(reducersPath, "reducers");
        if (sagas) createFileIfNotExist(sagasPath, "sagas");
    }

    public void createFileIfNotExist(String path, String template) throws IOException {
        File file = new File(path);
        boolean directoryCreated = file.getParentFile().mkdirs();
        if (!file.createNewFile() || template == null) {
            return;
        }

        String templateContent;
        try (InputStream stream = getClass().getResourceAsStream("/Templates/" + template + ".txt")) {
            try (StringWriter writer = new StringWriter()) {
                IOUtils.copy(stream, writer, Charset.defaultCharset());
                templateContent = writer.toString();
            }
        }
        if (templateContent == null) {
            return;
        }

        templateContent = templateContent.replace("$Process Name$", raw);
        templateContent = templateContent.replace("$ProcessName$", pascalCase);
        templateContent = templateContent.replace("$processName$", camelCase);
        templateContent = templateContent.replace("$process_name$", snakeCase);
        templateContent = templateContent.replace("$PROCESS_NAME$", snakeAllCapsCase);
        templateContent = templateContent.replace("$process-name$", kebabCase);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(templateContent);
        }
    }
}
