package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import nl.hr.scr.applicatie.webserver.Webserver;

import java.util.Map;

/* Eli @ September 30, 2023 (nl.hr.scr.applicatie.webserver.path) */
public final class Ping
{
    public Ping(Webserver webserver) {
        webserver.http().get("api/ping", this::handle);
        webserver.http().post("api/ping", this::handle);
    }

    private static final Map<String, String> MESSAGE =
        Map.of(
            "message", "Pong!"
        );

    private void handle (Context context) {
        Webserver.LOGGER.info("Body: " + context.body());
        context.json(MESSAGE);
    }
}
