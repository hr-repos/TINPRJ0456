package nl.hr.scr.applicatie.webserver;

import io.javalin.Javalin;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.util.NetworkUtil;
import nl.hr.scr.applicatie.webserver.path.Database;
import nl.hr.scr.applicatie.webserver.path.Ping;
import nl.hr.scr.applicatie.webserver.websocket.Websocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* Eli @ September 30, 2023 (nl.hr.scr.applicatie.webserver) */
public final class Webserver
{
    public static final Logger LOGGER = LoggerFactory.getLogger(Webserver.class.getSimpleName());

    private final Main main;
    private final Javalin javalin;
    private final Websocket socket;

    public Webserver(Main main) {
        this.main = main;
        this.javalin = Javalin.create(config -> {
            config.showJavalinBanner = false;
        }).start(
            main.config().webserver().port()
        );

        this.javalin.after(context -> {
            LOGGER.info("{} {} {}: {}",
                NetworkUtil.ip(context),
                context.method().name().toUpperCase(),
                context.status().name(),
                context.path());
        });

        this.socket = new Websocket(this, main);

        new Ping(this);
        new Database(this, main);
    }

    public void close() {
        this.javalin.close();
    }

    public Javalin http() {
        return javalin;
    }

    public Websocket socket() {
        return socket;
    }
}
