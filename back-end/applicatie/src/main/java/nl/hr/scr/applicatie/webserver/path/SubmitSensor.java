package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.validation.BodyValidator;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;
import nl.hr.scr.applicatie.webserver.json.SensorDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/* Eli van der Does (1061322) @ December 01, 2023 */
public class SubmitSensor {
    private final Main main;

    public SubmitSensor(Webserver webserver, Main main) {
        this.main = main;
        webserver.http().post("api/submit-sensor", this::handle);
    }

    public void handle(Context context) {
        BodyValidator<SensorDetails> dataValidator = context.bodyValidator(SensorDetails.class);

        if (!dataValidator.errors().isEmpty()) {
            context.status(HttpStatus.BAD_REQUEST);
            context.json(Map.of("error", "Invalid data"));
            return;
        }

        SensorDetails details = dataValidator.get();
        if (details.name() == null || details.name().length() < 3 || details.name().length() > 20) {
            context.status(HttpStatus.BAD_REQUEST);
            context.json(Map.of("error", "Invalid name (min. 3 chars, max. 20 chars)"));
            return;
        }

        if (details.unit() == null || details.unit().isEmpty() || details.unit().length() > 7) {
            context.status(HttpStatus.BAD_REQUEST);
            context.json(Map.of("error", "Invalid sensor unit (max. 7 chars)"));
            return;
        }

        Optional<Map<String, Object>> inserted = main.sql().statement(
            "INSERT INTO sensors (name, pin, project_id, unit) VALUES (?, ?, ?, ?)",
            details.name(),
            details.pin(),
            details.project(),
            details.unit()
        ).update().complete(data -> data.next()?  Map.of("id", data.getInt("id")) : null);

        if (inserted.isEmpty()) {
            context.status(HttpStatus.BAD_REQUEST);
            context.json(Map.of("error", "Could not insert sensor"));
            return;
        }

        main.cache().updateSensors(details.project());

        context.json(inserted.get());
    }
}
