package com.chimp.window;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.HashMap;

public class WindowMain extends JFrame {
    private JLabel guildText;
    private JLabel channelText;
    private JButton sendButton;
    private JTextField messageTextField;
    private JComboBox<String> guildsBox;
    private JComboBox<String> textChannelsBox;
    private HashMap<String, JTextPane> logPanes;
    private Style style;
    private JScrollPane logScrollPane;

    public WindowMain() {
        super("Chimp Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(50, 50);
        setMinimumSize(new Dimension(400, 300));
        setPreferredSize(new Dimension(400, 500));
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
    private void createUIElements() {
        GridBagConstraints layoutConstraints = new GridBagConstraints();
        Insets insets = new Insets(5, 5, 5, 5);

        setGuildText(new JLabel());
        getGuildText().setText("Server:");
        addObjects(getGuildText(), layoutConstraints, GridBagConstraints.HORIZONTAL,
                0, 0, 1,
                0, 0, insets);

        setGuildsBox(new JComboBox<>());
        getGuildsBox().addItem("Console");
        getGuildsBox().setEnabled(false);
        addObjects(getGuildsBox(), layoutConstraints, GridBagConstraints.HORIZONTAL,
                1, 0, 1,
                1, 0, insets);

        setChannelText(new JLabel());
        getChannelText().setText("Text channel:");
        addObjects(getChannelText(), layoutConstraints, GridBagConstraints.HORIZONTAL,
                2, 0, 1,
                0, 0, insets);

        setTextChannelsBox(new JComboBox<>());
        getTextChannelsBox().setEnabled(false);
        addObjects(getTextChannelsBox(), layoutConstraints, GridBagConstraints.HORIZONTAL,
                3, 0, 1,
                1, 0, insets);

        setLogPanes(new HashMap<>());
        getLogPanes().put("console", createLogTextArea());
        this.logScrollPane = new JScrollPane((getLogPanes().get("console")),
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        insets = new Insets(5, 5, 0, 5);
        addObjects(logScrollPane, layoutConstraints, GridBagConstraints.BOTH,
                0, 1, 4,
                0, 1, insets);

        insets = new Insets(5, 5, 5, 5);
        setMessageTextField(new JTextField());
        getMessageTextField().setEnabled(false);
        getMessageTextField().setText("Connecting. Please wait...");
        addObjects(getMessageTextField(), layoutConstraints, GridBagConstraints.HORIZONTAL,
                0, 2, 3,
                1, 0.0, insets);

        setSendButton(new JButton("Send"));
        getSendButton().setEnabled(false);
        addObjects(getSendButton(), layoutConstraints, GridBagConstraints.NONE,
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

    public String getMessageText(){
        String messageToSend = getMessageTextField().getText();
        getMessageTextField().setText("");
        return messageToSend;
    }
    public void addLogArea(String areaName){
        getLogPanes().put(areaName, createLogTextArea());
    }
    public void switchLogArea(String areaName){
        JTextPane area = getLogPanes().get(areaName);
        //TODO change name to channelID@guildID
        if(area == null){
            addLogArea(areaName);
            switchLogArea(areaName);
        }
        else{
            logScrollPane.setViewportView(area);
        }
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
    private StyledDocument getDocByName(String name){
        JTextPane area = getLogPanes().get(name);
        if(area != null)
            return area.getStyledDocument();
        else return null;
    }


    public JLabel getGuildText() {
        return guildText;
    }

    public void setGuildText(JLabel guildText) {
        this.guildText = guildText;
    }

    public JLabel getChannelText() {
        return channelText;
    }

    public void setChannelText(JLabel channelText) {
        this.channelText = channelText;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public void setSendButton(JButton sendButton) {
        this.sendButton = sendButton;
    }

    public JTextField getMessageTextField() {
        return messageTextField;
    }

    public void setMessageTextField(JTextField messageTextField) {
        this.messageTextField = messageTextField;
    }

    public JComboBox<String> getGuildsBox() {
        return guildsBox;
    }

    public void setGuildsBox(JComboBox<String> guildsBox) {
        this.guildsBox = guildsBox;
    }

    public JComboBox<String> getTextChannelsBox() {
        return textChannelsBox;
    }

    public void setTextChannelsBox(JComboBox<String> textChannelsBox) {
        this.textChannelsBox = textChannelsBox;
    }

    public HashMap<String, JTextPane> getLogPanes() {
        return logPanes;
    }

    public void setLogPanes(HashMap<String, JTextPane> logPanes) {
        this.logPanes = logPanes;
    }
}
