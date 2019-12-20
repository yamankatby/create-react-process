package CreateProcess.views;

import CreateProcess.models.ConfigFile;
import CreateProcess.models.Process;
import CreateProcess.models.TheVariable;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class CreateProcess extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField processesNames;
    private JComboBox<Process> processes;

    private ConfigFile configFile;

    public CreateProcess(String rootPath) {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Create New Process");
        getRootPane().setDefaultButton(buttonOK);

        try {
            configFile = new ConfigFile(rootPath);
            configFile.processes.forEach(process -> {
                processes.addItem(process);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

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
        String processesNamesText = processesNames.getText();
        if (processesNamesText.equals("")) {
            processesNames.requestFocus();
            return;
        }

        Process selectedProcess = (Process) processes.getSelectedItem();
        String[] formattedNames = processesNamesText.split("\\s*[,;]\\s*");
        try {
            for (String name : formattedNames) {
                assert selectedProcess != null;
                configFile.getProcess(selectedProcess.name).execute(new TheVariable(name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String rootPath) {
        CreateProcess dialog = new CreateProcess(rootPath);
        dialog.pack();
        dialog.setVisible(true);
    }
}
