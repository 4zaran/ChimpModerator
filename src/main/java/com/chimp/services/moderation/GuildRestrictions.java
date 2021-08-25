package com.chimp.services.moderation;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GuildRestrictions {

    /**
     * Holds information about moderation module being enabled.
     */
    private boolean enabled;

    /**
     * Holds amount of kicks executed before kicking the user.
     * Note that setting this to 0 will skip warn execution.
     */
    private int warnAmount;

    /**
     * Holds amount of kicks executed before ban.
     * Note that setting this to 0 will skip kicks and ban will be executed.
     */
    private int kickAmount;

    /**
     * List of all violators. User is added to this with the first violation.
     */
    private List<BadUser> badUsers;

    /**
     * List of all censored expressions.
     * It means that if any of those strings are found in message it is considered as violation.
     */
    private List<String> censoredExpressions;

    /** The guild that this object applies to. */
    private Guild guild;

    public GuildRestrictions(){
        this.guild = null;
        this.enabled = true;
        this.warnAmount = 3;
        this.kickAmount = 3;
        this.badUsers = new ArrayList<>();
        this.censoredExpressions = new ArrayList<>();
    }

    public GuildRestrictions(Guild guild) {
        this.guild = guild;
        this.enabled = true;
        this.warnAmount = 3;
        this.kickAmount = 3;
        this.badUsers = new ArrayList<>();
        this.censoredExpressions = new ArrayList<>();
    }

    /**
     * Adds a member to list of violators
     * @param member to be added
     * @return added member
     */
    public BadUser addBadUser(Member member){
        BadUser badUser = new BadUser(member);
        badUsers.add(badUser);
        return badUser;
    }

    // -- SETTERS --

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setWarnAmount(int warnAmount) {
        this.warnAmount = warnAmount;
    }

    public void setKickAmount(int kickAmount) {
        this.kickAmount = kickAmount;
    }

    public void setBadUsers(List<BadUser> badUsers) {
        this.badUsers = badUsers;
    }

    public void setCensoredExpressions(List<String> censoredExpressions) {
        this.censoredExpressions = censoredExpressions;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    // -- GETTERS --

    public boolean isEnabled() {
        return enabled;
    }

    public int getWarnAmount() {
        return warnAmount;
    }

    public int getKickAmount() {
        return kickAmount;
    }

    public List<BadUser> getBadUsers() {
        return badUsers;
    }

    public List<String> getCensoredExpressions() {
        return censoredExpressions;
    }

    public Guild getGuild() {
        return guild;
    }
}
