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

        // TODO DEBUG
        window.getGuildsBox().addActionListener(e -> {
            if(window.getGuildsBox().getSelectedIndex() != 0)
                switchToServer();
            else
                switchToConsole();
        });

        window.getTextChannelsBox().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ComboItem channel = (ComboItem) window.getTextChannelsBox().getSelectedItem();
                ComboItem guild = (ComboItem) window.getGuildsBox().getSelectedItem();
                assert channel != null;
                assert guild != null;
                String channelName = channel.getId() + "@" + guild.getId();
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
            window.getGuildsBox().addItem(new ComboItem(guild.getName(), guild.getId()));
            List<TextChannel> textChannels = guild.getTextChannels();
            for(TextChannel textChannel: textChannels){
                String channelName = textChannel.getId() + "@" + guild.getId();
                window.addLogArea(channelName);
            }
        }
        window.getGuildsBox().setEnabled(true);
    }

    private void sendMessage() {
        String messageToSend = window.getMessageText();
        ComboItem channel = (ComboItem) window.getTextChannelsBox().getSelectedItem();
        assert channel != null;
        TextChannel textChannel = jda.getJda().getTextChannelById(channel.getId());
        if (!Objects.equals(messageToSend, "")) {
            assert textChannel != null;
            textChannel.sendMessage(messageToSend).queue();
        }
    }
    private void switchToConsole(){
        window.switchLogArea("0");
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

        String guildId = ((ComboItem) Objects.requireNonNull(window.getGuildsBox().getSelectedItem())).getId();
        Guild guild = jda.getJda().getGuildById(guildId);
        if(guild != null) {
            for (TextChannel channel : guild.getTextChannels()) {
                window.getTextChannelsBox().addItem(new ComboItem(channel.getName(), channel.getId()));
            }
        }
        guildId = ((ComboItem) window.getGuildsBox().getSelectedItem()).getId();
        String textChannelId = ((ComboItem) Objects.requireNonNull(window.getTextChannelsBox().getSelectedItem())).getId();
        String channelId = textChannelId + "@" + guildId;
        window.switchLogArea(channelId);
    }
}