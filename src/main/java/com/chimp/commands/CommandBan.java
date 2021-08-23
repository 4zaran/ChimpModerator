package com.chimp.commands;

import com.chimp.services.syntax.Command;
import com.chimp.services.syntax.CommandWrapper;
import com.chimp.services.syntax.ParameterType;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Nullable;

public class CommandBan extends Command {

    public CommandBan(){
        addOption("user", ParameterType.USER, true, "Defines the user to ban");
        addOption("reason", ParameterType.STRING, false, "Defines the reason of ban");
    }

    @Override
    public String getDescription() {
        return "Used to ban memebers from server";
    }

    @Override
    public void execute(CommandWrapper wrapper) throws Exception {
        wrapper.assignOptions();

        if(wrapper.hasUnusedValues())
            reportError("Too many parameters!", wrapper);

        String reason = wrapper.getOption("reason").asString();
        Guild guild = wrapper.getGuild();
        Member target = getTarget(wrapper);

        checkUserPermissions(wrapper, target);
        checkBotPermissions(wrapper, target);

        guild.ban(target, 0, reason)
             .reason(reason)
             .queue(
                     (__) -> reportInfo("Ban was successful", wrapper),
                     (error) -> reportInfo("Could not ban %s" + error.getMessage(), wrapper)
             );
        }

    private void checkBotPermissions(CommandWrapper wrapper, Member target) throws Exception{
        Member selfMember = wrapper.getGuild().getSelfMember();
        if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.BAN_MEMBERS))
            reportError("I need permission to ban members!", wrapper);
    }

    private void checkUserPermissions(CommandWrapper wrapper, Member target) throws Exception {
        if(wrapper.isMessage()){
            Guild guild = wrapper.getGuild();
            User user = wrapper.getEvent().getAuthor();
            Member member = guild.getMember(user);

            if (!member.canInteract(target) || !member.hasPermission(Permission.BAN_MEMBERS))
                reportError("You need permission to ban members!", wrapper);
        }
    }

    @Nullable
    private Member getTarget(CommandWrapper wrapper) throws Exception {
        Member target;
        Guild guild = wrapper.getGuild();
        User userToBan = wrapper.getOption("user").asUser();

        if(userToBan == null) reportError("Target user not found!", wrapper);
        target = guild.getMember(userToBan);
        if (target == null) reportError("Target user not found!", wrapper);

        return target;
    }
}