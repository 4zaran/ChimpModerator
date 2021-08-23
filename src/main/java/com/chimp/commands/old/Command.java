package com.chimp.commands.old;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.TreeMap;

/**
 * @deprecated because it could handle only messages already sent to discord.
 *
 * Replaced with {@link com.chimp.services.syntax.Command}
 */
public interface Command {
    void execute(@Nonnull MessageReceivedEvent event, List<String> parameters);
    String getDescription();
    TreeMap<String, String> getSyntax();
}
