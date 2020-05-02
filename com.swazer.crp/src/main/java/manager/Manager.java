package manager;

import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import java.io.File;

public class Manager {
    private JPanel contentPane;

    private String[] directories;

    public JPanel getContentPane() {
        return contentPane;
    }

    public Manager(Project project, ToolWindow toolWindow) {
        String rootPath = ModuleRootManager.getInstance(ModuleManager.getInstance(project).getModules()[0]).getContentRoots()[0].getPath();
        File file = new File(rootPath.concat("/src/processes/"));
        String[] _directories = file.list((dir, name) -> new File(dir, name).isDirectory());
    }
}
