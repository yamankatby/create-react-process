package CreateProcess.views;

import CreateProcess.models.Process;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class CreateProcess extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField processesNames;

    private String rootPath;

    private String[] names;
    private ArrayList<Boolean> views = new ArrayList<>();
    private ArrayList<Boolean> types = new ArrayList<>();
    private ArrayList<Boolean> actions = new ArrayList<>();
    private ArrayList<Boolean> reducers = new ArrayList<>();
    private ArrayList<Boolean> sagas = new ArrayList<>();

    public CreateProcess() {
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

        processesNames.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onProcessesNames();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onProcessesNames();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onProcessesNames();
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
        for (int i = 0; i < names.length; i++) {
            try {
                Process.createIfNotExist(rootPath, names[i], views.get(i), types.get(i), actions.get(i), reducers.get(i), sagas.get(i));
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

    private void onProcessesNames() {
        String text = processesNames.getText();
        names = text.split("\\s*[,;]\\s*");

        int loops = names.length - views.size();
        for (int i = 0; i < Math.abs(loops); i++) {
            if (loops < 0) {
                views.remove(views.size() - 1);
                types.remove(types.size() - 1);
                actions.remove(actions.size() - 1);
                reducers.remove(reducers.size() - 1);
                sagas.remove(sagas.size() - 1);
            } else {
                views.add(true);
                types.add(true);
                actions.add(true);
                reducers.add(true);
                sagas.add(true);
            }
        }
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public static void main(String rootPath) {
        CreateProcess dialog = new CreateProcess();
        dialog.setRootPath(rootPath);
        dialog.pack();
        dialog.setVisible(true);
        // System.exit(0);
    }
}
