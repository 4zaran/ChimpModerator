package com.chimp.services;

import com.chimp.services.builders.MessageBuilder;
import com.chimp.services.builders.PreviewMessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Used to log messages in the window.
 */
public class Logger {

    /**
     * Logs the message into corresponding log pane.
     * @param event contains all necessary informations like message content, text channel and so on...
     */
    public void logMessage(@Nonnull MessageReceivedEvent event){
        // Build message
        MessageBuilder messageBuilder = new PreviewMessageBuilder(event.getMessage());
        messageBuilder.addAuthor();
        messageBuilder.addRawText();
        messageBuilder.addEmbed();
        messageBuilder.addAttachments();
        String messageContent = messageBuilder.getResult();

        // Setup colors
        String[] messageFragments = {getTime() + " ", messageContent};
        Color[] colors = {Color.gray, Color.BLACK};

        // Print
        String textChannelName = event.getTextChannel().getId() + "@"
                        + event.getGuild().getId();
        ContextService.getWindow().printText(messageFragments, colors, textChannelName);
    }

    /**
     * Logs passed text into view that corresponds to text channel.
     * @param info  text to log
     * @param textChannelId id of the log pane in window (format: {@code textChannelId@guildId})
     */
    public void logInfo(String info, String textChannelId){
        String[] messageFragments = {getTime(), " [INFO]: ", info};
        Color[] colors = {Color.gray, new Color(31, 158, 31), Color.BLACK};
        ContextService.getWindow().printText(messageFragments, colors, textChannelId);
    }

    /**
     * Logs passed text to default (console) view
     * @param info text to log
     */
    public void logInfo(String info){
        logInfo(info, "0");
    }

    /**
     * Adds an error to specified log panel
     * @param error text to log as an error
     * @param textChannelId name of the panel to log into
     */
    public void logError(String error, String textChannelId){
        String[] messageFragments = {getTime(), " [ERROR]: ", error};
        Color[] colors = {Color.gray, Color.RED, Color.BLACK};
        ContextService.getWindow().printText(messageFragments, colors, textChannelId);
    }

    /**
     * Returns the time for the log
     * @return formatted time to HH:MM
     */
    @NotNull
    private String getTime() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm");
        return myDateObj.format(myFormatObj);
    }
}
