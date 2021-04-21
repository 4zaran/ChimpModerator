package com.chimp.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class CommandKick implements Command{

    @Override
    public void execute(@NotNull MessageReceivedEvent event) {
        Message msg = event.getMessage();
        Member member = event.getMember();
        String messageContent = msg.getContentRaw();
        MessageChannel channel = event.getChannel();

        String[] messageContentParams = messageContent.split("\\s+");

        if (msg.getMentionedMembers().isEmpty()){
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
        channel.sendMessage("Reason: " + reason).queue();

        Member target = msg.getMentionedMembers().get(0);
        if (!member.canInteract(target) || !member.hasPermission(Permission.KICK_MEMBERS)) {
            channel.sendMessage("You need permission to kick!").queue();
            return;
        }

        final Member selfMember = event.getGuild().getSelfMember();

        if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.KICK_MEMBERS)) {
            event.getChannel().sendMessage("I don't have permission to kick!").queue();
            return;
        }


        event.getGuild()
                .kick(target, reason.toString())
                .reason(reason.toString())
                .queue(
                        (__) -> event.getChannel().sendMessage("Kick was successful").queue(),
                        (error) -> event.getChannel().sendMessageFormat("Could not kick %s", error.getMessage()).queue()
                );
    }

    @Override
    public String getDescription() {
        return "Used to kick user from server";
    }
}
