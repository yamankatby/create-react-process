package Action.views;

import Action.models.Action;
import Action.models.ActionParam;
import Process.models.Process;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class CreateNewAction extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<Process> processComboBox;
    private JTextField nameTextField;
    private JCheckBox resultActionCheckBox;
    private JCheckBox reducerCheckBox;
    private JCheckBox sagaCheckBox;
    private JCheckBox APICheckBox;
    private JPanel resultOptionsPane;
    private JCheckBox resultReducerCheckBox;
    private JCheckBox resultSagaCheckBox;
    private JCheckBox resultAPICheckBox;
    private JPanel paramsPane;
    private JPanel resultParamsPane;
    private JButton addParamButton;
    private JButton addResultParamButton;
    private JPanel resultParamsContainerPane;

    private ArrayList<ActionParam> actionParams = new ArrayList<>();
    private ArrayList<ActionParam> resultParams = new ArrayList<>();

    public CreateNewAction() {
        setTitle("Create New Action");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        resultActionCheckBox.addChangeListener(e -> onResultActionChange());

        Process[] processes = Process.fetchProcesses();
        for (Process process : processes) processComboBox.addItem(process);

        resultOptionsPane.setVisible(false);
        resultParamsContainerPane.setVisible(false);

        addParamButton.addActionListener(e -> onAddParam());
        addResultParamButton.addActionListener(e -> onAddResultParam());

        GridLayout gridLayout = new GridLayout(0, 1);
        paramsPane.setLayout(gridLayout);
        resultParamsPane.setLayout(gridLayout);

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
            Action action = new Action(selectedProcess, nameTextField.getText());
            action.setHasReducer(reducerCheckBox.isSelected());
            action.setHasSaga(sagaCheckBox.isSelected());
            action.setHasAPI(APICheckBox.isSelected());
            action.setParams(actionParams);
            action.create();
            if (resultActionCheckBox.isSelected()) {
                Action resultAction = new Action(selectedProcess, nameTextField.getText());
                resultAction.setResultAction(true);
                resultAction.setHasReducer(resultReducerCheckBox.isSelected());
                resultAction.setHasSaga(resultSagaCheckBox.isSelected());
                resultAction.setHasAPI(resultAPICheckBox.isSelected());
                resultAction.setParams(resultParams);
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

    private void onResultActionChange() {
        resultOptionsPane.setVisible(resultActionCheckBox.isSelected());
        resultParamsContainerPane.setVisible(resultActionCheckBox.isSelected());
    }

    private void onAddParam() {
        ActionParamObject actionParamObject = new ActionParamObject();
        paramsPane.add(actionParamObject.getComponent());
        paramsPane.revalidate();
        actionParams.add(new ActionParam(actionParamObject.getNameTextField(), actionParamObject.getTypeTextField()));
    }

    private void onAddResultParam() {
        ActionParamObject actionParamObject = new ActionParamObject();
        resultParamsPane.add(actionParamObject.getComponent());
        resultParamsPane.revalidate();
        resultParams.add(new ActionParam(actionParamObject.getNameTextField(), actionParamObject.getTypeTextField()));
    }

    public static void main() {
        CreateNewAction dialog = new CreateNewAction();
        dialog.pack();
        dialog.setVisible(true);
    }
}
