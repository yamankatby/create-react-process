package Action.views;

import javax.swing.*;
import java.awt.*;

public class ActionParamObject {
    private JPanel contentPane;
    private JTextField nameTextField;
    private JTextField typeTextField;

    public Component getComponent() {
        return contentPane;
    }

    public JTextField getNameTextField() {
        return nameTextField;
    }

    public JTextField getTypeTextField() {
        return typeTextField;
    }
}
