package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import me.justeli.esqueleto.UnparsedStatement;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/* Eli van der Does (1061322) @ January 22, 2024 */
public class GetProject {
    private final Main main;

    // api get endpoint to get a project, if no id is given the active project is returned
    public GetProject(Webserver webserver, Main main) {
        this.main = main;
        webserver.http().get("api/get-project/{id}", this::handle);
        webserver.http().get("api/get-active-project", this::handle);
    }

    public void handle(Context context) {
        Map<String, Object> values = new HashMap<>();
        AtomicInteger projectId = new AtomicInteger(
            context.pathParamMap().isEmpty()? 0 : context.pathParamAsClass("id", Integer.class).getOrDefault(0)
        );

        // depending on the given id, get the project or the active project
        UnparsedStatement statement;
        if (projectId.get() == 0) {
            statement = main.sql().statement(
                "SELECT id, name, creation_unix, description, creator_name, frequency, active FROM projects WHERE active = TRUE LIMIT 1"
            );
        } else {
            statement = main.sql().statement(
                "SELECT id, name, creation_unix, description, creator_name, frequency, active FROM projects WHERE id = ?",
                projectId.get()
            );
        }

        // get project data
        statement.query().complete(data -> {
            if (data.next()) {
                projectId.set(data.getInt("id"));
                values.put("id", projectId.get());
                values.put("name", data.getString("name"));
                values.put("creation_date", data.getLong("creation_unix"));
                values.put("description", data.getString("description"));
                values.put("creator_name", data.getString("creator_name"));
                values.put("frequency", data.getInt("frequency"));
                values.put("active", data.getBoolean("active"));
            }
        });

        List<Map<String, Object>> sensors = new ArrayList<>();
        Map<String, Object> pins = new HashMap<>();

        // put sensor data in the values maps
        main.sql().statement(
            "SELECT id, name, pin, unit, calibrationA, calibrationB, calibrationC FROM sensors WHERE project_id = ? ORDER BY pin",
            projectId.get()
        ).query().complete(sensorData -> {
            while (sensorData.next()) {
                float a = sensorData.getFloat("calibrationA");
                float b = sensorData.getFloat("calibrationB");
                float c = sensorData.getFloat("calibrationC");
                int pin = sensorData.getInt("pin");

                pins.put("pin" + pin, new HashMap<>() {{
                    put("a", a);
                    put("b", b);
                    put("c", c);
                }});

                sensors.add(new HashMap<>() {{
                    put("id", sensorData.getInt("id"));
                    put("name", sensorData.getString("name"));
                    put("pin", pin);
                    put("unit", sensorData.getString("unit"));
                    put("calibrated", a != 0 || b != 1 || c != 0);
                }});
            }
        });

        values.put("sensors", sensors);
        values.put("pins", pins);

        context.json(values);
    }
}
