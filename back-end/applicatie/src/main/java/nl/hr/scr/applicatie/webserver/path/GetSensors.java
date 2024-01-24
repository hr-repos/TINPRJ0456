package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Eli van der Does (1061322) @ December 01, 2023 */
public class GetSensors {
    private final Main main;

    public GetSensors(Webserver webserver, Main main) {
        this.main = main;
        webserver.http().get("api/get-sensors/{project_id}", this::handle);
    }

    public void handle(Context context) {
        var projectId = context.pathParamAsClass("project_id", Integer.class);

        List<Map<String, Object>> sensors = new ArrayList<>();
        main.sql().statement(
            "SELECT id, name, pin, calibrationA, unit, calibrationB, calibrationC FROM sensors WHERE project_id = ? ORDER BY pin",
            projectId.getOrDefault(0)
        ).query().complete(data -> {
            while (data.next()) {
                float a = data.getFloat("calibrationA");
                float b = data.getFloat("calibrationB");
                float c = data.getFloat("calibrationC");

                sensors.add(new HashMap<>() {{
                    put("id", data.getInt("id"));
                    put("name", data.getString("name"));
                    put("pin", data.getInt("pin"));
                    put("unit", data.getString("unit"));
                    put("calibration", Map.of(
                        "calibrated", a != 0 || b != 1 || c != 0,
                        "a", a, "b", b, "c", c
                    ));
                }});
            }
        });

        Map<String, Object> project = new HashMap<>();
        main.sql().statement(
            "SELECT id, name, creation_unix, description, creator_name, frequency, active FROM projects WHERE id = ?",
            projectId.getOrDefault(0)
        ).query().complete(data -> {
            if (data.next()) {
                project.put("id", data.getInt("id"));
                project.put("name", data.getString("name"));
                project.put("creation_date", data.getLong("creation_unix"));
                project.put("description", data.getString("description"));
                project.put("creator_name", data.getString("creator_name"));
                project.put("frequency", data.getInt("frequency"));
                project.put("active", data.getBoolean("active"));
            }
        });

        context.json(Map.of(
            "sensors", sensors,
            "project", project
        ));
    }
}
