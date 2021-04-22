package com.chimp.window;

import com.chimp.services.ApplicationService;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class WindowManager extends WindowAdapter {
    private ApplicationService jda;
    private WindowMain window;

    public WindowManager(ApplicationService jda, WindowMain window){
        this.window = window;
        window.addWindowListener(this);
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
    @Override
    public void windowClosing(WindowEvent e) {
        window.dispose();
        jda.disconnect();
    }
}
