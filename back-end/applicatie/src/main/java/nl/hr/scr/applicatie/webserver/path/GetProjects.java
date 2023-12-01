package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* Eli van der Does (1061322) @ December 01, 2023 */
public class GetProjects {
    private final Main main;

    public GetProjects(Webserver webserver, Main main) {
        this.main = main;
        webserver.http().get("api/get-projects", this::handle);
    }

    public void handle(Context context) {
        List<Map<String, Object>> projects = new ArrayList<>();
        main.sql().statement("""
            SELECT id, name, creation_date, description, creator_name, active
            FROM projects
            ORDER BY id
            """).query().complete(data -> {
            while (data.next()) {
                projects.add(Map.of(
                    "id", data.getInt("id"),
                    "name", data.getString("name"),
                    "creation_date", data.getInt("creation_date"),
                    "description", data.getString("description"),
                    "creator_name", data.getString("creator_name"),
                    "active", data.getBoolean("active")
                ));
            }
        });

        context.json(projects);
    }
}
