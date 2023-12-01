package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* Eli van der Does (1061322) @ December 01, 2023 */
public class GetSensors {
    private final Main main;

    public GetSensors(Webserver webserver, Main main) {
        this.main = main;
        webserver.http().get("api/get-sensors", this::handle);
    }

    public void handle(Context context) {
        List<Map<String, Object>> sensors = new ArrayList<>();
        main.sql().statement("SELECT id, name, pin, calibrationA, calibrationB, calibrationC FROM sensors ORDER BY pin").query().complete(data -> {
            while (data.next()) {
                sensors.add(Map.of(
                    "id", data.getInt("id"),
                    "name", data.getString("name"),
                    "pin", data.getInt("pin"),
                    "calibration_a", data.getNullableFloat("calibrationA"),
                    "calibration_b", data.getNullableFloat("calibrationB"),
                    "calibration_c", data.getNullableFloat("calibrationC")
                ));
            }
        });

        context.json(sensors);
    }
}
