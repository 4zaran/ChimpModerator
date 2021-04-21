package com.chimp.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class CommandBan implements Command{
    @Override
    public void execute(@NotNull MessageReceivedEvent event, String[] parameters) {
        Message msg = event.getMessage();
        Member member = event.getMember();
        String messageContent = msg.getContentRaw();
        MessageChannel channel = event.getChannel();

        String[] messageContentParams = messageContent.split("\\s+");

        if (msg.getMentionedMembers().isEmpty()){
            channel.sendMessage("Missing user to ban!").queue();
            return;
        }
        if(messageContentParams.length < 3) {
            channel.sendMessage("Missing reason!").queue();
            return;
        }

        StringBuilder reason = new StringBuilder();
        for(int i = 2; i < messageContentParams.length; i++){
            reason.append(messageContentParams[i]).append(" ");
        }

        Member target = msg.getMentionedMembers().get(0);
        if (!member.canInteract(target) || !member.hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("You need permission to ban members!").queue();
            return;
        }

        final Member selfMember = event.getGuild().getSelfMember();

        if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.BAN_MEMBERS)) {
            event.getChannel().sendMessage("I don't have permission to ban members!").queue();
            return;
        }


        event.getGuild()
                .ban(target,0, reason.toString())
                .reason(reason.toString())
                .queue(
                        (__) -> event.getChannel().sendMessage("Ban was successful").queue(),
                        (error) -> event.getChannel().sendMessageFormat("Could not ban %s", error.getMessage()).queue()
                );
    }

    @Override
    public @NotNull String getDescription() {
        return "Used to ban user from server";
    }
}
