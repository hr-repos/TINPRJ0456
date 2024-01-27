package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.validation.BodyValidator;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;
import nl.hr.scr.applicatie.webserver.json.ProjectId;

import java.util.Map;

/* Eli van der Does (1061322) @ January 22, 2024 */
public class DeleteProject {
    private final Main main;

    // api post endpoint to delete a project
    public DeleteProject (Webserver webserver, Main main) {
        this.main = main;
        webserver.http().post("api/delete-project", this::handle);
    }

    public void handle(Context context) {
        BodyValidator<ProjectId> dataValidator = context.bodyValidator(ProjectId.class);

        if (!dataValidator.errors().isEmpty()) {
            context.status(HttpStatus.BAD_REQUEST);
            context.json(Map.of("error", "Project not found"));
            return;
        }

        int id = dataValidator.get().id();
        main.sql().statement("DELETE FROM projects WHERE id = ?", id).update().complete();
        context.json(Map.of("deleted", id));
    }
}
