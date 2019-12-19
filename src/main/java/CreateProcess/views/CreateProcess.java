package CreateProcess.views;

import CreateProcess.models.Process;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class CreateProcess extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField processesNames;

    private String rootPath;

    public CreateProcess() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Create New Process");
        getRootPane().setDefaultButton(buttonOK);

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
        String[] names = processesNames.getText().split("\\s*[,;]\\s*");
        for (String name : names) {
            try {
                new Process(rootPath, name).createIfNotExist(true, true, true, true, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public static void main(String rootPath) {
        CreateProcess dialog = new CreateProcess();
        dialog.setRootPath(rootPath);
        dialog.pack();
        dialog.setVisible(true);
    }
}
