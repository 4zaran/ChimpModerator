package com.chimp.commands;

import com.chimp.services.AutoModerator;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.TreeMap;

public class CommandCensor implements Command{

    @Override
    public void execute(@NotNull MessageReceivedEvent event, List<String> parameters) {
        if(parameters.size() >= 2){
            int i = 1;
            StringBuilder ans = new StringBuilder();
            StringBuilder exp = new StringBuilder();
            StringBuilder ext = new StringBuilder();
            while (i < parameters.size()) {
                //TODO error on already censored
                if (AutoModerator.censor(parameters.get(i))){
                    if(!exp.toString().isEmpty())
                        exp.append(", ").append(parameters.get(i));
                    else
                        exp.append(parameters.get(i));
                }
                else{
                    if(!ext.toString().isEmpty())
                        ext.append(", ").append(parameters.get(i));
                    else
                        ext.append(parameters.get(i));
                }
                i++;
            }
            if(!exp.toString().isEmpty())
                ans.append("Censoring: ").append(exp).append('\n');
            if(!ext.toString().isEmpty())
                ans.append("Already censored: ").append(ext);
            event.getTextChannel().sendMessage(ans).queue();
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
        commandsWithDescriptions.put("/censor (text)", "Censors given string");
        commandsWithDescriptions.put("/censor (text) (text2)", "It is possible to censor multiple expressions with single command");
        return commandsWithDescriptions;
    }
}
