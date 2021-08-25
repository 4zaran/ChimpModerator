package com.chimp.services.json.serializers;

import com.chimp.services.moderation.BadUser;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

public class BadUserSerializer extends JsonSerializer<BadUser> {
    @Override
    public void serialize(BadUser value,
                          JsonGenerator jgen,
                          SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeStringField("state", String.valueOf(value.getState()));
        jgen.writeNumberField("violationAmount", value.getViolationAmount());
        jgen.writeStringField("name", value.getMember().getEffectiveName());
        jgen.writeStringField("memberId", value.getMember().getId());
        jgen.writeStringField("guildId", value.getMember().getGuild().getId());
        jgen.writeEndObject();
    }
}
