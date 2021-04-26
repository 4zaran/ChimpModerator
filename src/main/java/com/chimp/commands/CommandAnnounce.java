package com.chimp.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.TreeMap;

public class CommandAnnounce implements Command{
    @Override
    public void execute(@NotNull MessageReceivedEvent event, List<String> parameters) {
        if(parameters.size() > 2)
            event.getTextChannel().sendMessage(parameters.get(1));
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public TreeMap<String, String> getSyntax() {
        return null;
    }
}
