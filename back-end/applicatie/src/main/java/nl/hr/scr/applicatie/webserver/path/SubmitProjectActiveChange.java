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

/* Eli van der Does (1061322) @ December 08, 2023 */
public class SubmitProjectActiveChange
{
    private final Main main;

    public SubmitProjectActiveChange (Webserver webserver, Main main) {
        this.main = main;
        webserver.http().post("api/submit-project-active-change", this::handle);
    }

    public void handle(Context context) {
        int activeProject = context.bodyAsClass(Integer.class);

        main.sql().statement(
            "UPDATE projects SET active = FALSE"
        ).update().complete();

        main.sql().statement(
            "UPDATE projects SET active = TRUE WHERE id = ?",
            activeProject
        ).update().complete();

        context.json(Map.of(
            "updated", activeProject
        ));
    }
}
