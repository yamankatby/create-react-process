package ConfigFile.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Process {

    public UUID uuid;
    public String name;
    public ArrayList<Action> actions = new ArrayList<>();

    public Process(String content, ArrayList<Action> actions) {
        uuid = UUID.randomUUID();

        Pattern namePattern = Pattern.compile("(?<=^).*?(?=\\n)");
        Matcher nameMatcher = namePattern.matcher(content);
        if (nameMatcher.find()) {
            name = nameMatcher.group();
        }

        Pattern actionsPattern = Pattern.compile("(?<=-\\s).*?(?=\\n)");
        Matcher actionsMatcher = actionsPattern.matcher(content);
        while (actionsMatcher.find()) {
            this.actions.add(actions.stream().filter(a -> a.name.equals(actionsMatcher.group())).findFirst().orElse(null));
        }
    }

    public void execute(TheVariable theVariable) throws IOException {
        for (Action action : actions) {
            action.execute(theVariable);
        }
    }
}
