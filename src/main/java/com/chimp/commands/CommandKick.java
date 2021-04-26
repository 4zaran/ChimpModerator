package com.chimp.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

public class CommandKick implements Command{
    @Override
    public void execute(@NotNull MessageReceivedEvent event, List<String> parameters) {
        Message msg = event.getMessage();
        Member member = event.getMember();
        String reason;
        MessageChannel channel = event.getChannel();

        if (msg.getMentionedMembers().isEmpty()) {
            channel.sendMessage("Missing user to kick!").queue();
        } else {
            if (parameters.size() < 3)
                reason = "No reason provided";
            else
                reason = parameters.get(2);
            Member target = msg.getMentionedMembers().get(0);
            if (!Objects.requireNonNull(member).canInteract(target) || !member.hasPermission(Permission.KICK_MEMBERS)) {
                channel.sendMessage("You need permission to kick!").queue();
            } else {
                final Member selfMember = event.getGuild().getSelfMember();
                if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.KICK_MEMBERS)) {
                    event.getChannel().sendMessage("I don't have permission to kick!").queue();
                } else {
                    event.getGuild()
                            .kick(target, reason)
                            .reason(reason)
                            .queue(
                                    (__) -> event.getChannel().sendMessage("Kick was successful").queue(),
                                    (error) -> event.getChannel().sendMessageFormat("Could not kick %s", error.getMessage()).queue()
                            );
                }
            }
        }


    }

    @Override
    public String getDescription() {
        return "Used to kick user from server";
    }

    @Override
    public TreeMap<String, String> getSyntax() {
        TreeMap<String, String> commandsWithDescriptions= new TreeMap<>();
        commandsWithDescriptions.put("/kick @user \"Reason of kick\"", "Kicks user from server with specifed reason");
        commandsWithDescriptions.put("/kick @user", "Kicks user from server");
        return commandsWithDescriptions;
    }
}
