package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/* Eli van der Does (1061322) @ January 22, 2024 */
public class ExportToCsv
{
    private final Main main;

    public ExportToCsv(Webserver webserver, Main main) {
        this.main = main;
        webserver.http().get("api/export-to-csv/{project_id}", this::handle);
    }

    private record Sensor(int id, int pin, String name)
    {}

    public void handle(Context context) {
        AtomicInteger projectId = new AtomicInteger(
            context.pathParamAsClass("project_id", Integer.class).getOrDefault(0)
        );

        if (projectId.get() <= 0) {
            context.status(HttpStatus.BAD_REQUEST);
            context.json(Map.of("error", "Invalid project"));
            return;
        }

        List<Sensor> sensors = new ArrayList<>();
        main.sql().statement(
            "SELECT id, pin, name FROM sensors WHERE project_id = ?",
            projectId.get()
        ).query().complete(data -> {
            while (data.next()) {
                sensors.add(new Sensor(
                    data.getInt("id"),
                    data.getInt("pin"),
                    data.getString("name")
                ));
            }
        });

        if (sensors.isEmpty()) {
            context.status(HttpStatus.BAD_REQUEST);
            context.json(Map.of("error", "No sensors found in project"));
            return;
        }

        List<String> sensorQueries = new ArrayList<>();
        List<String> sensorNames = new ArrayList<>();

        for (Sensor sensor : sensors) {
            sensorQueries.add(
                "MAX(CASE WHEN sensor_id = " + sensor.id() + " THEN value END) AS sensor" + sensor.id()
            );
            sensorNames.add("sensor" + sensor.id());
        }

        List<String> csv = new ArrayList<>();
        csv.add("date;" + String.join(";", sensorNames));

        main.sql().statement(
            "SELECT FROM_UNIXTIME(measure_millis / 1000) AS date, "
                + String.join(",", sensorQueries)
                + " FROM data GROUP BY measure_millis ORDER BY measure_millis"
        ).query().complete(data -> {
            while (data.next()) {
                StringBuilder row = new StringBuilder();
                row.append(data.getString("date")).append(";");
                for (String sensorName : sensorNames) {
                    row.append(data.getInt(sensorName)).append(";");
                }
                row.deleteCharAt(row.length() - 1);
                csv.add(row.toString());
            }
        });

        context.contentType(ContentType.TEXT_CSV);
        context.result(String.join("\n", csv));
    }

    // SELECT
    //     measure_millis,
    //     MAX(CASE WHEN sensor_id = 1 THEN value END) AS sensor1,
    //     MAX(CASE WHEN sensor_id = 2 THEN value END) AS sensor2,
    //     MAX(CASE WHEN sensor_id = 3 THEN value END) AS sensor3
    // FROM data
    // GROUP BY measure_millis
    // ORDER BY measure_millis;
}
