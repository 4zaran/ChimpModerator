package com.chimp.window;

import com.chimp.services.ApplicationService;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

public class WindowManager {
    private ApplicationService jda;
    private WindowMain window;

    public WindowManager(ApplicationService jda, WindowMain window){
        this.window = window;
        this.jda = jda;

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

    private void sendMessage() {
        String messageToSend = window.getMessageText();
        TextChannel textChannel = jda.getTextChannel("general");
        if (!Objects.equals(messageToSend, ""))
            textChannel.sendMessage(messageToSend).queue();
    }
}
