package Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ConfigFile {

    public static String rootPath;

    public static String fetchConfigFile() throws FileNotFoundException {
        if (rootPath == null) return "";
        File configFile = new File(rootPath.concat("/.crp"));
        if (!configFile.exists()) return "";
        return new Scanner(configFile).useDelimiter("\\Z").next();
    }

    public static String fetchTemplate(String name) throws FileNotFoundException {
        String configFileContent = fetchConfigFile();
        Pattern pattern = Pattern.compile("(?<=//\\s@crp-name\\n)(.|\\n)*?(?=\\n*(//\\s@crp-|$))".replace("name", name));
        Matcher matcher = pattern.matcher(configFileContent);
        if (matcher.find()) return matcher.group();
        return "";
    }
}
