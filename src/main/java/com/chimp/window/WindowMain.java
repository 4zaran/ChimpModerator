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
    private JComboBox<ComboItem> guildsBox;
    private JComboBox<ComboItem> textChannelsBox;
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

        guildText = new JLabel();
        getGuildText().setText("Server:");
        addObjects(getGuildText(), layoutConstraints, GridBagConstraints.HORIZONTAL,
                0, 0, 1,
                0, 0, insets);

        guildsBox = new JComboBox<>();
        getGuildsBox().addItem(new ComboItem("console", "0"));
        getGuildsBox().setEnabled(false);
        addObjects(getGuildsBox(), layoutConstraints, GridBagConstraints.HORIZONTAL,
                1, 0, 1,
                1, 0, insets);

        channelText = new JLabel();
        getChannelText().setText("Text channel:");
        addObjects(getChannelText(), layoutConstraints, GridBagConstraints.HORIZONTAL,
                2, 0, 1,
                0, 0, insets);

        textChannelsBox = new JComboBox<>();
        getTextChannelsBox().setEnabled(false);
        addObjects(getTextChannelsBox(), layoutConstraints, GridBagConstraints.HORIZONTAL,
                3, 0, 1,
                1, 0, insets);

        logPanes = new HashMap<>();
        // Console has the id of "0"
        getLogPanes().put("0", createLogTextArea());
        this.logScrollPane = new JScrollPane((getLogPanes().get("0")),
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        insets = new Insets(5, 5, 0, 5);
        addObjects(logScrollPane, layoutConstraints, GridBagConstraints.BOTH,
                0, 1, 4,
                0, 1, insets);

        insets = new Insets(5, 5, 5, 5);
        messageTextField = new JTextField();
        getMessageTextField().setEnabled(false);
        getMessageTextField().setText("Connecting. Please wait...");
        addObjects(getMessageTextField(), layoutConstraints, GridBagConstraints.HORIZONTAL,
                0, 2, 3,
                1, 0.0, insets);

        sendButton = new JButton("Send");
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
    public void printText(String[] textFragment, Color[] colors, String channelId){
        if(textFragment.length == colors.length){
            for (int i = 0; i < textFragment.length; i++) {
                printText(textFragment[i], colors[i], false, channelId);
            }
            printText("", channelId);
        }
        else{
            printText("Printing error...", Color.RED, channelId);
        }
    }
    public void printText(String text, Color c, String channelId) {
        printText(text, c, true, channelId);
    }
    public void printText(String text, String channelId) {
        printText(text, Color.BLACK, true, channelId);
    }

    private void printText(String text, Color c, Boolean newLine, String channelId){
        StyleConstants.setForeground(style, c);
        if(newLine)
            text += '\n';
        try {
            StyledDocument styledDocument = getDocById(channelId);
            if(styledDocument != null)
                styledDocument.insertString(styledDocument.getLength(), text, style);
        }
        catch (BadLocationException ignored){}
    }

    private StyledDocument getDocById(String id){
        JTextPane area = getLogPanes().get(id);
        if(area != null)
            return area.getStyledDocument();
        else return null;
    }


    public JLabel getGuildText() {
        return guildText;
    }

    public JLabel getChannelText() {
        return channelText;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public JTextField getMessageTextField() {
        return messageTextField;
    }

    public JComboBox<ComboItem> getGuildsBox() {
        return guildsBox;
    }

    public JComboBox<ComboItem> getTextChannelsBox() {
        return textChannelsBox;
    }

    public HashMap<String, JTextPane> getLogPanes() {
        return logPanes;
    }
}