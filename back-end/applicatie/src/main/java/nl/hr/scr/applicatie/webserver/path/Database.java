package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;

import java.util.LinkedHashMap;
import java.util.SplittableRandom;

/* Eli van der Does (1061322) @ September 30, 2023 */
public class Database {
    private final Main main;

    public Database(Webserver webserver, Main main) {
        this.main = main;
        webserver.http().get("api/database", this::handle);
    }

    private static final SplittableRandom RANDOM = new SplittableRandom();

    private void handle(Context context) {
        main.sql().statement(
            "INSERT INTO sensors (name) VALUE (?)",
            Integer.toHexString(RANDOM.nextInt())
        ).update().complete();

        var ids = new LinkedHashMap<>();

        main.sql().statement("SELECT id, name FROM sensors").query().complete(data -> {
            while (data.next()) {
                ids.put(data.getInt("id"), data.getString("name"));
            }
        });

        context.json(ids);
    }
}
