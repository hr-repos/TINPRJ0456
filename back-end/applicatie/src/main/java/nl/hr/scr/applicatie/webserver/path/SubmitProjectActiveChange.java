package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;

import java.util.Map;

/* Eli van der Does (1061322) @ December 08, 2023 */
public class SubmitProjectActiveChange {
    private final Main main;

    // api post endpoint to change the active project through dashboard
    public SubmitProjectActiveChange(Webserver webserver, Main main) {
        this.main = main;
        webserver.http().post("api/submit-project-active-change", this::handle);
    }

    public void handle(Context context) {
        int activeProject = context.bodyAsClass(Integer.class);

        // set all projects to inactive
        main.sql().statement(
            "UPDATE projects SET active = FALSE"
        ).update().complete();

        // set the active project, or don't, if project id 0 is given
        if (activeProject != 0) {
            main.sql().statement(
                "UPDATE projects SET active = TRUE WHERE id = ?",
                activeProject
            ).update().complete();
        }

        main.cache().updateActiveProjectId(activeProject);
        main.post().sendFrequency();

        context.json(Map.of(
            "updated", activeProject
        ));
    }
}
