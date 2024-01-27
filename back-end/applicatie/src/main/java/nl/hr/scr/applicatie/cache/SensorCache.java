package nl.hr.scr.applicatie.cache;

import nl.hr.scr.applicatie.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/* Eli @ January 22, 2024 (nl.hr.scr.applicatie.cache) */
// keep track of active project and its sensors
public final class SensorCache {
    private final Main main;

    //                pin      sensor_id
    private final Map<Integer, Integer> activeSensors = new HashMap<>();
    private final AtomicInteger activeProjectId = new AtomicInteger(0);

    public SensorCache(Main main) {
        this.main = main;

        // we need to cache the active project on startup
        main.sql().statement("SELECT id FROM projects WHERE active = TRUE LIMIT 1").query().complete(data -> {
            if (data.next()) {
                updateActiveProjectId(data.getInt("id"));
            }
        });
    }

    public int getActiveProjectId() {
        return activeProjectId.get();
    }

    public void updateActiveProjectId(int activeProjectId) {
        this.activeProjectId.set(activeProjectId);

        this.activeSensors.clear();
        main.sql().statement(
            "SELECT id, pin FROM sensors WHERE project_id = ?",
            activeProjectId
        ).query().complete(data -> {
            while (data.next()) {
                this.activeSensors.put(data.getInt("pin"), data.getInt("id"));
            }
        });
    }

    public void updateSensors(int projectId) {
        // only update sensors if the active project is updated
        if (getActiveProjectId() != projectId) {
            return;
        }

        updateActiveProjectId(projectId);
    }

    public Map<Integer, Integer> getActiveSensors() {
        return activeSensors;
    }
}
