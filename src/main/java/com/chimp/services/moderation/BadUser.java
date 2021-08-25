package com.chimp.services.moderation;

import com.chimp.services.json.serializers.BadUserSerializer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.text.MessageFormat;

import static com.chimp.services.ContextService.getInterpreter;
import static com.chimp.services.ContextService.getPrefix;

/**
 * Describes a user that violated the rules.
 * Every time that another user violates for the first time, BadUser's object is created for this user.
 * Holds current state of the violations.
 */
@JsonSerialize(using = BadUserSerializer.class)
public class BadUser {
    /** Defines in what state the user is in.
     * @see BehaviourState
     */
    private BehaviourState state;

    /**
     * Defines how many times the user violated in current state.
     * With every state change this number is changed to 0.
     * <p>For example,
     * with state {@code WARNED} and {@code violationAmount = 2} will mean that user has been warned two times.
     */
    private int violationAmount;

    /** Server's member that has violated. */
    private final Member member;

    public BadUser(Member member){
        this.member = member;
        this.violationAmount = 0;
        state = BehaviourState.WARNED;
    }

    public BadUser(Member member, BehaviourState state, int violationAmount){
        this.member = member;
        this.violationAmount = violationAmount;
        this.state = state;
    }

    public void hasViolated(TextChannel textChannel){
        Guild guild = member.getGuild();
        while (true) {
            if (state == BehaviourState.WARNED && violationAmount >= AutoModerator.getWarnAmount(guild))
                nextPunishment();
            else if (state == BehaviourState.KICKED && violationAmount >= AutoModerator.getKickAmount(guild)) {
                nextPunishment();
            }
            else break;
        }
        punishment(textChannel);
    }

    public void punishment(TextChannel textChannel){
        violationAmount++;
        Guild guild = textChannel.getGuild();
        if(state == BehaviourState.WARNED){
            textChannel.sendMessage(
                    String.format("**Warning** <@!%s>! You have violated the rules!\n" +
                                    "Warnings issued: %d\n" +
                                    "Getting more than %d warnings will cause a kick or ban from the server!",
                            member.getId(),
                            violationAmount,
                            AutoModerator.getWarnAmount(guild))).queue();
        }
        if(state == BehaviourState.KICKED){
            textChannel.sendMessage(
                    String.format("**Kicking** <@!%s> for violating the rules.\n" +
                                    "Kicks issued: %d\n" +
                                    "Getting more than %d kicks will cause ban from the server!",
                            member.getId(),
                            violationAmount,
                            AutoModerator.getKickAmount(guild))).queue();
            executeCommand("kick", textChannel);
        }
        if(state == BehaviourState.BANNED){
            textChannel.sendMessage(
                    String.format("**Banning** <@!%s> for violating the rules. \n" +
                                    "Bans issued: %d",
                            member.getId(),
                            violationAmount)).queue();
            executeCommand("ban", textChannel);
        }
    }

    public void nextPunishment(){
        if(state == BehaviourState.CIVIL) {
            state = BehaviourState.WARNED;
            violationAmount = 0;
        }
        if(state == BehaviourState.WARNED) {
            state = BehaviourState.KICKED;
            violationAmount = 0;
        }
        else if (state == BehaviourState.KICKED){
            //ban
            state = BehaviourState.BANNED;
            violationAmount = 0;
        }
    }

    public Member getMember() {
        return member;
    }

    private void executeCommand(String commandName, TextChannel textChannel) {
        String command = MessageFormat.format("{0}{1} <@!{2}> \"Violation of the rules\"",
                getPrefix(),
                commandName,
                member.getId());
        String logArea = MessageFormat.format("{0}@{1}", textChannel.getId(), textChannel.getGuild().getId());
        getInterpreter().handleMessage(command, logArea);
    }

    public BehaviourState getState() {
        return state;
    }

    public int getViolationAmount() {
        return violationAmount;
    }
}
