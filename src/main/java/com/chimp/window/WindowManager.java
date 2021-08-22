package com.chimp.window;

import com.chimp.services.ApplicationService;
import com.chimp.services.ContextService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.event.*;
import java.util.List;
import java.util.Objects;

/**
 * This class manages the {@link WindowMain} class.
 * Setups window before use by loading all guilds / text channels into it.
 * Helps to switch between main console and text channels.
 */
public class WindowManager extends WindowAdapter {
    /** Object of application's main window */
    private final WindowMain window;
    /** Object holding JDA main class. */
    private final ApplicationService appService;
    /** Keeps the last sent message to restore it's content with up arrow key ({@code VK_UP})*/
    private String lastMessage;

    // -- CONSTRUCTORS --

    /**
     * Sets all private fields and adds necessary action listeners for the window.
     */
    public WindowManager(){
        this.window = ContextService.getWindow();
        this.window.addWindowListener(this);
        this.appService = ContextService.getAppService();
        this.lastMessage = "";

        window.getSendButton().addActionListener(e -> sendMessage());

        window.getMessageTextField().addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) { }
            @Override public void keyReleased(KeyEvent e) { }
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
                else if (e.getKeyCode() == KeyEvent.VK_UP){
                    window.getMessageTextField().setText(lastMessage);
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
                ComboItem channel = (ComboItem) window.getTextChannelsBox().getSelectedItem();
                ComboItem guild = (ComboItem) window.getGuildsBox().getSelectedItem();
                assert channel != null;
                assert guild != null;
                String channelName = channel.getId() + "@" + guild.getId();
                window.switchLogArea(channelName);
            }
        });
    }

    // -- PUBLIC METHODS

    /**
     * Makes sure that when window is closed, application will properly disconnect.
     * @param event not used
     */
    @Override
    public void windowClosing(WindowEvent event) {
        window.dispose();
        appService.disconnect();
    }

    /**
     * Setups console by switching the view to it
     */
    public void setupConsole(){
        switchToConsole();
    }

    /**
     * Setups window by pulling all guild and channels and loading them into {@code ComboBox}es
     */
    public void setupWindow(){
        List<Guild> guilds = appService.getJda().getGuilds();
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

    // -- PRIVATE METHODS

    /**
     * Method used when {@link WindowMain#getSendButton()}  send} button is clicked.
     */
    private void sendMessage() {
        String messageToSend = window.getMessageText();
        lastMessage = messageToSend;
        ComboItem channel = (ComboItem) window.getTextChannelsBox().getSelectedItem();
        assert channel != null;
        TextChannel textChannel = appService.getJda().getTextChannelById(channel.getId());

        if (!Objects.equals(messageToSend, "")) {
            // TODO ADD HANDLING FOR COMMANDS
            if(messageToSend.startsWith("/")){
                ContextService.getInterpreter().handleMessage(messageToSend , window.getCurrentLogArea());
            }
            else{
                assert textChannel != null;
                textChannel.sendMessage(messageToSend).queue();
            }
        }
    }

    /**
     * Switches the view to main log.
     * In this view, user can't send message,
     * it is used to display general info about
     * application's status (like successful connection) and errors encountered.
     */
    private void switchToConsole(){
        window.switchLogArea("0");
        window.getSendButton().setEnabled(false);
        if(window.getTextChannelsBox().getItemCount() > 0)
            window.getTextChannelsBox().removeAllItems();
        window.getTextChannelsBox().setEnabled(false);
        window.getMessageTextField().setEnabled(false);
        window.getMessageTextField().setText("Choose server to send messages...");
    }

    /**
     * Switches to view that displays messages logged on selected text channel.
     * In this mode user can send messages directly from main window.
     */
    private void switchToServer(){
        window.getMessageTextField().setText("");
        window.getSendButton().setEnabled(true);
        window.getTextChannelsBox().removeAllItems();
        window.getTextChannelsBox().setEnabled(true);
        window.getMessageTextField().setEnabled(true);

        String guildId = ((ComboItem) Objects.requireNonNull(window.getGuildsBox().getSelectedItem())).getId();
        Guild guild = appService.getJda().getGuildById(guildId);
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