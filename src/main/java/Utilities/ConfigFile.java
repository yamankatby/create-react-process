package Utilities;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ConfigFile {

    public static String rootPath;

    public enum BreakPoint {
        PROCESS_FILES,

        VIEW_FILE,
        TYPES_FILE,
        ACTIONS_FILE,
        REDUCERS_FILE,
        SAGAS_FILE,
        APIS_FILE,

        ACTION,
        ACTION_TYPE,
        ACTION_INTERFACE,
        ACTION_INTERFACE_LINK,
        REDUCER_CASE,
        SAGA,
        SAGA_LINK,
        API,
    }

    @NotNull
    @Contract(pure = true)
    public static String fetchConfigFile() throws FileNotFoundException {
        if (rootPath == null) return "";
        File configFile = new File(rootPath.concat("/.crp"));
        if (!configFile.exists()) return "";
        return new Scanner(configFile).useDelimiter("\\Z").next();
    }

    public static String fetchTemplate(BreakPoint breakPoint) throws FileNotFoundException {
        String breakPointName = "";

        if (breakPoint == BreakPoint.PROCESS_FILES) breakPointName = "process-files";

        else if (breakPoint == BreakPoint.VIEW_FILE) breakPointName = "view-file";
        else if (breakPoint == BreakPoint.TYPES_FILE) breakPointName = "types-file";
        else if (breakPoint == BreakPoint.ACTIONS_FILE) breakPointName = "actions-file";
        else if (breakPoint == BreakPoint.REDUCERS_FILE) breakPointName = "reducers-file";
        else if (breakPoint == BreakPoint.SAGAS_FILE) breakPointName = "sagas-file";
        else if (breakPoint == BreakPoint.APIS_FILE) breakPointName = "apis-file";

        else if (breakPoint == BreakPoint.ACTION) breakPointName = "action";
        else if (breakPoint == BreakPoint.ACTION_TYPE) breakPointName = "action-type";
        else if (breakPoint == BreakPoint.ACTION_INTERFACE) breakPointName = "action-interface";
        else if (breakPoint == BreakPoint.ACTION_INTERFACE_LINK) breakPointName = "action-interface-link";
        else if (breakPoint == BreakPoint.REDUCER_CASE) breakPointName = "reducer-case";
        else if (breakPoint == BreakPoint.SAGA) breakPointName = "saga";
        else if (breakPoint == BreakPoint.SAGA_LINK) breakPointName = "saga-link";
        else if (breakPoint == BreakPoint.API) breakPointName = "api";

        String configFileContent = fetchConfigFile();
        Pattern pattern = Pattern.compile("(?<=//\\s@crp-template-name\\n)(.|\\n)*?(?=(//\\s@crp-|$))".replace("template-name", breakPointName));
        Matcher matcher = pattern.matcher(configFileContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public static String fetchProcessFilesTemplate() throws FileNotFoundException {
        return fetchTemplate(BreakPoint.PROCESS_FILES);
    }

    public static String fetchViewFileTemplate() throws FileNotFoundException {
        return fetchTemplate(BreakPoint.VIEW_FILE);
    }

    public static String fetchTypesFileTemplate() throws FileNotFoundException {
        return fetchTemplate(BreakPoint.TYPES_FILE);
    }

    public static String fetchActionsFileTemplate() throws FileNotFoundException {
        return fetchTemplate(BreakPoint.ACTIONS_FILE);
    }

    public static String fetchReducersFileTemplate() throws FileNotFoundException {
        return fetchTemplate(BreakPoint.REDUCERS_FILE);
    }

    public static String fetchSagasFileTemplate() throws FileNotFoundException {
        return fetchTemplate(BreakPoint.SAGAS_FILE);
    }

    public static String fetchAPIsFileTemplate() throws FileNotFoundException {
        return fetchTemplate(BreakPoint.APIS_FILE);
    }

    public static String fetchActionTemplate() throws FileNotFoundException {
        return fetchTemplate(BreakPoint.ACTION);
    }

    public static String fetchActionTypeTemplate() throws FileNotFoundException {
        return fetchTemplate(BreakPoint.ACTION_TYPE);
    }

    public static String fetchActionInterfaceTemplate() throws FileNotFoundException {
        return fetchTemplate(BreakPoint.ACTION_INTERFACE);
    }

    public static String fetchActionInterfaceLinkTemplate() throws FileNotFoundException {
        return fetchTemplate(BreakPoint.ACTION_INTERFACE_LINK);
    }

    public static String fetchReducerCaseTemplate() throws FileNotFoundException {
        return fetchTemplate(BreakPoint.REDUCER_CASE);
    }

    public static String fetchSagaTemplate() throws FileNotFoundException {
        return fetchTemplate(BreakPoint.SAGA);
    }

    public static String fetchSagaLinkTemplate() throws FileNotFoundException {
        return fetchTemplate(BreakPoint.SAGA_LINK);
    }

    public static String fetchAPITemplate() throws FileNotFoundException {
        return fetchTemplate(BreakPoint.API);
    }
}
