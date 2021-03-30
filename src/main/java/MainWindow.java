import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public JButton sendButton;
    public JTextArea logTextArea;
    public JTextField messageTextField;
    private JScrollPane logScrollPane;

    public MainWindow() {
        super("Main Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(50, 50);
        setLayout(new GridBagLayout());

        createUIElements();

        setVisible(true);
        pack();
    }

    private JTextArea createLogTextArea() {
        logTextArea = new JTextArea("");
        logTextArea.setEditable(false);
        logTextArea.setSize(new Dimension(300, 400));
        logTextArea.setLineWrap(true);
        logTextArea.setMargin(new Insets(5, 5, 5, 5));
        return logTextArea;
    }

    private JScrollPane logScrollPane() {
        JScrollPane logScrollPane = new JScrollPane(logTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        logScrollPane.setPreferredSize(new Dimension(300, 400));
        return logScrollPane;
    }

    private void createUIElements() {
        GridBagConstraints layoutConstraints = new GridBagConstraints();

        logTextArea = createLogTextArea();
        logScrollPane = logScrollPane();
        Insets insets = new Insets(5, 5, 0, 5);
        addObjects(logScrollPane, layoutConstraints, GridBagConstraints.BOTH,
                0, 0, 2,
                0, 1, insets);

        insets = new Insets(5, 5, 5, 5);

        messageTextField = new JTextField();
        addObjects(messageTextField, layoutConstraints, GridBagConstraints.HORIZONTAL,
                0, 1, 1,
                0.9, 0.0, insets);

        sendButton = new JButton("Send");
        addObjects(sendButton, layoutConstraints, GridBagConstraints.NONE,
                1, 1, 1,
                0.05, 0.0, insets);
    }

    private void addObjects(Component component,
                            GridBagConstraints layoutConstraints,
                            int fill,
                            int gridX,
                            int gridY,
                            int gridWidth,
                            double weightX,
                            double weightY,
                            Insets insets) {
        layoutConstraints.fill = fill;
        layoutConstraints.gridx = gridX;
        layoutConstraints.gridy = gridY;
        layoutConstraints.gridwidth = gridWidth;
        layoutConstraints.weightx = weightX;
        layoutConstraints.weighty = weightY;
        layoutConstraints.insets = insets;
        add(component, layoutConstraints);
    }

    public void printText(String text) {
        String currentText = logTextArea.getText() + text + '\n';
        logTextArea.setText(currentText);
    }
}
