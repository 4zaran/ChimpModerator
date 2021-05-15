package com.chimp.commands;

import com.chimp.services.AutoModerator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.TreeMap;

public class CommandCensored implements Command{
    @Override
    public void execute(@NotNull MessageReceivedEvent event, List<String> parameters) {
        TextChannel textChannel = event.getTextChannel();
        String expressions = AutoModerator.censored();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(4, 133, 241));
        eb.setTitle("Censored expressions");
        if(!expressions.isEmpty()) {
            eb.setDescription(expressions);
        }
        else{
            eb.setDescription("No censored expressions found.");
        }
        textChannel.sendMessage(eb.build()).queue();
    }

    @Override
    public String getDescription() {
        return "Displays current censored expressions";
    }

    @Override
    public TreeMap<String, String> getSyntax() {
        TreeMap<String, String> commandsWithDescriptions= new TreeMap<>();
        commandsWithDescriptions.put("/censored", "Sends list of censored expressions");
        return commandsWithDescriptions;
    }
}
