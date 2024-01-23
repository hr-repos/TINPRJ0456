package nl.hr.scr.applicatie;

import me.justeli.esqueleto.Esqueleto;
import me.justeli.esqueleto.driver.MariaDBDriver;
import nl.hr.scr.applicatie.cache.SensorCache;
import nl.hr.scr.applicatie.config.Config;
import nl.hr.scr.applicatie.database.CreateTables;
import nl.hr.scr.applicatie.util.PostUtil;
import nl.hr.scr.applicatie.webserver.Webserver;
import nl.hr.scr.applicatie.webserver.path.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.Executors;

/* Eli @ September 30, 2023 (nl.hr.scr.applicatie) */
public final class Main {
    public static Logger LOGGER = LoggerFactory.getLogger(Main.class.getSimpleName());
    private static final long STARTUP_MILLIS = System.currentTimeMillis();

    private Config config;
    private Esqueleto sql;
    private Webserver webserver;
    private SensorCache cache;
    private PostUtil post;

    public void onEnable() {
        this.config = Config.parse("config.yml");
        if (this.config == null)
            return;

        this.sql = Esqueleto.create(sql -> {
            sql.setHost(this.config.sql().host());
            sql.setPort(this.config.sql().port());
            sql.setDriver(MariaDBDriver.class);
            sql.setAsyncService(Executors.newVirtualThreadPerTaskExecutor());
        }).start(
            this.config.sql().database(),
            this.config.sql().username(),
            this.config.sql().password()
        );

        // database
        new CreateTables(this);

        this.cache = new SensorCache(this);
        this.post = new PostUtil(this);

        // webserver
        this.webserver = new Webserver(this);

        // register api through constructor
        new ExportToCsv(this.webserver, this);
        new GetFrequency(this.webserver, this);
        new GetProject(this.webserver, this);
        new GetProjects(this.webserver, this);
        new GetSensors(this.webserver, this);
        new Ping(this.webserver);
        new SubmitProject(this.webserver, this);
        new SubmitProjectActiveChange(this.webserver, this);
        new SubmitSensor(this.webserver, this);
        new SubmitSensorData(this.webserver, this);

        // send frequency to gpio
        this.post.sendFrequency();
    }

    public void onDisable() {
        Optional.ofNullable(this.webserver).ifPresent(Webserver::close);
        Optional.ofNullable(this.sql).ifPresent(Esqueleto::close);
    }

    public Config config() {
        return config;
    }

    public Esqueleto sql() {
        return sql;
    }

    public Webserver api() {
        return webserver;
    }

    public SensorCache cache() {
        return cache;
    }

    public PostUtil post() {
        return post;
    }

    public static void main(String... args) {
        LOGGER.info("Opstarten van de server..");

        var main = new Main();
        main.onEnable();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            main.onDisable();
            LOGGER.info("Server is afgesloten.");
        }));

        LOGGER.info("Server is opgestart in {}ms.", System.currentTimeMillis() - STARTUP_MILLIS);
    }
}
