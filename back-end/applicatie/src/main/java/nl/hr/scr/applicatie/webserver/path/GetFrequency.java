package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import me.justeli.esqueleto.UnparsedStatement;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;
import org.eclipse.jetty.websocket.core.OpCode;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/* Eli van der Does (1061322) @ January 23, 2024 */
public class GetFrequency
{
    private final Main main;

    public GetFrequency(Webserver webserver, Main main) {
        this.main = main;
        webserver.http().get("api/get-frequency", this::handle);
    }

    public void handle(Context context) {
        Optional<Integer> frequency = main.sql().statement(
            "SELECT frequency FROM projects WHERE active = TRUE LIMIT 1"
        ).query().complete(data -> data.next()? data.getInt("frequency") : null);

        if (frequency.isEmpty()) {
            context.status(HttpStatus.NO_CONTENT);
            return;
        }

        context.json(Map.of("frequency", frequency.get()));
    }
}
