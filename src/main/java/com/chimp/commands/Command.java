package com.chimp.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;

public interface Command {
    void execute(@Nonnull MessageReceivedEvent event, List<String> parameters);
    @NotNull String getDescription();
    @NotNull HashMap<String, String> getSyntax();
}
