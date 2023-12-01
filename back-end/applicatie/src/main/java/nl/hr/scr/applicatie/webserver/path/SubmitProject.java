package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.validation.BodyValidator;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;
import nl.hr.scr.applicatie.webserver.json.ProjectDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/* Eli van der Does (1061322) @ December 01, 2023 */
public class SubmitProject {
    private final Main main;

    public SubmitProject(Webserver webserver, Main main) {
        this.main = main;
        webserver.http().post("api/submit-project", this::handle);
    }

    public void handle(Context context) {
        BodyValidator<ProjectDetails> dataValidator = context.bodyValidator(ProjectDetails.class);

        if (!dataValidator.errors().isEmpty()) {
            context.status(HttpStatus.BAD_REQUEST);
            return;
        }

        ProjectDetails details = dataValidator.get();
        Optional<Map<String, Object>> inserted = main.sql().statement(
            "INSERT INTO projects (name, description, creator_name, active) VALUES (?, ?, ?, ?)",
            details.name(),
            details.description(),
            details.creatorName(),
            details.active()
        ).update().complete(data -> {
            if (data.next()) {
                return new HashMap<>() {{
                    put("id", data.getInt("id"));
                    put("name", data.getString("name"));
                    put("creation_date", data.getInt("creation_date"));
                    put("description", data.getString("description"));
                    put("creator_name", data.getString("creator_name"));
                    put("active", data.getBoolean("active"));
                }};
            }
            return null;
        });

        if (inserted.isEmpty()) {
            context.status(HttpStatus.BAD_REQUEST);
            return;
        }

        context.json(inserted.get());
    }
}
