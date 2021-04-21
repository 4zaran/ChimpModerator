package com.chimp.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class CommandPurge implements Command{
    @Override
    public void execute(@NotNull MessageReceivedEvent event, String[] parameters) {
        JDA jda = event.getJDA();
        TextChannel channel = jda.getTextChannelsByName("general", true).get(0);
        int amount;
        if(parameters.length > 1)
            amount = Integer.parseInt(parameters[1]);
        else
            amount = 2;
        //if amount 1 -> delete zamiast purge
        System.out.println(channel.getHistory().retrievePast(amount).complete());
        channel.purgeMessages(channel.getHistory().retrievePast(amount).complete());
        //channel.deleteMessages(channel.mess).queue();
    }

    @Override
    public @NotNull String getDescription() {
        return "Used to delete messages";
    }
}
