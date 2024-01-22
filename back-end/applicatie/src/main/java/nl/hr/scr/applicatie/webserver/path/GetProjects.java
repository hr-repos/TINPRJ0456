package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;

import java.util.ArrayList;
import java.util.HashMap;
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
            SELECT p.id, p.name, p.creation_date, p.description, p.creator_name, p.active
            FROM projects p
            ORDER BY p.id
            """).query().complete(data -> {
            while (data.next()) {
                long projectId = data.getInt("id");
                List<Map<String, Object>> sensors = new ArrayList<>();

                main.sql().statement("SELECT s.id, s.name, s.pin FROM sensors s WHERE s.project_id = ?", projectId).query().complete(sensorData -> {
                    while (sensorData.next()) {
                        sensors.add(new HashMap<>() {{
                            put("id", sensorData.getInt("id"));
                            put("name", sensorData.getString("name"));
                            put("pin", sensorData.getInt("pin"));
                        }});
                    }
                });

                projects.add(new HashMap<>() {{
                    put("id", projectId);
                    put("name", data.getString("name"));
                    put("creation_date", data.getLong("creation_date"));
                    put("description", data.getString("description"));
                    put("creator_name", data.getString("creator_name"));
                    put("active", data.getBoolean("active"));
                    put("sensors", sensors);
                }});
            }
        });

        context.json(projects);
    }
}
