package CreateProcess.models;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplatesManager {
    public Dictionary<String, Template> templates = new Hashtable<>();

    protected TemplatesManager() throws IOException {
        String fileContent;
        try (InputStream stream = getClass().getResourceAsStream("/templates.txt")) {
            try (StringWriter writer = new StringWriter()) {
                IOUtils.copy(stream, writer, Charset.defaultCharset());
                fileContent = writer.toString();
            }
        }
        if (fileContent == null) {
            return;
        }

        Pattern pattern = Pattern.compile("(//\\s@crp-template\\s\\w*\\s.*)(.|\\n)*?(?=//\\s@crp-(template|end))");
        Matcher matcher = pattern.matcher(fileContent);

        while (matcher.find()) {
            Template template = new Template(matcher.group());
            templates.put(template.name, template);
        }
    }
}
