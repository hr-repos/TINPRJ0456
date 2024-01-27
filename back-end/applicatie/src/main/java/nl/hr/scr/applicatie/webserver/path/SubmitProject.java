package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.validation.BodyValidator;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;
import nl.hr.scr.applicatie.webserver.json.ProjectDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/* Eli van der Does (1061322) @ December 01, 2023 */
public class SubmitProject {
    private final Main main;

    // api post endpoint to submit a new project
    public SubmitProject(Webserver webserver, Main main) {
        this.main = main;
        webserver.http().post("api/submit-project", this::handle);
    }

    public void handle(Context context) {
        BodyValidator<ProjectDetails> dataValidator = context.bodyValidator(ProjectDetails.class);

        if (!dataValidator.errors().isEmpty()) {
            context.status(HttpStatus.BAD_REQUEST);
            context.json(Map.of("error", "Invalid data"));
            return;
        }

        ProjectDetails details = dataValidator.get();
        if (details.name() == null || details.name().length() < 3 || details.name().length() > 30) {
            context.status(HttpStatus.BAD_REQUEST);
            context.json(Map.of("error", "Invalid name"));
            return;
        }

        if (details.creatorName() == null || details.creatorName().length() < 3 || details.creatorName().length() > 60) {
            context.status(HttpStatus.BAD_REQUEST);
            context.json(Map.of("error", "Invalid creator name"));
            return;
        }

        if (details.frequency() < 100 || details.frequency() > 60000) {
            context.status(HttpStatus.BAD_REQUEST);
            context.json(Map.of("error", "Invalid frequency (min. 100ms, max. 60000ms)"));
            return;
        }

        // insert project into database
        Optional<Map<String, Object>> inserted = main.sql().statement(
            "INSERT INTO projects (name, description, creator_name, active, frequency) VALUES (?, ?, ?, ?, ?)",
            details.name(),
            details.description(),
            details.creatorName(),
            details.active(),
            details.frequency()
        ).update().complete(data -> {
            if (data.next()) {
                return new HashMap<>() {{
                    put("id", data.getInt("id"));
                    put("name", data.getString("name"));
                    put("creation_date", data.getString("creation_unix"));
                    put("description", data.getString("description"));
                    put("creator_name", data.getString("creator_name"));
                    put("frequency", data.getInt("frequency"));
                    put("active", data.getBoolean("active"));
                    put("sensors", new ArrayList<>());
                }};
            }
            return null;
        });

        // return error if project could not be inserted
        if (inserted.isEmpty()) {
            context.status(HttpStatus.BAD_REQUEST);
            context.json(Map.of("error", "Could not insert project"));
            return;
        }

        context.json(inserted.get());
    }
}
