package com.chimp.services.json;

import com.chimp.services.moderation.GuildRestrictions;

import java.util.List;

public class JsonPackager {
    private List<GuildRestrictions> restrictions;

    public JsonPackager(){ }

    public List<GuildRestrictions> getRestrictions(){
        return restrictions;
    }

    public void setRestrictions(List<GuildRestrictions> restrictions) {
        this.restrictions = restrictions;
    }
}
