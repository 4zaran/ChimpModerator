package com.chimp.services.syntax;

import com.chimp.services.ContextService;

import java.util.ArrayList;
import java.util.List;

/**
 * Base for all commands. Contains list of options,
 * methods to inform user if execution was successful and method for executing the command's work.
 */
public abstract class Command {
    /** List of all the options for this command. */
    protected List<Option> options = new ArrayList<>();


    // PROTECTED METHODS

    /**
     * Logs desired text to log pane from wrapper.
     * @param text to log
     * @param wrapper contains text channel on which it should be printed
     */
    protected void reportInfo(String text, CommandWrapper wrapper){
        if (wrapper.isMessage())
            wrapper.getTextChannel().sendMessage(text).queue();
        else
            ContextService.getLogger().logInfo(text, wrapper.getLogArea());
    }

    /**
     * Throws an exception with specified text.
     * If command was executed by sent message (not by console)
     * it sends back another message containing provided text.
     * @param text explaining what the error was
     * @param wrapper containing all information about message which executed the command
     * @throws Exception when something went wrong during execution
     */
    protected void reportError(String text, CommandWrapper wrapper) throws Exception {
        if (wrapper.isMessage())
            wrapper.getTextChannel().sendMessage(text).queue();
        throw new Exception(text);
    }

    /**
     * Adds an option to command.
     * @param name of the new option
     * @param type of the option
     * @param required determines if option is necessary for a command to work
     * @param description for help command
     * @return added option
     * @see Option
     */
    protected Option addOption(String name,
                               ParameterType type,
                               boolean required,
                               String description){

        name = name.toLowerCase();
        Option option = new Option(name, type, required, description);
        options.add(option);
        return option;
    }


    // ABSTRACT METHODS

    /**
     * Used for the help command. Return string containing short description of what the command does.
     * @return short description of command functionality
     */
    abstract public String getDescription();

    /**
     * Contains all the work of the command.
     * @param wrapper contains all necessary information about message and command
     * @throws Exception when something went wrong with command execution
     * @see CommandWrapper
     */
    abstract public void execute(CommandWrapper wrapper) throws Exception;


    // -- GETTERS --

    public String getSyntax(){
        StringBuilder sb = new StringBuilder();
        for (Option option : options) {
            sb.append(option.getName());
        }
        return sb.toString();
    }

    public List<String> getKeywords(){
        List<String> keywords = new ArrayList<>();
        for (Option option : options) {
            keywords.add(option.getName());
        }
        return keywords;
    }

    public Option getOption(String name){
        for (Option option : options) {
            if(option.getName().equals(name))
                return option;
        }
        return null;
    }

    public List<Option> getOptions() {
        return options;
    }
}
