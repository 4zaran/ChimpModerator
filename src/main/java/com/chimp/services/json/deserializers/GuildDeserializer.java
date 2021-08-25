package com.chimp.services.json.deserializers;

import com.chimp.services.ContextService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;

public class GuildDeserializer extends JsonDeserializer<Guild> {

    @Override
    public Guild deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String id = node.get("guildID").asText();
        JDA jda = ContextService.getJdaService().getJda();
        return jda.getGuildById(id);
    }
}
