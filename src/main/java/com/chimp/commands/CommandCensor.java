package com.chimp.commands;

import com.chimp.services.AutoModerator;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class CommandCensor implements Command{

    @Override
    public void execute(@NotNull MessageReceivedEvent event, List<String> parameters) {
        if(parameters.size() == 2){
            AutoModerator.censor(parameters.get(1));
            event.getTextChannel().sendMessage("Censoring: " + parameters.get(1)).queue();
        }
        else{
            event.getTextChannel().sendMessage("Invalid syntax!").queue();
        }
    }

    @Override
    public String getDescription() {
        return "Used to censor provided text";
    }

    @Override
    public TreeMap<String, String> getSyntax() {
        TreeMap<String, String> commandsWithDescriptions= new TreeMap<>();
        commandsWithDescriptions.put("/censor (text)", "Censores given string");
        return commandsWithDescriptions;
    }
}
