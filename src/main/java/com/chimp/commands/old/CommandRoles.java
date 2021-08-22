package com.chimp.commands.old;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.TreeMap;

/**
 * @deprecated not in use
 */
public class CommandRoles implements Command {
    @Override
    public void execute(@NotNull MessageReceivedEvent event, List<String> parameters) {
        StringBuilder availableRoles = new StringBuilder();
        List<Role> roles = event.getGuild().getRoles();
        EmbedBuilder eb = new EmbedBuilder();

        if( parameters.size() <= 2){
            if(parameters.size() == 1){
                eb.setTitle("Available roles on this server");
                for (Role role: roles) {
                    availableRoles.append(" - ").append(role.getName()).append("\n");
                }
                eb.setDescription(availableRoles);
            }
            else if(parameters.size() == 2){
                List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
                if(mentionedMembers.size() == 1){
                    eb.setTitle("Roles assigned to " + mentionedMembers.get(0).getEffectiveName());
                    List<Role> userRoles = mentionedMembers.get(0).getRoles();
                    availableRoles = new StringBuilder();
                    for (Role role: userRoles) {
                        availableRoles.append(role.getName()).append("\n");
                    }
                    eb.setDescription(availableRoles);
                }
            }
            if(availableRoles.toString().isEmpty())
                eb.setDescription("**No assigned roles!**");
            eb.setColor(new Color(21, 220, 64, 255));
            event.getTextChannel().sendMessage(eb.build()).queue();
        }
        else {
            event.getTextChannel().sendMessage("Invalid syntax").queue();
        }
    }

    @Override
    public String getDescription() {
        return "Displays all roles";
    }

    @Override
    public TreeMap<String, String> getSyntax() {
        TreeMap<String, String> commandsWithDescriptions= new TreeMap<>();
        commandsWithDescriptions.put("/roles", "Displays list of roles");
        commandsWithDescriptions.put("/roles @user", "Displays list of roles of specified user");
        return commandsWithDescriptions;
    }
}
