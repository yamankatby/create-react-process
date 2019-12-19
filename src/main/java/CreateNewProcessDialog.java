import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogPanel;
import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;

public class CreateNewProcessDialog {
    public String[] names;
    public boolean[] views;
    public boolean[] types;
    public boolean[] actions;
    public boolean[] reducers;
    public boolean[] sagas;

    public JPanel dialogPanel;
    public JTextField processName;

    private JComponent buildUI() {
        dialogPanel = new DialogPanel();

        processName = new JTextField();

        dialogPanel.add(processName);
        return dialogPanel;
    }


    public boolean show() {
        DialogBuilder dialogBuilder = new DialogBuilder();

        dialogBuilder.setTitle("Create new React Process");
        dialogBuilder.setCenterPanel(buildUI());

        return dialogBuilder.show() == DialogWrapper.OK_EXIT_CODE;
    }
}
