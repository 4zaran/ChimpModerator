package com.chimp.window;

import com.chimp.services.ApplicationService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.event.*;
import java.util.List;
import java.util.Objects;

public class WindowManager extends WindowAdapter {
    private final ApplicationService jda;
    private final WindowMain window;

    public WindowManager(ApplicationService jda, WindowMain window){
        this.window = window;
        window.addWindowListener(this);
        this.jda = jda;

        window.getSendButton().addActionListener(e -> sendMessage());

        window.getMessageTextField().addKeyListener(new KeyListener() {
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

        window.getGuildsBox().addActionListener(e -> {
            if(window.getGuildsBox().getSelectedIndex() != 0)
                switchToServer();
            else
                switchToConsole();
        });

        window.getTextChannelsBox().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String channelName = Objects.requireNonNull(window.getTextChannelsBox().getSelectedItem()) + "@" + Objects.requireNonNull(window.getGuildsBox().getSelectedItem());
                window.switchLogArea(channelName);
            }
        });
    }

    @Override
    public void windowClosing(WindowEvent e) {
        window.dispose();
        jda.disconnect();
    }
    public void setupConsole(){
        switchToConsole();
    }
    public void setupWindow(){
        List<Guild> guilds = jda.getJda().getGuilds();
        for (Guild guild: guilds) {
            window.getGuildsBox().addItem(guild.getName());
            List<TextChannel> textChannels = guild.getTextChannels();
            for(TextChannel textChannel: textChannels){
                String channelName = textChannel.getName() + "@" + guild.getName();
                window.addLogArea(channelName);
            }
        }
        window.getGuildsBox().setEnabled(true);
    }

    private void sendMessage() {
        String messageToSend = window.getMessageText();
        TextChannel textChannel = jda.getTextChannel(Objects.requireNonNull(window.getTextChannelsBox().getSelectedItem()).toString());
        if (!Objects.equals(messageToSend, ""))
            textChannel.sendMessage(messageToSend).queue();
    }
    private void switchToConsole(){
        window.switchLogArea("console");
        window.getSendButton().setEnabled(false);
        if(window.getTextChannelsBox().getItemCount() > 0)
            window.getTextChannelsBox().removeAllItems();
        window.getTextChannelsBox().setEnabled(false);
        window.getMessageTextField().setEnabled(false);
        window.getMessageTextField().setText("Choose server to send messages...");
    }
    private void switchToServer(){
        window.getMessageTextField().setText("");
        window.getSendButton().setEnabled(true);
        window.getTextChannelsBox().removeAllItems();
        window.getTextChannelsBox().setEnabled(true);
        window.getMessageTextField().setEnabled(true);

        List<Guild> guilds = jda.getJda().getGuildsByName(Objects.requireNonNull(window.getGuildsBox().getSelectedItem()).toString(), false);
        if(guilds.size() > 0) {
            for (TextChannel channel : guilds.get(0).getTextChannels()) {
                window.getTextChannelsBox().addItem(channel.getName());
            }
        }
        String channelName = Objects.requireNonNull(window.getTextChannelsBox().getSelectedItem()) + "@" + window.getGuildsBox().getSelectedItem().toString();
        window.switchLogArea(channelName);
    }
}
// TODO wchłonięcie funkcji