package Action.views;

import Action.models.Action;
import Process.models.Process;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.*;
import java.io.IOException;

public class CreateNewAction extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<Process> processComboBox;
    private JTextField actionNameTextField;
    private JCheckBox resultActionCheckBox;
    private JCheckBox reducerCheckBox;
    private JCheckBox sagaCheckBox;
    private JCheckBox APICheckBox;
    private JPanel resultActionOptions;
    private JCheckBox resultReducerCheckBox;
    private JCheckBox resultSagaCheckBox;
    private JCheckBox resultAPICheckBox;

    public CreateNewAction() {
        setTitle("Create New Action");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        resultActionCheckBox.addChangeListener(this::onResultActionChange);

        Process[] processes = Process.fetchProcesses();
        for (Process process : processes) processComboBox.addItem(process);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        try {
            Process selectedProcess = ((Process) processComboBox.getSelectedItem());
            Action action = new Action(selectedProcess, actionNameTextField.getText());
            action.setHasReducer(reducerCheckBox.isSelected());
            action.setHasSaga(sagaCheckBox.isSelected());
            action.setHasAPI(APICheckBox.isSelected());
            action.create();
            if (resultActionCheckBox.isSelected()) {
                Action resultAction = new Action(selectedProcess, actionNameTextField.getText());
                resultAction.setResultAction(true);
                resultAction.setHasReducer(resultReducerCheckBox.isSelected());
                resultAction.setHasSaga(resultSagaCheckBox.isSelected());
                resultAction.setHasAPI(resultAPICheckBox.isSelected());
                resultAction.create();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void onResultActionChange(@NotNull ChangeEvent e) {
        JCheckBox source = (JCheckBox) e.getSource();
        boolean selected = source.isSelected();
        resultActionOptions.setVisible(selected);
    }

    public static void main() {
        CreateNewAction dialog = new CreateNewAction();
        dialog.pack();
        dialog.setVisible(true);
    }
}
