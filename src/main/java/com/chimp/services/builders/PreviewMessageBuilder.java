package com.chimp.services.builders;

import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

public class PreviewMessageBuilder implements MessageBuilder {

    Message message;
    StringBuilder messageString;

    public PreviewMessageBuilder(Message message){
        this.message = message;
        this.messageString = new StringBuilder();
    }

    @Override
    public void addAuthor(){
        messageString.append(message.getMember().getEffectiveName()).append(": ");
    }
    @Override
    public void addRawText(){
        if(message.getContentRaw().length() > 0)
            messageString.append(message.getContentRaw()).append('\n');
    }
    @Override
    public void addEmbed(){
        List<MessageEmbed> embeds = message.getEmbeds();
        for(MessageEmbed embed: embeds){
            EmbedType embedType = embed.getType();
            switch (embedType) {
                case RICH:
                    messageString.append(embed.getTitle()).append("\n");
                    messageString.append(embed.getDescription()).append("\n");
                    for (MessageEmbed.Field f : embed.getFields()) {
                        messageString.append(f.getName()).append(" - ");
                        messageString.append(f.getValue()).append("\n");
                    }
                case IMAGE:
                    messageString.append("IMAGE: [");
                    messageString.append(embed.getImage().getUrl()).append("]\n");
                case VIDEO:
                    messageString.append("VIDEO: [");
                    messageString.append(embed.getVideoInfo().toString()).append("]\n");
            }
        }
    }
    @Override
    public void addAttachments(){
        List<Message.Attachment> attachments = message.getAttachments();
        for(Message.Attachment attachment: attachments){
            String fileName = attachment.getFileName();
            messageString.append("FILE: [");
            messageString.append(fileName).append("]\n");
        }
    }
    @Override
    public String getResult(){
        if(messageString.toString().endsWith("\n"))
            messageString.deleteCharAt(messageString.length() - 1);
        return messageString.toString();
    }
}
