package com.chimp.services;

import com.chimp.services.builders.MessageBuilder;
import com.chimp.services.builders.PreviewMessageBuilder;
import com.chimp.window.WindowMain;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    WindowMain window;
    public Logger(WindowMain window){
        this.window = window;
    }

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
        window.printText(messageFragments, colors, textChannelName);
    }

    @NotNull
    private String getTime() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm");
        return myDateObj.format(myFormatObj);
    }

    public void logInfo(String info){
        String[] messageFragments = {getTime(), " [INFO]: ", info};
        Color[] colors = {Color.gray, new Color(31, 158, 31), Color.BLACK};
        String textChannelId = "0";
        window.printText(messageFragments, colors, textChannelId);
    }

    public void logError(String error){
        String[] messageFragments = {getTime(), " [ERROR]: ", error};
        Color[] colors = {Color.gray, Color.RED, Color.BLACK};
        String textChannelId = "0";
        window.printText(messageFragments, colors, textChannelId);
    }
}
