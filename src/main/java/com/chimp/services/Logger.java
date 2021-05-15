package com.chimp.services;

import com.chimp.window.WindowMain;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class Logger {
    WindowMain window;
    public Logger(WindowMain window){
        this.window = window;
    }

    public void logMessage(@Nonnull MessageReceivedEvent event){
        Message message = event.getMessage();

        StringBuilder messageContent = new StringBuilder(message.getContentRaw());
        if(message.getContentRaw().length() > 0)
            messageContent.append("\n");

        if(message.getEmbeds().size() > 0){
            List<MessageEmbed> embeds = message.getEmbeds();
            for(MessageEmbed embed: embeds){
                EmbedType embedType = embed.getType();
                if(embedType == EmbedType.RICH){
                    messageContent.append(embed.getTitle()).append("\n");
                    messageContent.append(embed.getDescription()).append("\n");
                    for(MessageEmbed.Field f: embed.getFields()){
                        messageContent.append(f.getName()).append(" - ");
                        messageContent.append(f.getValue()).append("\n");
                    }
                }
            }
        }

        if(message.getAttachments().size() > 0){
            List<Message.Attachment> attachments = message.getAttachments();
            for(Message.Attachment attachment: attachments){
                String fileName = attachment.getFileName();
                messageContent.append(fileName).append("\n");
            }
        }

        messageContent.insert(0,Objects.requireNonNull(event.getMember()).getEffectiveName() + ": ");
        if(messageContent.toString().endsWith("\n"))
            messageContent.deleteCharAt(messageContent.length() - 1);

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm");
        String formattedDate = myDateObj.format(myFormatObj);

        String[] messageFragments = {formattedDate + " ", messageContent.toString()};
        Color[] colors = {Color.gray, Color.BLACK};

        window.printText(messageFragments, colors);
    }

    public void logInfo(String info){
        String[] messageFragments = {"[INFO]: ", info};
        Color[] colors = {new Color(31, 158, 31), Color.BLACK};
        window.printText(messageFragments, colors);
    }

    public void logError(String error){
        String[] messageFragments = {"[ERROR]: ", error};
        Color[] colors = {Color.RED, Color.BLACK};
        window.printText(messageFragments, colors);
    }
}
