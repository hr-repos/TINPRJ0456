package nl.hr.scr.applicatie.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import nl.hr.scr.applicatie.Main;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/* Eli @ September 30, 2023 (nl.hr.scr.applicatie.config) */
public record Config(SqlConfig sql, WebserverConfig webserver, GpioConfig gpio)
{
    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class.getSimpleName());

    @Nullable
    public static Config parse (String path) {
        try {
            var file = new File(path);
            if (!file.exists()) {
                var stream = Config.class.getResourceAsStream("/config.yml");
                if (stream == null)
                    return null;

                Files.copy(stream, Paths.get("config.yml"));
                LOGGER.info("Nieuw configuratiebestand aangemaakt.");
            }

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            String yaml = Files.readString(Paths.get(path));

            return mapper.readValue(yaml, Config.class);
        }
        catch (IOException exception) {
            LOGGER.error("Kon configuratiebestand {} niet laden.", path);
        }

        return null;
    }
}
