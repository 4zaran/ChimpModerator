package com.chimp.commands;

import com.chimp.services.AutoModerator;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class CommandUncensor implements Command{
    @Override
    public void execute(@NotNull MessageReceivedEvent event, List<String> parameters) {
        if(parameters.size() == 2){
            AutoModerator.uncensor(parameters.get(1));
            event.getTextChannel().sendMessage("Removing \"" + parameters.get(1) +"\" from censored expressions.").queue();
        }
        else{
            event.getTextChannel().sendMessage("Invalid syntax!").queue();
        }
    }

    @Override
    public String getDescription() {
        return "Used to remove provided text from censored expressions";
    }

    @Override
    public TreeMap<String, String> getSyntax() {
        TreeMap<String, String> commandsWithDescriptions= new TreeMap<>();
        commandsWithDescriptions.put("/uncensor (text)", "Uncensores given string");
        return commandsWithDescriptions;
    }
}
