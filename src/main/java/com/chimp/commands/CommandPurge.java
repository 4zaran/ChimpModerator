package com.chimp.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class CommandPurge implements Command{
    @Override
    public void execute(@NotNull MessageReceivedEvent event, List<String> parameters) {
        JDA jda = event.getJDA();
        TextChannel channel = jda.getTextChannelsByName("general", true).get(0);

        int amount;
        if(parameters.size() > 1) {
            amount = Integer.parseInt(parameters.get(1));
            // Do not count the command message
            amount++;
        }
        else
            amount = 11;

        System.out.println(channel.getHistory().retrievePast(amount).complete());
        if(amount == 1) {
            List<Message> lastMessage = channel.getHistory().retrievePast(1).complete();
            if (!lastMessage.isEmpty()) {
                lastMessage.get(0).delete().queue();
            }
        }
        else channel.purgeMessages(channel.getHistory().retrievePast(amount).complete());
    }

    @Override
    public @NotNull String getDescription() {
        return "Used to delete messages";
    }

    @Override
    public @NotNull HashMap<String, String> getSyntax() {
        HashMap<String, String> commandsWithDescriptions= new HashMap<>();
        commandsWithDescriptions.put("/purge", "Deletes 10 messages");
        commandsWithDescriptions.put("/purge [count]", "Deletes specified amount of messages (max 100)");
        return commandsWithDescriptions;
    }
}
