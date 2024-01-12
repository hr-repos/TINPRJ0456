package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.validation.BodyValidator;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;
import nl.hr.scr.applicatie.webserver.json.SensorData;

import java.util.Map;

/* Eli @ September 30, 2023 (nl.hr.scr.applicatie.webserver.path) */
public final class SubmitSensorData {
    private final Main main;

    public SubmitSensorData(Webserver webserver, Main main) {
        this.main = main;
        webserver.http().post("api/sensors", this::handle); // <- deprecated
        webserver.http().post("api/submit-sensor-data", this::handle);
    }

    private void handle (Context context) {
        BodyValidator<SensorData> dataValidator = context.bodyValidator(SensorData.class);

        if (!dataValidator.errors().isEmpty()) {
            context.status(HttpStatus.BAD_REQUEST);
            return;
        }

        SensorData data = dataValidator.get();
        System.out.println(data);

        this.main.api().socket().broadcast(Map.of(
            "inputNH3", data.data().get(0),
            "outputNH3", data.data().get(1),
            "inputCO2", data.data().get(2),
            "calculation", data.data().get(1) - data.data().get(0)
        ));
    }

    /*
        fetch('http://localhost/api/sensors', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                inputNH3: 12.34,
                outputNH3: 56.78
            }),
        })
        .then(response => {
            if (response.ok) {
                console.log('Data sent successfully');
            }
            else {
                console.error('Failed to send data');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
     */
}
