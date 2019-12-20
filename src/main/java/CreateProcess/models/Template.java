package CreateProcess.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Template {
    public String name;
    public String path;
    public String content;

    protected Template(String templateContent) {
        Pattern namePattern = Pattern.compile("(?<=//\\s@crp-template\\s).+?(?=\\s)");
        Matcher nameMatcher = namePattern.matcher(templateContent);
        if (!nameMatcher.find()) {
            return;
        }
        this.name = nameMatcher.group();

        Pattern pathPattern = Pattern.compile("(?<=//\\s@crp-template\\s(name)\\s).*".replace("name", this.name));
        Matcher pathMatcher = pathPattern.matcher(templateContent);
        if (!pathMatcher.find()) {
            return;
        }
        this.path = pathMatcher.group();

        Pattern contentPattern = Pattern.compile("(?<=\\n)(.|\\n)*");
        Matcher contentMatcher = contentPattern.matcher(templateContent);
        if (!contentMatcher.find()) {
            return;
        }
        this.content = contentMatcher.group();
    }

    public void replace(String target, String replacement) {
        Variable key = new Variable(target);
        Variable value = new Variable(replacement);

        path = path.replace("$" + key.raw + "$", value.raw);
        path = path.replace("$" + key.pascalCase + "$", value.pascalCase);
        path = path.replace("$" + key.camelCase + "$", value.camelCase);
        path = path.replace("$" + key.snakeCase + "$", value.snakeCase);
        path = path.replace("$" + key.snakeAllCapsCase + "$", value.snakeAllCapsCase);
        path = path.replace("$" + key.kebabCase + "$", value.kebabCase);
        content = content.replace("$" + key.raw + "$", value.raw);
        content = content.replace("$" + key.pascalCase + "$", value.pascalCase);
        content = content.replace("$" + key.camelCase + "$", value.camelCase);
        content = content.replace("$" + key.snakeCase + "$", value.snakeCase);
        content = content.replace("$" + key.snakeAllCapsCase + "$", value.snakeAllCapsCase);
        content = content.replace("$" + key.kebabCase + "$", value.kebabCase);
    }
}
