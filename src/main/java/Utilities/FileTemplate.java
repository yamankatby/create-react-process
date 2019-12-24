package Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileTemplate {

    public String name;
    public String path;
    public String content;

    public FileTemplate(String fileTemplateContent) {
        Pattern templateNamePattern = Pattern.compile("(?<=^).*?(?=\\s)");
        Matcher templateNameMatcher = templateNamePattern.matcher(fileTemplateContent);
        if (templateNameMatcher.find()) {
            name = templateNameMatcher.group();
        }
        Pattern templatePathPattern = Pattern.compile("(?<=\\$rootPath\\$).*?(?=\\n)");
        Matcher templatePathMatcher = templatePathPattern.matcher(fileTemplateContent);
        if (templatePathMatcher.find()) {
            path = ConfigFile.rootPath.concat(templatePathMatcher.group());
        }
        Pattern templateContentPattern = Pattern.compile("(?<=\\n)(.|\\n)*?(?=$)");
        Matcher templateContentMatcher = templateContentPattern.matcher(fileTemplateContent);
        if (templateContentMatcher.find()) {
            content = templateContentMatcher.group();
        }
    }

    public void createFile(String processName) throws IOException {
        if (name == null || path == null || content == null) return;
        Constant constant = new Constant(processName);

        String formattedPath = this.path;
        formattedPath = formattedPath.replace("$Process Name$", constant.raw);
        formattedPath = formattedPath.replace("$ProcessName$", constant.pascalCase);
        formattedPath = formattedPath.replace("$processName$", constant.camelCase);
        formattedPath = formattedPath.replace("$process_name$", constant.snakeCase);
        formattedPath = formattedPath.replace("$PROCESS_NAME$", constant.snakeAllCapsCase);
        formattedPath = formattedPath.replace("$process-name$", constant.kebabCase);

        String formattedContent = this.content;
        formattedContent = formattedContent.replace("$Process Name$", constant.raw);
        formattedContent = formattedContent.replace("$ProcessName$", constant.pascalCase);
        formattedContent = formattedContent.replace("$processName$", constant.camelCase);
        formattedContent = formattedContent.replace("$process_name$", constant.snakeCase);
        formattedContent = formattedContent.replace("$PROCESS_NAME$", constant.snakeAllCapsCase);
        formattedContent = formattedContent.replace("$process-name$", constant.kebabCase);

        File file = new File(formattedPath);
        file.getParentFile().mkdirs();
        if (!file.createNewFile()) return;
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(formattedContent);
        }
    }
}
