package com.chimp.commands;

import com.chimp.services.AutoModerator;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class CommandCensored implements Command{
    @Override
    public void execute(@NotNull MessageReceivedEvent event, List<String> parameters) {
        TextChannel textChannel = event.getTextChannel();
        String expressions = AutoModerator.censored();
        textChannel.sendMessage("Censored expressions:\n" + expressions).queue();
    }

    @Override
    public @NotNull String getDescription() {
        return "Displays current censored expressions";
    }

    @Override
    public @NotNull HashMap<String, String> getSyntax() {
        HashMap<String, String> commandsWithDescriptions= new HashMap<>();
        commandsWithDescriptions.put("/censored", "Sends list of censored expressions");
        return commandsWithDescriptions;
    }
}
