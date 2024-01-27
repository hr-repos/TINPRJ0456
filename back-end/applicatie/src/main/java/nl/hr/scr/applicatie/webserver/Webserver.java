package nl.hr.scr.applicatie.webserver;

import io.javalin.Javalin;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.util.NetworkUtil;
import nl.hr.scr.applicatie.webserver.websocket.Websocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* Eli @ September 30, 2023 (nl.hr.scr.applicatie.webserver) */
public final class Webserver {
    public static final Logger LOGGER = LoggerFactory.getLogger(Webserver.class.getSimpleName());

    private final Javalin javalin;
    private final Websocket socket;

    // webserver to handle http and websocket requests
    public Webserver(Main main) {
        // create webserver instance with javalin
        this.javalin = Javalin.create(config -> {
            config.showJavalinBanner = false;
        }).start(
            main.config().webserver().port()
        );

        // log all requests
        this.javalin.after(context -> {
            LOGGER.info("{} {} {}: {}",
                NetworkUtil.ip(context),
                context.method().name().toUpperCase(),
                context.status().name(),
                context.path());
        });

        this.socket = new Websocket(this);
    }

    // close webserver
    public void close() {
        this.javalin.close();
    }

    // get javalin webserver instance
    public Javalin http() {
        return javalin;
    }

    // get websocket instance
    public Websocket socket() {
        return socket;
    }
}
