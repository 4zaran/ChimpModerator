package com.chimp.window;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.HashMap;

/**
 * The class of the main window of application.
 */
public class WindowMain extends JFrame {

    /** Button that sends the message to desired channel */
    private JButton sendButton;

    /** Text field used to input messages or commands from application window */
    private JTextField messageTextField;

    /** ComboBox containing all available guilds */
    private JComboBox<ComboItem> guildsBox;

    /** ComboBox containing all available text channels from selected guild in {@link #guildsBox} */
    private JComboBox<ComboItem> textChannelsBox;

    /** Used to change color of the font */
    private Style style;

    /** Provides scrollable view for currently selected logPane */
    private JScrollPane logScrollPane;

    /** Holds the name of currently selected {@link #logScrollPane} */
    private String currentLogArea;

    /**  Every pane holds received messages from corresponding text channel */
    private HashMap<String, JTextPane> logPanes;

    // -- CONSTRUCTORS --
    /**
     * Setups all parameters of the window like it's content, size and location.
     */
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

    // -- PUBLIC METHODS --
    /**
     * Adds new log area. In most cases, for text channel.
     * @param areaName  name that new log area will be associated with
     */
    public void addLogArea(String areaName){
        getLogPanes().put(areaName, createLogTextArea());
    }

    /**
     * Tries to change current log area to area with passed name.
     * If area with such name wasn't found, it calls {@link #addLogArea(String)} to add it and then tries again.
     * It is used to change (and add) the log area for the current text channel.
     * @param areaName  name of the desired
     */
    public void switchLogArea(String areaName){
        JTextPane area = getLogPanes().get(areaName);
        if(area == null){
            addLogArea(areaName);
            switchLogArea(areaName);
        }
        else{
            currentLogArea = areaName;
            logScrollPane.setViewportView(area);
        }
    }

    /**
     * Prints text on log area for specified channel.
     * It uses two lists to define the colors of individual parts of the message.
     * <p>For example, the first expression from {@code String[]} list matches the first {@code Color[]}.
     * That being said, the fragment from {@code textFragment[0]} will be printed in color from {@code colors[0]}.
     * <p><b>IMPORTANT!</b> Please keep in mind, that both lists have to be the same size.
     * @param textFragment list containing all parts of text to be printed
     * @param colors list containing colors for passed parts of the text
     * @param logAreaName name of the log area to print text to
     */
    public void printText(String[] textFragment, Color[] colors, String logAreaName){
        if(textFragment.length == colors.length){
            for (int i = 0; i < textFragment.length; i++) {
                printText(textFragment[i], colors[i], false, logAreaName);
            }
            printText("", logAreaName);
        }
        // TODO throw exception
        else{
            printText("Printing error...", Color.RED, logAreaName);
        }
    }

    /**
     * Prints text on log area for specified channel in desired color.
     * @param text text to print
     * @param color color in which the text should be printed
     * @param logAreaName name of the log area to print text to
     */
    public void printText(String text, Color color, String logAreaName) {
        printText(text, color, true, logAreaName);
    }

    /**
     * Prints text on log area corresponding to text channel.
     * <p><b>NOTE</b> The name of the channel is equal to {@code channelId@guildId}
     * @param text  text to print
     * @param logAreaName name of the area to
     */
    public void printText(String text, String logAreaName) {
        printText(text, Color.BLACK, true, logAreaName);
    }

    // -- PRIVATE METHODS --
    /**
     * Creates the {@link JTextPane} object and sets its properties.
     * @return created JTextPane object
     */
    private JTextPane createLogTextArea() {
        JTextPane logTextArea = new JTextPane();

        style = logTextArea.addStyle("default style", null);

        logTextArea.setEditable(false);
        logTextArea.setSize(new Dimension(300, 400));
        // logTextArea.setLineWrap(true);
        logTextArea.setMargin(new Insets(5, 5, 5, 5));
        return logTextArea;
    }

    /**
     * Creates all user interface elements in the window.
     */
    private void createUIElements() {

        JLabel guildText = new JLabel();
        guildText.setText("Server:");
        addObjects(guildText, 0, 0, 1, 0, 0);

        guildsBox = new JComboBox<>();
        guildsBox.addItem(new ComboItem("console", "0"));
        guildsBox.setEnabled(false);
        addObjects(guildsBox, 1, 0, 1, 1, 0);

        JLabel channelText = new JLabel();
        channelText.setText("Text channel:");
        addObjects(channelText, 2, 0, 1, 0, 0);

        textChannelsBox = new JComboBox<>();
        textChannelsBox.setEnabled(false);
        addObjects(textChannelsBox, 3, 0, 1, 1, 0);

        logPanes = new HashMap<>();
        // Console has the id of "0"
        logPanes.put("0", createLogTextArea());
        currentLogArea = "0";
        logScrollPane = new JScrollPane(logPanes.get("0"),
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        addObjects(logScrollPane, 0, 1, 4, 0, 1,
                GridBagConstraints.BOTH, new Insets(5, 5, 0, 5));

        messageTextField = new JTextField();
        messageTextField.setEnabled(false);
        messageTextField.setText("Connecting. Please wait...");
        addObjects(messageTextField, 0, 2, 3, 1, 0.0);

        sendButton = new JButton("Send");
        sendButton.setEnabled(false);
        addObjects(sendButton, 3, 2, 1, 1, 0.0, GridBagConstraints.NONE);
    }

    /**
     * Adds component with given properties to the window.
     * @param component element to be added
     * @param fill fill property
     * @param gridX X coordinate in window's grid layout
     * @param gridY Y coordinate in window's grid layout
     * @param gridWidth width of the element in grid
     * @param weightX {@link GridBagConstraints#weightx}
     * @param weightY {@link GridBagConstraints#weighty}
     * @param insets {@link Insets} object
     * @see GridBagConstraints
     */
    private void addObjects(Component component,
                            int gridX,
                            int gridY,
                            int gridWidth,
                            double weightX,
                            double weightY,
                            int fill,
                            Insets insets) {

        // TODO: decrease the number of parameters
        GridBagConstraints layoutConstraints = new GridBagConstraints();

        layoutConstraints.fill = fill;
        layoutConstraints.gridx = gridX;
        layoutConstraints.gridy = gridY;
        layoutConstraints.gridwidth = gridWidth;
        layoutConstraints.weightx = weightX;
        layoutConstraints.weighty = weightY;
        layoutConstraints.insets = insets;

        add(component, layoutConstraints);
    }

    private void addObjects(Component component,
                            int gridX,
                            int gridY,
                            int gridWidth,
                            double weightX,
                            double weightY,
                            int fill){
        addObjects(component, gridX, gridY, gridWidth, weightX, weightY, fill, new Insets(5,5,5,5));
    }

    private void addObjects(Component component,
                            int gridX,
                            int gridY,
                            int gridWidth,
                            double weightX,
                            double weightY){
        addObjects(component, gridX, gridY, gridWidth, weightX, weightY, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5));
    }

    /**
     * Prints given text in desired color.
     * @param text  text to print
     * @param c color to print with
     * @param newLine   add new line before printing text
     * @param logAreaName   log area to print to
     */
    private void printText(String text, Color c, Boolean newLine, String logAreaName){
        StyleConstants.setForeground(style, c);
        if(newLine)
            text += '\n';
        try {
            StyledDocument styledDocument = getDocById(logAreaName);
            if(styledDocument != null)
                styledDocument.insertString(styledDocument.getLength(), text, style);
        }
        catch (BadLocationException ignored){}
    }

    /**
     * Searches for log pane with given name and returns its {@link StyledDocument} object
     * @param logPaneName   name of the log pane
     * @return {@link StyledDocument} object of corresponding log pane
     * @see #logPanes
     */
    private StyledDocument getDocById(String logPaneName){
        JTextPane area = getLogPanes().get(logPaneName);
        if(area != null)
            return area.getStyledDocument();
        else return null;
    }

    // -- GETTERS --
    public JButton getSendButton() { return sendButton; }

    public JTextField getMessageTextField() { return messageTextField; }

    public JComboBox<ComboItem> getGuildsBox() { return guildsBox; }

    public JComboBox<ComboItem> getTextChannelsBox() { return textChannelsBox; }

    public HashMap<String, JTextPane> getLogPanes() { return logPanes; }

    public String getCurrentLogArea(){ return currentLogArea; }

    public String getMessageText(){
        String messageToSend = getMessageTextField().getText();
        getMessageTextField().setText("");
        return messageToSend;
    }
}