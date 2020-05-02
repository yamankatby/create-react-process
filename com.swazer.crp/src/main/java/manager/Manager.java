package manager;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFileFactory;
import org.jetbrains.annotations.NotNull;

public class Manager implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        Language ts = Language.findLanguageByID("ts");
        assert ts != null;
        PsiFileFactory.getInstance(project).createFileFromText(ts, "import");
    }
}
