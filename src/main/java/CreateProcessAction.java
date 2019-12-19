import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class CreateProcessAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();

        CreateNewProcessDialog createNewProcessDialog = new CreateNewProcessDialog();

        boolean isOk = createNewProcessDialog.show();
        if (!isOk) return;

        String rootPath = ModuleRootManager.getInstance(ModuleManager.getInstance(project).getModules()[0]).getContentRoots()[0].getPath();
        String userInput = createNewProcessDialog.processName.getText();

        try {
            for (String processName : userInput.split(",")) {
                Process.createIfNotExist(rootPath, processName, true, true, true, true, true);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
