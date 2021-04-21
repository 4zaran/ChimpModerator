package com.chimp.services;

import com.chimp.commands.Command;
import com.chimp.commands.CommandSet;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class MessageInterpreter {
    private HashMap<String, Command> commands;

    public MessageInterpreter(){
        commands = CommandSet.getCommands();
    }

    public void handleMessage(@Nonnull MessageReceivedEvent event) {
        String[] messageContentParams = splitMessage(event.getMessage().getContentRaw());

        if (commands.containsKey(messageContentParams[0].toLowerCase())){
            Command c = commands.get(messageContentParams[0].toLowerCase());
            c.execute(event, messageContentParams);
            event.getMessage().delete().queue();
        }
    }

    public String[] splitMessage(String message){
        return message.split("\\s+");
    }
}
