package com.chimp.services.json.deserializers;

import com.chimp.services.ContextService;
import com.chimp.services.moderation.BadUser;
import com.chimp.services.moderation.BehaviourState;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;

public class BadUserDeserializer extends JsonDeserializer<BadUser> {
    @Override
    public BadUser deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JDA jda = ContextService.getJdaService().getJda();
        JsonNode node = jp.getCodec().readTree(jp);


        BehaviourState state = BehaviourState.valueOf(node.get("state").asText());
        int violationAmount = node.get("violationAmount").asInt();
        String memberId = node.get("memberId").asText();
        String guildId = node.get("guildId").asText();
        // String name = node.get("name").asText();

        Guild guild = jda.getGuildById(guildId);
        assert guild != null;
        Member member = guild.getMemberById(memberId);
        return new BadUser(member, state, violationAmount);
    }
}
