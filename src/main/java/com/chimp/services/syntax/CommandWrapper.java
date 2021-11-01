package com.chimp.services.syntax;

import com.chimp.services.ContextService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chimp.services.ContextService.getPrefix;

/**
 * Wraps everything about message containing command.
 * Helps with command execution by assigning  {@code parameters} to command's options.
 * <p>Supports two types of messages: <ul>
 *     <li>messages already sent to discord
 *     <li>messages written in main window
 *     (if such message contains executable command it'll not be sent, only executed)</li>
 * </ul>
 * Thanks to this wrapper it is possible to execute same operations from window and by sending a message to channel.
 */
public class CommandWrapper {
    /**
     * Base for whole class.
     * Contains message executing the command split into list of parameters.
     */
    private List<String> parameters;

    /** Used if a message was received. Contains all information about message like text channel, guild, author etc. */
    private final MessageReceivedEvent event;

    /** Specifies which area command applies to i.e. where every info should be printed. */
    private final String logArea;

    /** Holds name of the command (e.g. "/help" for help command). */
    private String commandName;

    /** True if the wrapper refers to message already sent to text channel. */
    private final boolean isMessage;

    /** Holds the text channel on which command has been used. */
    private final TextChannel textChannel;

    /** List specifying the types of {@code parameters}. */
    private List<ParameterType> parameterTypes;

    /** Map containing option's name as key, and it's value as value.
     * If option is a keyword without value, then value will be set to empty string.
     * <ul>For example, {@link com.chimp.commands.CommandConfig} has an option "warn" which type is {@code ParameterType.KEYWORD}
     *      <li>using {@code /config warn} will set the value of said option to empty string,
     *          which means that it has been found but without any value</li>
     *      <li>using {@code /config warn 5} will set the value  in this map to "5"</li>
     * </ul>
     * */
    private HashMap<String, String> optionAndValue;


    // CONSTRUCTORS

    /**
     * Used when message was received from server.
     * @param event of receiving a message
     * @param parameters    {@link #parameters}
     */
    public CommandWrapper(@NotNull MessageReceivedEvent event, @NotNull List<String> parameters){
        setFields(parameters);

        this.isMessage = true;
        this.event = event;
        // TODO CREATE PARSER FOR THIS
        this.logArea = event.getTextChannel().getId() + "@" + event.getGuild().getId();

        this.textChannel = event.getTextChannel();
    }

    /**
     * Used when message was written in app's window.
     * @param parameters {@link #parameters}
     * @param logArea determines to which log area / text channel the message is related to
     */
    public CommandWrapper(@NotNull List<String> parameters, String logArea){
        setFields(parameters);

        this.isMessage = false;
        this.event = null;
        this.logArea = logArea;

        JDA jda = ContextService.getJdaService().getJda();
        String channelId = getTextChannelIdFromLogAreaString(logArea);
        assert channelId != null;
        this.textChannel = jda.getTextChannelById(channelId);
    }


    // -- PUBLIC METHODS --

    /**
     * Puts the option as a key to the map with the given value
     * @param value value of the option
     * @param option key
     */
    public void assignValueToOption(String value, String option){
        this.optionAndValue.put(option, value);
    }

    /**
     * First, tries to guess the type of every given parameter.
     * Then assigns those parameters to command's options.
     * @throws Exception when a required options / values were not found
     */
    public void assignOptions() throws Exception{
        // Command which is executed
        Command command = getCommand();

        // List containing divided message
        List<String> parameters = getParameters();

        // List determining types of values in parameters list
        parameterTypes = new ArrayList<>();

        // Try to assign a type for each parameter
        for (String parameter : parameters) {
            ParameterType type = ParameterParser.matchType(parameter, command.getKeywords());
            parameterTypes.add(type);
        }

        // Try to search for defined option in executed command
        for (Option option : command.options) {
            // In this case, option needs additional value
            // So it needs to search for a pair of parameters
            if(option.getType() == ParameterType.KEYWORD){
                // TODO MAKE SURE THAT ALL OPTIONS HAVE UNIQUE NAMES
                int keywordIndex = parameters.indexOf(option.getName());
                if(keywordIndex == -1){
                    if(option.isRequired()) {
                        String error = "Required option (" + option.getName() + ") not found! ";
                        command.reportError(error, this);
                    }
                }
                else if(option.getValue() != null) {
                    if (parameterTypes.size() > keywordIndex + 1
                            && checkType(parameterTypes.get(keywordIndex + 1), option.getValue().getType())){
                        // Mark as used
                        // parameterTypes.set(keywordIndex, ParameterType.ALREADY_USED);
                        useValue(keywordIndex + 1, parameters.get(keywordIndex + 1), option.getName());
                    }
                    else {
                        if (option.getValue().isRequired()) {
                            String error = "Required value for option (" + option.getName() + ") not found! ";
                            command.reportError(error, this);
                        }
                        else // useValue(keywordIndex, "", option.getName());
                            assignValueToOption("", option.getName());
                    }
                }
                else //useValue(keywordIndex, "", option.getName());
                    assignValueToOption("", option.getName());
            }
            else{
                boolean assigned = false;

                for (ParameterType parameterType : parameterTypes) {
                    if(parameterType == ParameterType.ALREADY_USED)
                        continue;

                    if(checkType(parameterType, option.getType())){
                        int index = parameterTypes.indexOf(parameterType);
                        useValue(index, parameters.get(index), option.getName());
                        assigned = true;
                        break;
                    }
                }
                if(!assigned && option.isRequired()){
                    String error = "Required option (" + option.getName() + ") not found! ";
                    command.reportError(error, this);
                }
            }
        }
    }

    /**
     * Marks parameter with specified index as used and assigns a value to the option.
     * @param parameterIndx     index of the parameter to mark as used
     * @param value             value to assign
     * @param optionName        option to assign to
     */
    private void useValue(int parameterIndx, String value, String optionName) {
        parameterTypes.set(parameterIndx, ParameterType.ALREADY_USED);

        assignValueToOption(value, optionName);
    }

    /**
     * Tries to find next value for specified option.
     * In more detail, goes through the parameters
     * searching for not used parameter with same type as the option's.
     * Useful for extracting multiple strings with one option.
     * @param optionName option for which the value will be searched
     * @return found value or null
     */
    public OptionConverter fetchNextValue(String optionName){
        Option option = getCommand().getOption(optionName);
        // Start from current value
        String currentValue = optionAndValue.get(optionName);
        int i = parameters.indexOf(currentValue);
        // Skipping first because it's already used
        for(i += 1 ; i < parameterTypes.size(); i++){
            // Went to another keyword, so next values don't apply to this one anymore.
            if(parameterTypes.get(i) == ParameterType.KEYWORD)
                break;
            // Found it!
            ParameterType optionType = option.getType();
            // Take value's type
            if(optionType == ParameterType.KEYWORD && option.hasValue())
                optionType = option.getValue().getType();
            if(checkType(parameterTypes.get(i), optionType)){
                parameterTypes.set(i, ParameterType.ALREADY_USED);
                optionAndValue.replace(optionName, parameters.get(i));
                return new OptionConverter(parameters.get(i));
            }
        }
        return null;
    }

    /**
     * Checks if any of the parameters are not in use.
     * It helps when user gave too many arguments.
     * @return true when unused parameter was found
     */
    public boolean hasUnusedValues(){
        for (ParameterType type : parameterTypes) {
            if(type != ParameterType.ALREADY_USED && type != ParameterType.KEYWORD){
                return true;
            }
        }
        return false;
    }


    // -- PRIVATE METHODS

    private void setFields(@NotNull List<String> parameters) {
        this.parameters = parameters;
        this.optionAndValue = new HashMap<>();
        String c = parameters.get(0);
        c = c.replaceFirst(getPrefix(), "");
        this.commandName = c;
        parameters.remove(0);
    }

    /**
     * Checks if both types are equal OR the option type is ParameterType.ANY - then any type is accepted
     * @param typeToCheck current parameter's type
     * @param optionsType option which this parameter relates to
     * @return true if this type can be used
     */
    private boolean checkType(ParameterType typeToCheck, ParameterType optionsType) {
        return typeToCheck != ParameterType.ALREADY_USED && typeToCheck == optionsType || optionsType == ParameterType.ANY;
    }

    /**
     * Extrudes a channel id from log area code
     * @param logArea containing channelID@guildID
     * @return channel id
     */
    private @Nullable String getTextChannelIdFromLogAreaString(String logArea){
        Pattern p = Pattern.compile("([0-9]+)@([0-9]+)");
        Matcher m = p.matcher(logArea);
        if(m.matches())
            return m.group(1);
        return null;
    }

    /**
     * Extrudes a guild id from log area code
     * @param logArea containing channelID@guildID
     * @return guild id
     */
    private @Nullable String getGuildIdFromLogAreaString(String logArea){
        Pattern p = Pattern.compile("([0-9]+)@([0-9]+)");
        Matcher m = p.matcher(logArea);
        if(m.matches())
            return m.group(1);
        return null;
    }

    // -- GETTERS --

    public MessageReceivedEvent getEvent(){
        return event;
    }

    public boolean isMessage() {
        return isMessage;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public Guild getGuild(){
        return textChannel.getGuild();
    }

    public String getLogArea() {
        return logArea;
    }

    public String getCommandName(){
        return commandName;
    }

    private Command getCommand(){
        return CommandSet.getCommands().get(getCommandName());
    }

    public OptionConverter getOption(String optionName){
        return new OptionConverter(optionAndValue.getOrDefault(optionName, null));
    }

    public HashMap<String, String> getOptions(){
        return this.optionAndValue;
    }

    public boolean isOptionPresent(String optionName){
        return optionAndValue.containsKey(optionName);
    }

    public boolean hasNoOptions(){
        return getOptions().size() == 0;
    }
}
