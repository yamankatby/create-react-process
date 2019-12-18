import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;

public class ProcessFile {

    private ProcessName processName;
    private String path;
    private String template;

    protected ProcessFile(ProcessName processName, String path, String template) {
        this.processName = processName;
        this.path = path;
        this.template = template;
    }

    private String formatTemplate(String template) {
        template = template.replace("$Process Name$", processName.raw);
        template = template.replace("$ProcessName$", processName.pascalCase);
        template = template.replace("$processName$", processName.camelCase);
        template = template.replace("$process_name$", processName.snakeCase);
        template = template.replace("$PROCESS_NAME$", processName.snakeAllCapsCase);
        template = template.replace("$process-name$", processName.kebabCase);
        return template;
    }

    public void createIfNotExist() throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        if (!file.createNewFile() || template == null) {
            return;
        }

        String templateContent;
        try (InputStream stream = getClass().getResourceAsStream("/Templates/" + template + ".txt")) {
            try (StringWriter writer = new StringWriter()) {
                IOUtils.copy(stream, writer, Charset.defaultCharset());
                templateContent = writer.toString();
            }
        }
        if (templateContent == null) {
            return;
        }
        templateContent = formatTemplate(templateContent);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(templateContent);
        }
    }
}
