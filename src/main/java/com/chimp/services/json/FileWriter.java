package com.chimp.services.json;

import com.chimp.services.ContextService;
import com.chimp.services.Logger;
import com.chimp.services.json.serializers.GuildSerializer;
import com.chimp.services.moderation.AutoModerator;
import net.dv8tion.jda.api.entities.Guild;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

import java.io.File;
import java.io.IOException;

public class FileWriter {

    public static void saveConfig(){
        Logger logger = ContextService.getLogger();
        ObjectMapper mapper = new ObjectMapper();
        JsonPackager packager = new JsonPackager();

        packager.setRestrictions(AutoModerator.getRestrictions());
        addSerializers(mapper);

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("config.json"), packager);
            logger.logInfo("Saved successfully!");
        } catch (IOException e) {
            logger.logError(e.getLocalizedMessage());
        }
    }

    private static void addSerializers(ObjectMapper mapper) {
        SimpleModule module = new SimpleModule("", Version.unknownVersion());
        module.addSerializer(Guild.class, new GuildSerializer());
        mapper.registerModule(module);
    }
}
