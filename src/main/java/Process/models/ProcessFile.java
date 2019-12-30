package Process.models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessFile {

    public String path;
    public String content;

    private File file;

    public ProcessFile(String path) {
        this.path = path;
        file = new File(path);
    }

    public void createIfNotExists(String template) throws IOException {
        file.getParentFile().mkdirs();
        if (!file.createNewFile()) {
            try {
                content = new Scanner(file).useDelimiter("\\Z").next();
            } catch (Exception e) {
                content = "";
            }
            return;
        }
        if (template == null || template.equals("")) {
            content = "";
            return;
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(template);
        }
        content = template;
    }

    public void addImport(String anImport) {
        Pattern pattern = Pattern.compile("import\\s(.|\\n)*from\\s.*?;");
    }

    public void reflect() throws IOException {
        Pattern pattern = Pattern.compile("import\\s.*?;");
        Matcher matcher = pattern.matcher(content);

        ArrayList<String> imports = new ArrayList<>();
        while (matcher.find()) {
            imports.add(matcher.group());
        }

        content = content.replaceAll(pattern.pattern(), "");
        for (String anImport : imports) {
            content = content.replaceAll("$", "\n" + anImport);
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(content);
        }
    }
}
