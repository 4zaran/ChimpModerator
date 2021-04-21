
package com.chimp;
import com.chimp.commands.Command;
import com.chimp.commands.CommandBan;
import com.chimp.commands.CommandKick;
import com.chimp.window.WindowMain;

import net.dv8tion.jda.api.Permission;
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

    public static void main(String[] args) {
        window = new WindowMain();
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
        String messageContent = event.getMessage().getContentRaw();

        window.printText(event.getMember().getEffectiveName() + ": " + messageContent);

        if (messageContent.toLowerCase().startsWith("/kick")){
            Command c = new CommandKick();
            c.execute(event);
        }
        if (messageContent.toLowerCase().startsWith("/ban")){
            Command c = new CommandBan();
            c.execute(event);
        }
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