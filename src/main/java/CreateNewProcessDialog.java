import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogPanel;
import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;

public class CreateNewProcessDialog {
    public JPanel jDialogPanel;
    public JTextField jProcessName;

    private JComponent buildUI() {
        jDialogPanel = new DialogPanel();
        jProcessName = new JTextField();

        jDialogPanel.add(jProcessName);
        return jDialogPanel;
    }


    public boolean show() {
        DialogBuilder dialogBuilder = new DialogBuilder();

        dialogBuilder.setTitle("Create new React Process");
        dialogBuilder.setCenterPanel(buildUI());

        return dialogBuilder.show() == DialogWrapper.OK_EXIT_CODE;
    }
}
