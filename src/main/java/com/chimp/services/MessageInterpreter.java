package com.chimp.services;

import com.chimp.commands.Command;
import com.chimp.commands.CommandSet;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageInterpreter {
    private HashMap<String, Command> commands;

    public MessageInterpreter(){
        commands = CommandSet.getCommands();
    }

    public void handleMessage(@Nonnull MessageReceivedEvent event) {
        List<String> messageParameters = splitMessage(event.getMessage().getContentRaw());

        if (event.getMessage().getContentRaw().equals("")) return;

        if (commands.containsKey(messageParameters.get(0).toLowerCase())){
            Command c = commands.get(messageParameters.get(0).toLowerCase());
            c.execute(event, messageParameters);
            if (!messageParameters.get(0).equalsIgnoreCase("/purge")) {
                event.getMessage().delete().queue();
            }
        }
    }

//    public String[] splitMessage(String message){
//        String[] parameters = message.split("\\s+");
//        if(message.contains("\"")){
//            // Check if quote marks are even
//            int quoteCount = 0;
//            for (int i = 0; i < message.length(); i++) {
//                if (message.charAt(i) == '\"')
//                    quoteCount++;
//            }
//            if(quoteCount % 2 == 0){
//                for (String parameter : parameters) {
//                    if (parameter.contains("\"")) {
//
//                    }
//                }
//            }
//            else{
//                // exception...
//            }
//
//        }
//        return parameters;
//    }

    public List<String> splitMessage(String message){
        List<String> parameters = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(message);
        while (m.find())
            parameters.add(m.group(1).replace("\"", ""));
        return parameters;
    }

}
