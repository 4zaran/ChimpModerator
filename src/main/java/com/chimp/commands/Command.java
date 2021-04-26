package com.chimp.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.TreeMap;

public interface Command {
    void execute(@Nonnull MessageReceivedEvent event, List<String> parameters);
    String getDescription();
    TreeMap<String, String> getSyntax();
}
