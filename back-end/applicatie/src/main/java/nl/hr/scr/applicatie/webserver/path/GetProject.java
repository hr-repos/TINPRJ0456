package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Eli van der Does (1061322) @ January 22, 2024 */
public class GetProject
{
    private final Main main;

    public GetProject (Webserver webserver, Main main) {
        this.main = main;
        webserver.http().get("api/get-project/{id}", this::handle);
    }

    public void handle(Context context) {
        Map<String, Object> values = new HashMap<>();
        int projectId = context.pathParamAsClass("id", Integer.class).getOrDefault(0);

        values.put("id", projectId);

        List<Map<String, Object>> sensors = new ArrayList<>();
        main.sql().statement(
            "SELECT s.id, s.name, s.pin FROM sensors s WHERE s.project_id = ?",
            projectId
        ).query().complete(sensorData -> {
            while (sensorData.next()) {
                sensors.add(new HashMap<>() {{
                    put("id", sensorData.getInt("id"));
                    put("name", sensorData.getString("name"));
                    put("pin", sensorData.getInt("pin"));
                }});
            }
        });

        values.put("sensors", sensors);

        main.sql().statement(
            "SELECT id, name, creation_date, description, creator_name, active, frequency FROM projects WHERE id = ? LIMIT 1",
            projectId
        ).query().complete(data -> {
            if (data.next()) {
                values.put("name", data.getString("name"));
                values.put("creation_date", data.getLong("creation_date"));
                values.put("description", data.getString("description"));
                values.put("creator_name", data.getString("creator_name"));
                values.put("frequency", data.getInt("frequency"));
                values.put("active", data.getBoolean("active"));
                values.put("sensors", sensors);
            }
        });

        context.json(values);
    }
}
