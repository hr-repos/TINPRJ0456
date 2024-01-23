package nl.hr.scr.applicatie.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hr.scr.applicatie.Main;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PostUtil {
    private final Main main;

    public PostUtil(Main main) {
        this.main = main;
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static @Nullable HttpResponse<String> postRequest(String url, Object body)
    throws InterruptedException, ExecutionException {
        CompletableFuture<HttpResponse<String>> responseFuture;

        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(buildBodyPublisher(OBJECT_MAPPER.writeValueAsString(body)))
                .timeout(Duration.of(100, ChronoUnit.MILLIS))
                .build();

            responseFuture = httpClient.sendAsync(
                request,
                HttpResponse.BodyHandlers.ofString()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return responseFuture.get();
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

        try {
            postRequest(
                "http://localhost:" + main.config().gpio().port() + "/submit-frequency",
                Map.of("frequency", frequency.orElse(0))
            );
        } catch (InterruptedException | ExecutionException e) {
            Main.LOGGER.warn("Could not send frequency. GPIO will have to get frequency by GET request.");
        }
    }
}
