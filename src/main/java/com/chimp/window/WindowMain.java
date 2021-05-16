package com.chimp.window;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.HashMap;

public class WindowMain extends JFrame {
    public JLabel guildText;
    public JLabel channelText;
    public JButton sendButton;
    public JTextField messageTextField;
    public JComboBox<String> guildsBox;
    public JComboBox<String> textChannelsBox;
    public HashMap<String, JTextPane> logPanes;
    private Style style;
    private JScrollPane logScrollPane;

    public WindowMain() {
        super("Chimp Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(50, 50);
        setMinimumSize(new Dimension(400, 300));
        setLayout(new GridBagLayout());

        createUIElements();

        setVisible(true);
        pack();
    }

    private JTextPane createLogTextArea() {
        JTextPane logTextArea = new JTextPane();

        style = logTextArea.addStyle("default style", null);

        logTextArea.setEditable(false);
        logTextArea.setSize(new Dimension(300, 400));
        // logTextArea.setLineWrap(true);
        logTextArea.setMargin(new Insets(5, 5, 5, 5));
        return logTextArea;
    }

    private JScrollPane logScrollPane(JTextPane area) {
        JScrollPane logScrollPane = new JScrollPane(area,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        logScrollPane.setPreferredSize(new Dimension(300, 400));
        return logScrollPane;
    }

    private void createUIElements() {
        GridBagConstraints layoutConstraints = new GridBagConstraints();
        Insets insets = new Insets(5, 5, 5, 5);

        guildText = new JLabel();
        guildText.setText("Server:");
        addObjects(guildText, layoutConstraints, GridBagConstraints.HORIZONTAL,
                0, 0, 1,
                0, 0, insets);

        guildsBox = new JComboBox<>();
        guildsBox.addItem("Console");
        guildsBox.setEnabled(false);
        addObjects(guildsBox, layoutConstraints, GridBagConstraints.HORIZONTAL,
                1, 0, 1,
                1, 0, insets);

        channelText = new JLabel();
        channelText.setText("Text channel:");
        addObjects(channelText, layoutConstraints, GridBagConstraints.HORIZONTAL,
                2, 0, 1,
                0, 0, insets);

        textChannelsBox = new JComboBox<>();
        textChannelsBox.setEnabled(false);
        addObjects(textChannelsBox, layoutConstraints, GridBagConstraints.HORIZONTAL,
                3, 0, 1,
                1, 0, insets);

        logPanes = new HashMap<>();
        logPanes.put("console", createLogTextArea());
        logScrollPane = logScrollPane(logPanes.get("console"));
        insets = new Insets(5, 5, 0, 5);
        addObjects(logScrollPane, layoutConstraints, GridBagConstraints.BOTH,
                0, 1, 4,
                0, 1, insets);

        insets = new Insets(5, 5, 5, 5);
        messageTextField = new JTextField();
        messageTextField.setEnabled(false);
        messageTextField.setText("Connecting. Please wait...");
        addObjects(messageTextField, layoutConstraints, GridBagConstraints.HORIZONTAL,
                0, 2, 3,
                1, 0.0, insets);

        sendButton = new JButton("Send");
        sendButton.setEnabled(false);
        addObjects(sendButton, layoutConstraints, GridBagConstraints.NONE,
                3, 2, 1,
                1, 0.0, insets);
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
    public void printText(String[] textFragment, Color[] colors, String channel){
        if(textFragment.length == colors.length){
            for (int i = 0; i < textFragment.length; i++) {
                printText(textFragment[i], colors[i], false, channel);
            }
            printText("", channel);
        }
        else{
            printText("Printing error...", Color.RED, channel);
        }
    }
    public void printText(String text, Color c, String channel) {
        printText(text, c, true, channel);
    }

    public void printText(String text, String channel) {
        printText(text, Color.BLACK, true, channel);
    }

    private void printText(String text, Color c, Boolean newLine, String channel){
        StyleConstants.setForeground(style, c);
        if(newLine)
            text += '\n';
        try {
            StyledDocument styledDocument = getDocByName(channel);
            if(styledDocument != null)
                styledDocument.insertString(styledDocument.getLength(), text, style);
        }
        catch (BadLocationException ignored){}
    }

    public String getMessageText(){
        String messageToSend = messageTextField.getText();
        messageTextField.setText("");
        return messageToSend;
    }

    public void switchLogArea(String areaName){
        JTextPane area = logPanes.get(areaName);
        if(area == null){
            logPanes.put(areaName, createLogTextArea());
            switchLogArea(areaName);
        }
        else{
            logScrollPane.setViewportView(area);
        }
    }

    private StyledDocument getDocByName(String name){
        JTextPane area = logPanes.get(name);
        if(area != null)
            return area.getStyledDocument();
        else return null;
    }
}
