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
public class GetProject
{
    private final Main main;

    public GetProject (Webserver webserver, Main main) {
        this.main = main;
        webserver.http().get("api/get-project/{id}", this::handle);
        webserver.http().get("api/get-active-project", this::handle);
    }

    public void handle(Context context) {
        Map<String, Object> values = new HashMap<>();
        AtomicInteger projectId = new AtomicInteger(
            context.pathParamMap().isEmpty()? 0 : context.pathParamAsClass("id", Integer.class).getOrDefault(0)
        );

        UnparsedStatement statement;
        if (projectId.get() == 0) {
            statement = main.sql().statement(
                "SELECT id, name, creation_date, description, creator_name, frequency, active FROM projects WHERE active = TRUE LIMIT 1"
            );
        } else {
            statement = main.sql().statement(
                "SELECT id, name, creation_date, description, creator_name, frequency, active FROM projects WHERE id = ?",
                projectId.get()
            );
        }

        statement.query().complete(data -> {
            if (data.next()) {
                projectId.set(data.getInt("id"));
                values.put("id", projectId.get());
                values.put("name", data.getString("name"));
                values.put("creation_date", data.getLong("creation_date"));
                values.put("description", data.getString("description"));
                values.put("creator_name", data.getString("creator_name"));
                values.put("frequency", data.getInt("frequency"));
                values.put("active", data.getBoolean("active"));
            }
        });

        List<Map<String, Object>> sensors = new ArrayList<>();
        main.sql().statement(
            "SELECT id, name, pin FROM sensors WHERE project_id = ?",
            projectId.get()
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
        context.json(values);
    }
}
