package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.validation.BodyValidator;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;
import nl.hr.scr.applicatie.webserver.json.CalibrationDetails;
import nl.hr.scr.applicatie.webserver.json.ProjectDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/* Eli van der Does (1061322) @ January 24, 2024 */
public class SubmitCalibration
{
    private final Main main;

    public SubmitCalibration (Webserver webserver, Main main) {
        this.main = main;
        webserver.http().post("api/submit-calibration", this::handle);
    }

    public void handle(Context context) {
        BodyValidator<CalibrationDetails> dataValidator = context.bodyValidator(CalibrationDetails.class);

        if (!dataValidator.errors().isEmpty()) {
            context.status(HttpStatus.BAD_REQUEST);
            context.json(Map.of("error", "Invalid data"));
            return;
        }

        CalibrationDetails details = dataValidator.get();
        main.sql().statement(
            "UPDATE sensors SET calibrationA = ?, calibrationB = ?, calibrationC = ? WHERE id = ?",
            details.a(), details.b(), details.c(), details.sensorId()
        ).update().complete();

        context.json(Map.of("success", true));
    }
}
