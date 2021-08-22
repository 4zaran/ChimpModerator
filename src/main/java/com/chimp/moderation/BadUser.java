package com.chimp.moderation;

import com.chimp.services.AutoModerator;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * Describes a user that violated the rules.
 * Every time that another user violates for the first time, BadUser's object is created for this user.
 * Holds current state of the violations.
 */
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

    /** User that has violated. */
    private final User user;
    // TODO: 22.08.2021 change this to member, so violations will be separated for all guilds.

    public BadUser(User user){
        this.user = user;
        this.violationAmount = 0;
        state = BehaviourState.WARNED;
    }

    public void hasViolated(TextChannel textChannel){
        while (true) {
            if (state == BehaviourState.WARNED && violationAmount >= AutoModerator.getWarnAmount())
                nextPunishment(textChannel);
            else if (state == BehaviourState.KICKED && violationAmount >= AutoModerator.getKickAmount()) {
                nextPunishment(textChannel);
            }
            else{
                break;
            }
        }
        punishment(textChannel);
    }

    public void punishment(TextChannel textChannel){
        violationAmount++;
        if(state == BehaviourState.WARNED){
            textChannel.sendMessage(
                    String.format("**Warning** <@!%s>! You have violated the rules!\n" +
                                    "Warnings issued: %d\n" +
                                    "Getting more than %d warnings will cause a kick or ban from the server!",
                            user.getId(),
                            violationAmount,
                            AutoModerator.getWarnAmount())).queue();
        }
        if(state == BehaviourState.KICKED){
            textChannel.sendMessage(
                    String.format("**Kicking** <@!%s> for violating the rules.\n" +
                                    "Kicks issued: %d\n" +
                                    "Getting more than %d kicks will cause ban from the server!",
                            user.getId(),
                            violationAmount,
                            AutoModerator.getKickAmount())).queue();
            textChannel.sendMessage(String.format("/kick <@!%s> \"Violation of the rules\"", user.getId())).queue();
        }
        if(state == BehaviourState.BANNED){
            textChannel.sendMessage(
                    String.format("**Banning** <@!%s> for violating the rules. \n" +
                                    "Bans issued: %d",
                            user.getId(),
                            violationAmount)).queue();
            textChannel.sendMessage(String.format("/ban <@!%s> \"Violation of the rules\"", user.getId())).queue();
        }
    }

    public void nextPunishment(TextChannel textChannel){
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

    public User getUser() {
        return user;
    }
}
