package Process.views;

import Action.models.Action;
import Process.models.Process;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class CreateNewProcess extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField processesNames;

    public CreateNewProcess() {
        setTitle("Create New Process");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

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
        // add your code here
        if (processesNames.getText().equals("")) {
            processesNames.requestFocus();
            return;
        }

        String[] names = processesNames.getText().split("\\s*[,;]\\s*");
        for (String name : names) {
            try {
                Process process = Process.createProcess(name);
                Action action = new Action(process, "clear " + name);
                action.setHasReducer(true);
                action.create();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main() {
        CreateNewProcess dialog = new CreateNewProcess();
        dialog.pack();
        dialog.setVisible(true);
    }
}
