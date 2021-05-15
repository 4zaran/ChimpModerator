package com.chimp.window;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class WindowMain extends JFrame {
    public JButton sendButton;
    public JTextPane logTextArea;
    public JTextField messageTextField;
    private StyledDocument doc;
    private Style style;
    private JScrollPane logScrollPane;

    public WindowMain() {
        super("Chimp Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(50, 50);
        setMinimumSize(new Dimension(200, 200));
        setLayout(new GridBagLayout());

        createUIElements();

        setVisible(true);
        pack();
    }

    private JTextPane createLogTextArea() {
        logTextArea = new JTextPane();

        doc = logTextArea.getStyledDocument();
        style = logTextArea.addStyle("default style", null);

        logTextArea.setEditable(false);
        logTextArea.setSize(new Dimension(300, 400));
        // logTextArea.setLineWrap(true);
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
        messageTextField.setEnabled(false);
        messageTextField.setText("Connecting. Please wait...");
        addObjects(messageTextField, layoutConstraints, GridBagConstraints.HORIZONTAL,
                0, 1, 1,
                0.9, 0.0, insets);

        sendButton = new JButton("Send");
        sendButton.setEnabled(false);
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
    public void printText(String[] textFragment, Color[] colors){
        if(textFragment.length == colors.length){
            for (int i = 0; i < textFragment.length; i++) {
                printText(textFragment[i], colors[i], false);
            }
            printText("");
        }
    }
    public void printText(String text, Color c) {
        printText(text, c, true);
    }

    public void printText(String text) {
        printText(text, Color.BLACK, true);
    }

    private void printText(String text, Color c, Boolean newLine){
        StyleConstants.setForeground(style, c);
        if(newLine)
            text += '\n';
        try { doc.insertString(doc.getLength(), text, style); }
        catch (BadLocationException ignored){}
    }

    public String getMessageText(){
        String messageToSend = messageTextField.getText();
        messageTextField.setText("");
        return messageToSend;
    }
}
