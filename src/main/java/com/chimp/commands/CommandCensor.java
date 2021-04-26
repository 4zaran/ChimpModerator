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
        if(parameters.size() >= 2){
            int i = 1;
            StringBuilder exp = new StringBuilder();
            while (i < parameters.size()) {
                //TODO error on already censored
                AutoModerator.censor(parameters.get(i));
                exp.append(parameters.get(i)).append(", ");
                i++;
            }
            event.getTextChannel().sendMessage("Censoring: " + exp).queue();
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
        commandsWithDescriptions.put("/censor (text) (text2)", "It is possible to censor multiple expressions with single command");
        return commandsWithDescriptions;
    }
}
