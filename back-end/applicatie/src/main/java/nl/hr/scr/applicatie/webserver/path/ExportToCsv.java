package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/* Eli van der Does (1061322) @ January 22, 2024 */
public class ExportToCsv
{
    private final Main main;

    public ExportToCsv(Webserver webserver, Main main) {
        this.main = main;
        webserver.http().get("api/export-to-csv/{id}", this::handle);
    }

    public void handle(Context context) {
        AtomicInteger projectId = new AtomicInteger(
            context.pathParamAsClass("id", Integer.class).getOrDefault(0)
        );

        if (projectId.get() == 0) {
            context.status(HttpStatus.BAD_REQUEST);
            context.json(Map.of("error", "Invalid project"));
            return;
        }

        StringBuilder builder = new StringBuilder();

        main.sql().statement("""
            SELECT FROM_UNIXTIME(d.measure_millis / 1000) AS date, d.value, s.name
            FROM data d
                JOIN sensors s ON d.sensor_id = s.id
            WHERE s.project_id = ?
            ORDER BY d.measure_millis
            """, projectId.get()
        ).query().complete(data -> {
            while (data.next()) {
                builder.append(data.getLong("measure_millis")).append(",")
                    .append(data.getFloat("value")).append(",")
                    .append(data.getString("name")).append("\n");
            }
        });

//        context.contentType(ContentType.TEXT_CSV);
        context.result(builder.toString());
    }
}
