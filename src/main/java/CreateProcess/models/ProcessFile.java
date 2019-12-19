package CreateProcess.models;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;

public class ProcessFile {

    public static void createIfNotExist(ProcessName processName, String path, String template) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();

        if (!file.createNewFile() || template == null) {
            return;
        }

        String templateContent;
        try (InputStream stream = ProcessFile.class.getResourceAsStream("/Templates/" + template + ".txt")) {
            try (StringWriter writer = new StringWriter()) {
                IOUtils.copy(stream, writer, Charset.defaultCharset());
                templateContent = writer.toString();
            }
        }
        if (templateContent == null) {
            return;
        }

        templateContent = templateContent.replace("$Process Name$", processName.raw);
        templateContent = templateContent.replace("$ProcessName$", processName.pascalCase);
        templateContent = templateContent.replace("$processName$", processName.camelCase);
        templateContent = templateContent.replace("$process_name$", processName.snakeCase);
        templateContent = templateContent.replace("$PROCESS_NAME$", processName.snakeAllCapsCase);
        templateContent = templateContent.replace("$process-name$", processName.kebabCase);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(templateContent);
        }
    }
}
