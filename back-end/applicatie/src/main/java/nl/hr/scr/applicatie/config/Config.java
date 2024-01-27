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
// config parser
public record Config(SqlConfig sql, WebserverConfig webserver, GpioConfig gpio) {
    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class.getSimpleName());

    public static @Nullable Config parse(String path) {
        try {
            var file = new File(path);
            if (!file.exists()) {
                var stream = Config.class.getResourceAsStream("/config.yml");
                if (stream == null)
                    return null;

                // copy file from resources to current directory if it doesn't exist
                Files.copy(stream, Paths.get("config.yml"));
                LOGGER.info("Created new configuration file.");
            }

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            String yaml = Files.readString(Paths.get(path));

            // parse the config from the config class
            return mapper.readValue(yaml, Config.class);
        }
        catch (IOException exception) {
            LOGGER.error("Couldn't load config file {}.", path);
        }

        return null;
    }
}
