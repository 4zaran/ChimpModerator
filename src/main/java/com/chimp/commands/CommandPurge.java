package com.chimp.commands;

import com.chimp.services.syntax.Command;
import com.chimp.services.syntax.CommandWrapper;
import com.chimp.services.syntax.ParameterType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Used to mass-delete messages
 * from text channel on which the command was used.
 * <p>
 * The command is using filters to be more useful.
 */
public class CommandPurge extends Command {
    private List<Message> messages;
    CommandWrapper wrapper;

    public CommandPurge(){
        addOption("silent",
                ParameterType.KEYWORD,
                false,
                "With this keyword, there will be no confirmation after successful operation");
        addOption("amount",
                ParameterType.KEYWORD,
                false,
                "Defines the amount of messages to check.")
                .addValue("amount_value",
                        ParameterType.INTEGER,
                        true,
                        "Takes the integer value");

        addOption("contains",
                ParameterType.KEYWORD,
                false,
                "Defines the messages containing text specified in option")
                .addValue("value",
                        ParameterType.ANY,
                        true,
                        "Takes any text.");

        addOption("equals",
                ParameterType.KEYWORD,
                false,
                "Checks if content of messages is equal to the text specified. " +
                        "Note that this is the only option in purge command that is case sensitive.")
                .addValue("value",
                        ParameterType.ANY,
                        true,
                        "Takes any text.");

        addOption("startswith",
                ParameterType.KEYWORD,
                false,
                "Checks if content of messages starts with text provided.")
                .addValue("value",
                        ParameterType.ANY,
                        true,
                        "Takes any text.");

        addOption("endswith",
                ParameterType.KEYWORD,
                false,
                "Checks if content of messages ends with text provided.")
                .addValue("value",
                        ParameterType.ANY,
                        true,
                        "Takes any text");

        addOption("from",
                ParameterType.KEYWORD,
                false,
                "Checks if message was sent by specified user.")
                .addValue("value",
                        ParameterType.USER,
                        true,
                        "Takes an user. " +
                                "The format of the accepted input: \n" +
                                " - normal mentions\n" +
                                " - <@USERID> or <@!USERID>\n" +
                                " - @USERNAME#1234 (NOTE: This method will return user only when it's already in cache)");

        addOption("has",
                ParameterType.KEYWORD,
                false,
                "Checks if message has something besides normal text")
                .addValue("value",
                        ParameterType.STRING,
                        true,
                        "Currently accepted values:\n" +
                                " - file\n" +
                                " - embed\n" +
                                " - mentions\n" +
                                " - invites\n" +
                                " - reactions\n");
    }

    @Override
    public String getDescription() {
        return "Used to delete messages";
    }

    @Override
    public void execute(CommandWrapper wrapper) throws Exception {
        this.wrapper = wrapper;
        wrapper.assignOptions();

        if(wrapper.hasUnusedValues())
            reportError("Too many parameters!", wrapper);
        if(wrapper.hasNoOptions())
            reportError("No options provided! Use help to see available options.", wrapper);

        boolean silent = wrapper.getOption("silent").asBoolFromKeyword();
        TextChannel channel = wrapper.getTextChannel();

        int amount = getAmount(wrapper);
        messages = channel.getHistory().retrievePast(amount).complete();

        Iterator<Map.Entry<String, String>> it = wrapper.getOptions().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, String> pair = it.next();
            removeMessagesIf( pair.getKey(), pair.getValue());
            it.remove();
        }

        // Don't delete message with command
        if(wrapper.isMessage())
            messages.remove(wrapper.getEvent().getMessage());

        if (messages.size() == 1) {
            // You can't use purgeMessages() method on 1 message
            if(!silent) reportInfo("Deleting 1 message...", wrapper);
            messages.get(0).delete().queue();
        }
        else{
            if (!silent) reportInfo(String.format("Deleting %d messages...", messages.size()), wrapper);
            channel.purgeMessages(messages);
        }
    }

    @NotNull
    private Integer getAmount(CommandWrapper wrapper) throws Exception {
        Integer amount = wrapper.getOption("amount").asInteger();

        if(amount == null) amount = 10;
        else if (amount < 1) reportError("Amount can't be less than 1", wrapper);
        else if (amount > 100) reportError("Amount is limited to 100!", wrapper);

        if(wrapper.isMessage() && amount < 100)
            amount += 1; // Message containing command

        return amount;
    }

    private void removeMessagesIf(String keyword, String value) throws Exception {
        // Only equals pays attention to case size
        if(!keyword.equals("equals"))
            value = value.toLowerCase();

        String finalValue = value;

        switch (keyword){
            case "contains":
                messages.removeIf(message -> !message.getContentRaw().toLowerCase().contains(finalValue));
                break;
            case "equals":
                messages.removeIf(message -> !message.getContentRaw().equals(finalValue));
                break;
            case "startswith":
                messages.removeIf(message -> !message.getContentRaw().toLowerCase().startsWith(finalValue));
                break;
            case "endswith":
                messages.removeIf(message -> !message.getContentRaw().toLowerCase().endsWith(finalValue));
                break;
            case "from":
                User user = wrapper.getOption("from").asUser();
                messages.removeIf(message -> !message.getAuthor().equals(user));
                break;
            case "has":
                switch (value) {
                    case "embed":
                        messages.removeIf(message -> message.getEmbeds().size() == 0);
                        break;
                    case "file":
                        messages.removeIf(message -> message.getAttachments().size() == 0);
                        break;
                    case "mentions":
                        messages.removeIf(message -> message.getMentions().size() == 0);
                        break;
                    case "invites":
                        messages.removeIf(message -> message.getInvites().size() == 0);
                        break;
                    case "reactions":
                        messages.removeIf(message -> message.getReactions().size() == 0);
                        break;
                    default:
                        reportError("Unkown option \"" + value + "\"", wrapper);
                        break;
                }
                break;
            case "amount":  // fall through
            case "silent":
                break;
            default:
                reportError("Unkown keyword \"" + value + "\"", wrapper);
                break;
        }
    }
}
