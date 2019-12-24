package Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ConfigFile {

    public static String rootPath;

    public static ArrayList<String> processFilesNames = new ArrayList<>();
    public static ArrayList<FileTemplate> fileTemplates = new ArrayList<>();

    public static void reFetchConfig() throws FileNotFoundException {
        if (rootPath == null) return;
        processFilesNames = new ArrayList<>();
        fileTemplates = new ArrayList<>();

        File configFile = new File(rootPath.concat("/.crp"));
        if (!configFile.exists()) return;
        String configContent = new Scanner(configFile).useDelimiter("\\Z").next();

        Pattern processInfoContentPattern = Pattern.compile("(?<=//\\s@crp-process-files)(.|\\n)*?(?=(//\\s@crp-|$))");
        Matcher processInfoContentMatcher = processInfoContentPattern.matcher(configContent);
        if (processInfoContentMatcher.find()) {
            String processInfoContent = processInfoContentMatcher.group();

            Pattern processFilesNamesPattern = Pattern.compile("(?<=-\\s).*?(?=\\n)");
            Matcher processFilesNamesMatcher = processFilesNamesPattern.matcher(processInfoContent);
            while (processFilesNamesMatcher.find()) {
                processFilesNames.add(processFilesNamesMatcher.group());
            }
        }

        Pattern fileTemplatesPattern = Pattern.compile("(?<=//\\s@crp-file-template\\s)(.|\\n)*?(?=(//\\s@crp-|$))");
        Matcher fileTemplatesMatcher = fileTemplatesPattern.matcher(configContent);
        while (fileTemplatesMatcher.find()) {
            fileTemplates.add(new FileTemplate(fileTemplatesMatcher.group()));
        }
    }

    public static void createProcess(String processName) throws IOException {
        for (String name : processFilesNames) {
            FileTemplate fileTemplate = fileTemplates.stream().filter(template -> template.name.equals(name)).findFirst().orElse(null);
            if (fileTemplate != null) {
                fileTemplate.createFile(processName);
            }
        }
    }
}
