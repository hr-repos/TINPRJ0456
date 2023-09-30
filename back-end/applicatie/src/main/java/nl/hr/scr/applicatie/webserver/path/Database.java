package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;

import java.util.ArrayList;

/* Eli van der Does (1061322) @ September 30, 2023 */
public class Database
{
    private final Main main;

    public Database(Webserver webserver, Main main) {
        this.main = main;
        webserver.http().get("api/database", this::handle);
    }

    private void handle(Context context) {
        var ids = new ArrayList<>();

        main.sql().statement("SELECT id FROM voorbeeld_tabel").query().complete(data -> {
            while (data.next()) {
                ids.add(data.getInt("id"));
            }
        });

        context.json(ids);
    }
}
