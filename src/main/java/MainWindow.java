import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
    public JTextArea logField;
    public JTextField messageField;
    public JButton sendMessageButton;

    public MainWindow() {
        super("Main Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(50,50);
        setLayout(new GridBagLayout());
        setSize(100,100);
        GridBagConstraints c = new GridBagConstraints();


        logField = new JTextArea("");
        logField.setEditable(false);
        logField.setSize(new Dimension(300,400));
        //logField.setMinimumSize(new Dimension(299,399));
        logField.setLineWrap(true);
        logField.setMargin(new Insets(5,5,5,5));
        JScrollPane scroll = new JScrollPane(logField,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setPreferredSize(new Dimension(300,400));
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.weighty = 0.95;
        c.weightx = 0.95;
        c.insets = new Insets(5,5,0,5);
        add(scroll, c);

        messageField = new JTextField();
        //messageField.setMaximumSize(new Dimension(150,300));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.9;
        c.weighty = 0.0;
        c.insets = new Insets(5,5,5,5);
        add(messageField, c);

        sendMessageButton = new JButton("Send");
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.05;
        c.weighty = 0.0;
        add(sendMessageButton, c);

        int a = 0;

        setVisible(true);
        pack();
    }
}