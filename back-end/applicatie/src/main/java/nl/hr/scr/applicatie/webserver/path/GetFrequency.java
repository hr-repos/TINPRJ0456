package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;

import java.util.Map;
import java.util.Optional;

/* Eli van der Does (1061322) @ January 23, 2024 */
public class GetFrequency {
    private final Main main;

    // api get endpoint to get the current frequency of the active project
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
        Main.LOGGER.info("Frequency was given through GET request.");
    }
}
