package com.chimp.services.json;

import com.chimp.services.ContextService;
import com.chimp.services.Logger;
import com.chimp.services.json.deserializers.BadUserDeserializer;
import com.chimp.services.json.deserializers.GuildDeserializer;
import com.chimp.services.moderation.AutoModerator;
import com.chimp.services.moderation.BadUser;
import com.chimp.services.moderation.GuildRestrictions;
import net.dv8tion.jda.api.entities.Guild;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class FileReader {
    public static void loadConfig(){
        Logger logger = ContextService.getLogger();
        ObjectMapper mapper = new ObjectMapper();

        addDeserializers(mapper);
        try {
            JsonPackager packager = mapper.readValue(new File("config.json"), JsonPackager.class);
            extractPackager(packager);
            logger.logInfo("Load successful!");
        } catch (IOException e) {
            logger.logError(e.getLocalizedMessage());
        }
    }

    private static void addDeserializers(ObjectMapper mapper) {
        SimpleModule module = new SimpleModule("", Version.unknownVersion());

        module.addDeserializer(BadUser.class, new BadUserDeserializer());
        module.addDeserializer(Guild.class, new GuildDeserializer());

        mapper.registerModule(module);
    }

    private static void extractPackager(JsonPackager packager){
        List<GuildRestrictions> restrictions = packager.getRestrictions();
        for (GuildRestrictions restriction : restrictions) {
            String guildId = restriction.getGuild().getId();

            // Remove if exists
            if(AutoModerator.getRestrictionsById(guildId) != null) {
                AutoModerator.removeRestrictionsById(guildId);
            }

            AutoModerator.addRestrictions(restriction);
        }
    }

    public static String readTextFile(String path){
        try {
            StringBuilder text = new StringBuilder();
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine());
                if(scanner.hasNextLine())
                    text.append("\n");
            }
            scanner.close();
            return String.valueOf(text);
        } catch (FileNotFoundException e) {
            ContextService.getLogger().logInfo(e.getLocalizedMessage());
        }
        return null;
    }
}
