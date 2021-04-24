package com.chimp.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class CommandPurge implements Command{
    @Override
    public void execute(@NotNull MessageReceivedEvent event, List<String> parameters) {
        JDA jda = event.getJDA();
        TextChannel channel = jda.getTextChannelsByName("general", true).get(0);
        Message commandMessage = event.getMessage();

        int amount;
        if(parameters.size() > 1 && isNumeric(parameters.get(1))) {
            // Get number from command parameters
            amount = Integer.parseInt(parameters.get(1));
            // Do not count the command message
            amount++;
        }
        else{
            // Delete 10 last messages (+ message containing command to do so)
            amount = 11;
        }
        // Get messages
        List<Message> messages = channel.getHistory().retrievePast(amount).complete();

        //Filter them if arguments are provided
        if(parameters.size() > 1){
            boolean mentioned = false;
            for (int i = 0; i < parameters.size(); i++) {
                if (!mentioned && event.getMessage().getMentionedUsers().size() > 0){
                    mentioned = true;
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
                else if (parameters.get(i).equalsIgnoreCase("startswith")){
                    if(parameters.size() != i + 1) {
                        int finalI = i;
                        messages.removeIf(curr -> !curr.getContentRaw().toLowerCase().startsWith(parameters.get(finalI+1).toLowerCase()));
                        i++;
                    }
                }
                else if (parameters.get(i).equalsIgnoreCase("endswith")){
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

        if (messages.isEmpty())
            return;
        if(messages.size() == 1) {
            messages.get(0).delete().queue();
        }
        else channel.purgeMessages(messages);
        if(commandMessage != null)
            commandMessage.delete().queue();
    }

    @Override
    public @NotNull String getDescription() {
        return "Used to delete messages";
    }

    @Override
    public @NotNull HashMap<String, String> getSyntax() {
        HashMap<String, String> commandsWithDescriptions= new HashMap<>();
        commandsWithDescriptions.put("/purge", "Deletes 10 messages");
        commandsWithDescriptions.put("/purge [count]", "Deletes specified amount of messages (max 100)");
        commandsWithDescriptions.put("/purge @user", "Deletes specified amount of messages (max 100)");
        commandsWithDescriptions.put("/purge phrase (text)", "Deletes messages containing phrase");
        commandsWithDescriptions.put("/purge has (file/embed)", "Deletes messages containing file or embed");
        commandsWithDescriptions.put("/purge [count] [@user] [phrase (text)] [has (file / embed)]", "Full command syntax");
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
