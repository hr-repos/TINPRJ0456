package nl.hr.scr.applicatie.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hr.scr.applicatie.Main;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class PostUtil {
    private final Main main;

    public PostUtil(Main main) {
        this.main = main;
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static HttpResponse<String> postRequest(String url, Object body)
    throws InterruptedException, ExecutionException, IOException {
        HttpResponse<String> response;

        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(buildBodyPublisher(OBJECT_MAPPER.writeValueAsString(body)))
                .timeout(Duration.of(500, ChronoUnit.MILLIS))
                .build();

            response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            );
        }

        return response;
    }

    private static HttpRequest.BodyPublisher buildBodyPublisher(String jsonBody) {
        return HttpRequest.BodyPublishers.ofString(
            jsonBody,
            StandardCharsets.UTF_8
        );
    }

    // send to gpio that a different frequency is needed
    public void sendFrequency () {
        Optional<Integer> frequency = main.sql().statement(
            "SELECT frequency FROM projects WHERE active = TRUE LIMIT 1"
        ).query().complete(data -> data.next()? data.getInt("frequency") : null);

        String url = "http://localhost:" + main.config().gpio().port() + "/submit-frequency";
        try {
            Main.LOGGER.info("Sending frequency of {}ms to {}", frequency.orElse(0), url);
            var data = postRequest(
                url,
                Map.of("frequency", frequency.orElse(0))
            );

            Main.LOGGER.info("Sent frequency. Received status code {}.", data.statusCode());
        } catch (InterruptedException | ExecutionException | IOException e) {
            Main.LOGGER.warn(
                "Could not send frequency. GPIO will have to get frequency by GET request: {}",
                e.getMessage()
            );
        }
    }
}
