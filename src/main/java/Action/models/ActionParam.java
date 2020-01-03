package Action.models;

import javax.swing.*;

public class ActionParam {

    private String name;
    private String type;

    private JTextField nameField;
    private JTextField typesField;

    public ActionParam(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public ActionParam(JTextField name, JTextField type) {
        nameField = name;
        typesField = type;
    }

    public String getName() {
        if (nameField != null) return nameField.getText();
        else return name;
    }

    public String getType() {
        if (typesField != null) return typesField.getText();
        else return type;
    }
}
