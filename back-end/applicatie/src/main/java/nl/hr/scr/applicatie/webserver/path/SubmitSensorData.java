package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.validation.BodyValidator;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;
import nl.hr.scr.applicatie.webserver.json.SensorData;

import java.util.Map;

/* Eli @ September 30, 2023 (nl.hr.scr.applicatie.webserver.path) */
public final class SubmitSensorData {
    private final Main main;

    public SubmitSensorData(Webserver webserver, Main main) {
        this.main = main;
        webserver.http().post("api/sensors", this::handle); // <- deprecated
        webserver.http().post("api/submit-sensor-data", this::handle);
    }

    private void handle (Context context) {
        BodyValidator<SensorData> dataValidator = context.bodyValidator(SensorData.class);

        if (!dataValidator.errors().isEmpty()) {
            context.status(HttpStatus.BAD_REQUEST);
            return;
        }

        this.main.api().socket().broadcast(Map.of("sensor_data", dataValidator.get().data()));
    }
}
