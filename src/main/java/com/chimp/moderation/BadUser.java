package com.chimp.moderation;

import com.chimp.services.AutoModerator;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class BadUser {
    private BehaviourState state;
    private int violationAmount;
    private int warnAmount, kickAmount;
    private User user;

    public BadUser(User user, int warnAmount, int kickAmount){
        this.user = user;
        this.warnAmount = warnAmount;
        this.kickAmount = kickAmount;
        this.violationAmount = 0;
        state = BehaviourState.WARNED;
    }

    public void hasViolated(TextChannel textChannel){
        while (true) {
            if (state == BehaviourState.WARNED && violationAmount >= warnAmount)
                nextPunishment(textChannel);
            else if (state == BehaviourState.KICKED && violationAmount >= kickAmount) {
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
                    String.format("**Warning** <@!%s>! You have violated the rules!\nWarnings issued: %d\nYou will be kicked after %d warnings!",
                            user.getId(),
                            violationAmount,
                            AutoModerator.getWarnAmount())).queue();
        }
        if(state == BehaviourState.KICKED){
            textChannel.sendMessage(
                    String.format("**Kicking** <@!%s> for violating the rules. \nKicks issued: %d\nYou will be banned after %d kicks!",
                            user.getId(),
                            violationAmount,
                            AutoModerator.getKickAmount())).queue();
            textChannel.sendMessage(String.format("/kick <@!%s> \"Violation of the rules\"", user.getId())).queue();
        }
        if(state == BehaviourState.BANNED){
            textChannel.sendMessage(
                    String.format("**Banning** <@!%s> for violating the rules. \nBans issued: %d",
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

    public int getViolationAmount() {
        return violationAmount;
    }

    public void setViolationAmount(int violationAmount) {
        this.violationAmount = violationAmount;
    }

    public User getUser() {
        return user;
    }
}
