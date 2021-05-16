package com.chimp.window;

import com.chimp.services.ApplicationService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.event.*;
import java.util.List;
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

        window.guildsBox.addActionListener(e -> {
            if(window.guildsBox.getSelectedIndex() != 0)
                switchToServer();
            else
                switchToConsole();
        });

        window.textChannelsBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String channelName = Objects.requireNonNull(window.textChannelsBox.getSelectedItem()) + "@" + Objects.requireNonNull(window.guildsBox.getSelectedItem());
                window.switchLogArea(channelName);
            }
        });
    }

    private void sendMessage() {
        String messageToSend = window.getMessageText();
        TextChannel textChannel = jda.getTextChannel(Objects.requireNonNull(window.textChannelsBox.getSelectedItem()).toString());
        if (!Objects.equals(messageToSend, ""))
            textChannel.sendMessage(messageToSend).queue();
    }
    @Override
    public void windowClosing(WindowEvent e) {
        window.dispose();
        jda.disconnect();
    }
    public void setupWindow(){
        List<Guild> guilds = jda.getJda().getGuilds();
        for (Guild guild: guilds) {
            window.guildsBox.addItem(guild.getName());
            List<TextChannel> textChannels = guild.getTextChannels();
            for(TextChannel textChannel: textChannels){
                String channelName = textChannel.getName() + "@" + guild.getName();
                window.switchLogArea(channelName);
            }
        }

        window.guildsBox.setEnabled(true);
        switchToConsole();
    }

    private void switchToConsole(){
        window.switchLogArea("console");
        window.sendButton.setEnabled(false);
        if(window.textChannelsBox.getItemCount() > 0)
            window.textChannelsBox.removeAllItems();
        window.textChannelsBox.setEnabled(false);
        window.messageTextField.setEnabled(false);
        window.messageTextField.setText("Choose server to send messages...");
    }

    private void switchToServer(){
        window.messageTextField.setText("");
        window.sendButton.setEnabled(true);
        window.textChannelsBox.removeAllItems();
        window.textChannelsBox.setEnabled(true);
        window.messageTextField.setEnabled(true);

        List<Guild> guilds = jda.getJda().getGuildsByName(Objects.requireNonNull(window.guildsBox.getSelectedItem()).toString(), false);
        if(guilds.size() > 0) {
            for (TextChannel channel : guilds.get(0).getTextChannels()) {
                window.textChannelsBox.addItem(channel.getName());
            }
        }
        String channelName = Objects.requireNonNull(window.textChannelsBox.getSelectedItem()) + "@" + window.guildsBox.getSelectedItem().toString();
        window.switchLogArea(channelName);
    }
}
