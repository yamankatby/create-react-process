package ConfigFile.models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Action {

    public UUID uuid;
    public String name;
    public String path;
    public String anchor;
    public String content;

    public Action(String content, String rootPath) {
        uuid = UUID.randomUUID();
        Pattern namePattern = Pattern.compile("(?<=^).*?(?=\\s)");
        Matcher nameMatcher = namePattern.matcher(content);
        if (nameMatcher.find()) {
            name = nameMatcher.group();
        }
        Pattern pathPattern = Pattern.compile("(?<=\\$rootPath\\$).*?(?=\\s)");
        Matcher pathMatcher = pathPattern.matcher(content);
        if (pathMatcher.find()) {
            path = rootPath.concat(pathMatcher.group());
        }
        Pattern anchorPattern = Pattern.compile("(?<=\\$-).*?(?=(\\n|\\s))");
        Matcher anchorMatcher = anchorPattern.matcher(content);
        if (anchorMatcher.find()) {
            anchor = anchorMatcher.group();
        }
        Pattern contentPattern = Pattern.compile("(?<=\\n)(.|\\n)*");
        Matcher contentMatcher = contentPattern.matcher(content);
        if (contentMatcher.find()) {
            this.content = contentMatcher.group();
        }
    }

    public void execute(TheVariable theVariable) throws IOException {
        if (name == null || path == null || content == null) return;

        String formattedPath = this.path;
        formattedPath = formattedPath.replace("$Process Name$", theVariable.raw);
        formattedPath = formattedPath.replace("$ProcessName$", theVariable.pascalCase);
        formattedPath = formattedPath.replace("$processName$", theVariable.camelCase);
        formattedPath = formattedPath.replace("$process_name$", theVariable.snakeCase);
        formattedPath = formattedPath.replace("$PROCESS_NAME$", theVariable.snakeAllCapsCase);
        formattedPath = formattedPath.replace("$process-name$", theVariable.kebabCase);

        String formattedContent = this.content;
        formattedContent = formattedContent.replace("$Process Name$", theVariable.raw);
        formattedContent = formattedContent.replace("$ProcessName$", theVariable.pascalCase);
        formattedContent = formattedContent.replace("$processName$", theVariable.camelCase);
        formattedContent = formattedContent.replace("$process_name$", theVariable.snakeCase);
        formattedContent = formattedContent.replace("$PROCESS_NAME$", theVariable.snakeAllCapsCase);
        formattedContent = formattedContent.replace("$process-name$", theVariable.kebabCase);

        if (anchor == null) {
            executeCreationAction(formattedPath, formattedContent);
        } else {
            executeReplacementAction(formattedPath, formattedContent);
        }
    }

    private void executeCreationAction(String path, String content) throws IOException {
        File file = new File(path);
        boolean mkdirs = file.getParentFile().mkdirs();
        if (!file.createNewFile() || content == null) {
            return;
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(content);
        }
    }

    private void executeReplacementAction(String path, String content) {

    }
}
