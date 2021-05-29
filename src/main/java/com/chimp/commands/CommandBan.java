package com.chimp.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

public class CommandBan implements Command{
    @Override
    public void execute(@NotNull MessageReceivedEvent event, List<String> parameters) {
        Message msg = event.getMessage();
        Member member = event.getMember();
        MessageChannel channel = event.getChannel();
        String reason;

        if (msg.getMentionedMembers().isEmpty()) {
            channel.sendMessage("Missing user to ban!").queue();
        } else {
            if (parameters.size() < 3)
                reason = "No reason provided";
            else
                reason = parameters.get(2);
            Member target = msg.getMentionedMembers().get(0);
            if (!Objects.requireNonNull(member).canInteract(target) || !member.hasPermission(Permission.BAN_MEMBERS)) {
                channel.sendMessage("You need permission to ban members!").queue();
            } else {
                final Member selfMember = event.getGuild().getSelfMember();
                if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.BAN_MEMBERS)) {
                    event.getChannel().sendMessage("I don't have permission to ban members!").queue();
                } else {
                    event.getGuild()
                            .ban(target, 0, reason)
                            .reason(reason)
                            .queue(
                                    (__) -> event.getChannel().sendMessage("Ban was successful").queue(),
                                    (error) -> event.getChannel().sendMessageFormat("Could not ban %s", error.getMessage()).queue()
                            );
                }
            }
        }

    }

    @Override
    public String getDescription() {
        return "Used to ban user from server";
    }

    @Override
    public TreeMap<String, String> getSyntax() {
        TreeMap<String, String> commandsWithDescriptions= new TreeMap<>();
        commandsWithDescriptions.put("/ban @user \"reason of ban\"", "Bans specified user with specified reason");
        commandsWithDescriptions.put("/ban @user", "Bans specified user");
        return commandsWithDescriptions;
    }
}