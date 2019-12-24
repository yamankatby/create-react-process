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

        Constant constant = new Constant("Process Name", processName);
        String formattedPath = constant.format(this.path);
        String formattedContent = constant.format(this.content);

        File file = new File(formattedPath);
        file.getParentFile().mkdirs();
        if (!file.createNewFile()) return;
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(formattedContent);
        }
    }
}
