package nl.hr.scr.applicatie.webserver.path;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.validation.BodyValidator;
import nl.hr.scr.applicatie.Main;
import nl.hr.scr.applicatie.webserver.Webserver;
import nl.hr.scr.applicatie.webserver.json.SensorData;

import java.util.Map;

/* Eli @ September 30, 2023 (nl.hr.scr.applicatie.webserver.path) */
public final class Sensors
{
    private final Main main;

    public Sensors (Webserver webserver, Main main) {
        this.main = main;
        webserver.http().post("api/sensors", this::handle);
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
            "inputNH3", data.inputNH3(),
            "outputNH3", data.outputNH3(),
            "inputCO2", -999,
            "calculation", data.inputNH3() - data.outputNH3()
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
