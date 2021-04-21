
package com.chimp;
import com.chimp.commands.Command;
import com.chimp.commands.CommandBan;
import com.chimp.commands.CommandKick;
import com.chimp.services.ApplicationService;
import com.chimp.services.Logger;
import com.chimp.services.MessageInterpreter;
import com.chimp.window.WindowMain;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

public class ApplicationMain extends ListenerAdapter {
    private static ApplicationService jda;
    private static WindowMain window;
    private static Logger logger;
    private static MessageInterpreter interpreter;

    public static void main(String[] args) {
        interpreter = new MessageInterpreter();
        window = new WindowMain();
        logger = new Logger(window);
        jda = new ApplicationService();
        jda.connect();
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        window.printText("Connected!");

        window.sendButton.addActionListener(e -> sendMessage());

        window.messageTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        interpreter.handleMessage(event);
        logger.logMessage(event);
    }

    private void sendMessage() {
        String messageToSend = window.messageTextField.getText();
        if(messageToSend.equals("/exit")){
            jda.disconnect();
        }
        window.messageTextField.setText("");
        TextChannel textChannel = jda.getTextChannel("general");
        if (!Objects.equals(messageToSend, ""))
            textChannel.sendMessage(messageToSend).queue();
    }
}