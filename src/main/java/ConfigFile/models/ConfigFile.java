package ConfigFile.models;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigFile {

    public ArrayList<Process> processes = new ArrayList<>();
    public ArrayList<Action> actions = new ArrayList<>();

    public ConfigFile(@NotNull String rootPath) throws IOException {
        File configFile = new File(rootPath.concat("/.crp"));
        if (!configFile.exists()) {
            return;
        }

        String configContent = new Scanner(configFile).useDelimiter("\\Z").next();

        Pattern actionsPattern = Pattern.compile("(?<=//\\s@crp-action\\s)(.|\\n)*?(?=($|//\\s@crp-(process|action)))");
        Matcher actionsMatcher = actionsPattern.matcher(configContent);
        while (actionsMatcher.find()) {
            actions.add(new Action(actionsMatcher.group(), rootPath));
        }

        Pattern processesPattern = Pattern.compile("(?<=//\\s@crp-process\\s)(.|\\n)*?(?=($|//\\s@crp-(process|action)))");
        Matcher processesMatcher = processesPattern.matcher(configContent);
        while (processesMatcher.find()) {
            processes.add(new Process(processesMatcher.group(), actions));
        }
    }

    public Process getProcess(String name) {
        Optional<Process> process = processes.stream().filter(p -> p.name.equals(name)).findFirst();
        return process.orElse(null);
    }
}
