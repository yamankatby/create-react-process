import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;

public class Process {

    private String path;
    private String name;

    protected Process(String path, String name) {
        this.path = path;
        this.name = name;
    }

    private void createFromTemplate(String templateName, String path) {
        try {
            String content;
            try (InputStream stream = getClass().getResourceAsStream(templateName)) {
                try (StringWriter writer = new StringWriter()) {
                    IOUtils.copy(stream, writer, Charset.defaultCharset());
                    content = writer.toString();
                }
            }
            File view = new File(path);

            if (content == null || !view.createNewFile()) {
                return;
            }

            content = content.replace("$ViewName$", path);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(view))) {
                bw.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void create() throws IOException {
        ProcessNamesManager processNamesManager = new ProcessNamesManager(path, name);
        boolean logicRoot = new File(processNamesManager.getLogicRootPath()).mkdirs();
        boolean viewsRoot = new File(processNamesManager.getViewsRootPath()).mkdirs();

        createFromTemplate("/TemplateProcess/views/View.txt", processNamesManager.getViewPath());
        createFromTemplate("/TemplateProcess/logic/types.txt", processNamesManager.getTypesPath());
        createFromTemplate("/TemplateProcess/logic/actions.txt", processNamesManager.getActionsPath());
        createFromTemplate("/TemplateProcess/logic/reducers.txt", processNamesManager.getReducersPath());
        createFromTemplate("/TemplateProcess/logic/sagas.txt", processNamesManager.getSagasPath());
        createFromTemplate("/TemplateProcess/logic/apis.txt", processNamesManager.getApiPath());
    }
}
