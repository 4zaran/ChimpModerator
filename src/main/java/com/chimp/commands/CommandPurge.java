package com.chimp.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Used to mass-delete messages
 * from text channel on which the command was used.
 * <p>
 * The command is using filters to be more useful.
 */
public class CommandPurge implements Command{
    @Override
    public void execute(@NotNull MessageReceivedEvent event, List<String> parameters) {
        TextChannel channel = event.getTextChannel();
        Message commandMessage = event.getMessage();

        int amount;
        if(parameters.size() > 1 && isNumeric(parameters.get(1))) {
            // Get number from command parameters
            amount = Integer.parseInt(parameters.get(1));
            // Do not count the command message
            amount++;
            //TODO może ostrzeżenie, że nie można tak dużo?
            if(amount > 100)
                amount = 100;
        }
        else{
            // Delete 10 last messages (+ message containing command to do so)
            amount = 11;
        }
        // Get messages
        List<Message> messages = channel.getHistory().retrievePast(amount).complete();

        //Filter them if arguments are provided
        if(parameters.size() > 1){
            boolean mentionHandled = false;
            for (int i = 0; i < parameters.size(); i++) {
                if (!mentionHandled && event.getMessage().getMentionedUsers().size() > 0){
                    mentionHandled = true;
                    List<User> MentionedUsers = event.getMessage().getMentionedUsers();
                    messages.removeIf(curr -> !MentionedUsers.get(0).equals(curr.getAuthor()));
                }
                if (parameters.get(i).equalsIgnoreCase("phrase")){
                    if(parameters.size() != i + 1) {
                        int finalI = i;
                        messages.removeIf(curr -> !curr.getContentRaw().toLowerCase().contains(parameters.get(finalI+1).toLowerCase()));
                        i++;
                    }
                }
                else if (parameters.get(i).equalsIgnoreCase("equals")){
                    if(parameters.size() != i + 1) {
                        int finalI = i;
                        messages.removeIf(curr -> !curr.getContentRaw().equals(parameters.get(finalI+1)));
                        i++;
                    }
                }
                else if (parameters.get(i).equalsIgnoreCase("startsWith")){
                    if(parameters.size() != i + 1) {
                        int finalI = i;
                        messages.removeIf(curr -> !curr.getContentRaw().toLowerCase().startsWith(parameters.get(finalI+1).toLowerCase()));
                        i++;
                    }
                }
                else if (parameters.get(i).equalsIgnoreCase("endsWith")){
                    if(parameters.size() != i + 1) {
                        int finalI = i;
                        messages.removeIf(curr -> !curr.getContentRaw().toLowerCase().endsWith(parameters.get(finalI+1).toLowerCase()));
                        i++;
                    }
                }
                else if (parameters.get(i).equalsIgnoreCase("has")){
                    if(parameters.size() != i + 1) {
                        if(parameters.get(i+1).equalsIgnoreCase("embed")){
                            messages.removeIf(message -> message.getEmbeds().size() == 0);
                        }
                        else if(parameters.get(i+1).equalsIgnoreCase("file")){
                            messages.removeIf(message -> message.getAttachments().size() == 0);
                        }
                        i++;
                    }
                }
            }
        }

        // Delete message with command even when it's not matching applied filters
        if (!messages.contains(commandMessage))
            messages.add(commandMessage);

        // You can't use purgeMessages() method on 1 message
        if(messages.size() == 1) {
            messages.get(0).delete().queue();
        }
        else channel.purgeMessages(messages);
    }

    @Override
    public String getDescription() {
        return "Used to delete messages";
    }

    @Override
    public TreeMap<String, String> getSyntax() {
        TreeMap<String, String> commandsWithDescriptions= new TreeMap<>();
        commandsWithDescriptions.put("/purge", "Deletes 10 messages");
        commandsWithDescriptions.put("/purge [amount]", "Deletes specified amount of messages (max 100)");
        commandsWithDescriptions.put("/purge @user", "Deletes specified amount of messages (max 100)");
        commandsWithDescriptions.put("/purge phrase (text)", "Deletes messages containing phrase");
        commandsWithDescriptions.put("/purge has (file/embed)", "Deletes messages containing file or embed");
        commandsWithDescriptions.put("/purge equals (text)", "Deletes messages containing exact text provided");
        commandsWithDescriptions.put("/purge [amount] (other filters)", "Full command syntax. Note that you can use multiple filters in one comamnd but the `amount` field must be the first argument");
        return commandsWithDescriptions;
    }

    private static boolean isNumeric(String s) {
        if (s == null) {
            return false;
        }
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
