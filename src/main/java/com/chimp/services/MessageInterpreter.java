package com.chimp.services;

import com.chimp.services.moderation.AutoModerator;
import com.chimp.services.syntax.Command;
import com.chimp.services.syntax.CommandSet;
import com.chimp.services.syntax.CommandWrapper;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles messages. Checks if a message contains executable command or if message violates any rules.
 */
public class MessageInterpreter {

    /** Commands repository. Command must be added to this map to be usable. */
    private final TreeMap<String, Command> commands;

    /** AutoModerator instance. Checks for violations and executes punishments. */
    private final AutoModerator autoModerator;

    public MessageInterpreter(){
        commands = CommandSet.getCommands();
        autoModerator = new AutoModerator();
    }

    /** Handles messages. This method is executed when messages are sent from main window. */
    public void handleMessage(String message, String logArea){
        List<String> messageParameters = splitMessage(message);
        CommandWrapper wrapper = new CommandWrapper(messageParameters, logArea);

        if(message.startsWith(ContextService.getPrefix()))
            searchAndExecute(wrapper);
    }

    /** Handles messages. This method is executed when a message is received. */
    public void handleMessage(@Nonnull MessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw();

        Member self = event.getGuild().getSelfMember();
        Member author = event.getMessage().getMember();

        // Check for violation
        // Bots are not violating, right?
        // TODO VIOLATION IN COMMANDS
        assert author != null;
        if (AutoModerator.isEnabled(event.getGuild()) && !author.equals(self))
            autoModerator.checkViolation(event);

        if(content.startsWith(ContextService.getPrefix())) {
            List<String> messageParameters = splitMessage(content);
            CommandWrapper wrapper = new CommandWrapper(event, messageParameters);
            searchAndExecute(wrapper);
        }
    }

    /**
     * Searches for a command and executes it.
     * @param wrapper object containing all information about message
     */
    public void searchAndExecute(CommandWrapper wrapper) {
        // Search for command and execute it
        if (commands.containsKey(wrapper.getCommandName())) {
            Command c = commands.get(wrapper.getCommandName());
            try {
                c.execute(wrapper);
            } catch (Exception e) {
                String error;
                if (e.getMessage() == null)
                    error = "Cause: " + e.getCause() + ", " + e.getLocalizedMessage();
                else error = e.getMessage();
                ContextService.getLogger().logError(error, wrapper.getLogArea());
            }
        } else {
            String error = "Command \"" + wrapper.getCommandName() + "\" not found!";
            if(wrapper.isMessage()) wrapper.getTextChannel().sendMessage(error).queue();
            else ContextService.getLogger().logError(error, wrapper.getLogArea());
        }
        // Delete message containing command
        if (wrapper.isMessage())
            wrapper.getEvent().getMessage().delete().queue();
    }

    /**
     * Splits message into list of strings.
     * Message is divided into single words OR groped together with quotation sign (").
     * Note that quotation signs are deleted.
     * @param message string containing whole message
     * @return list containing all expressions from message
     */
    public List<String> splitMessage(String message){
        List<String> parameters = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(message);
        while (m.find())
            parameters.add(m.group(1).replace("\"", ""));
        return parameters;
    }

    /**
     * Return how many commands are in the command repository.
     * @return string telling command count
     */
    public String getCommandCount(){
        return ("Loaded " + commands.size() + " valid commands.");
    }
}