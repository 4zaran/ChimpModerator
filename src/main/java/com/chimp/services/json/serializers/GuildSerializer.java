package com.chimp.services.json.serializers;

import net.dv8tion.jda.api.entities.Guild;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

public class GuildSerializer extends JsonSerializer<Guild> {

    @Override
    public void serialize(Guild value,
                          JsonGenerator jgen,
                          SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("guildID", value.getId());
        jgen.writeStringField("guildName", value.getName());
        jgen.writeEndObject();
    }
}
