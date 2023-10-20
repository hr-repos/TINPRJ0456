package nl.hr.scr.applicatie.monitor;

import nl.hr.scr.applicatie.Main;

import java.util.Map;
import java.util.SplittableRandom;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/* Eli @ October 20, 2023 (nl.hr.scr.applicatie.monitor) */
public final class Sensors
{
    private final Main main;

    private static final ScheduledExecutorService SCHEDULE = Executors.newSingleThreadScheduledExecutor();

    public Sensors(Main main) {
        this.main = main;
        SCHEDULE.scheduleAtFixedRate(this::execute, 0, 100, TimeUnit.MILLISECONDS);
    }

    private static final SplittableRandom RANDOM = new SplittableRandom();

    private long inputNH3 = 200;
    private long outputNH3 = 100;
    private long inputCO2 = 1200;

    private void execute() {
        this.main.api().socket().broadcast(Map.of(
            "inputNH3", RANDOM.nextBoolean()? this.inputNH3++ : this.inputNH3--,
            "outputNH3", RANDOM.nextBoolean()? this.outputNH3++ : this.outputNH3--,
            "inputCO2", RANDOM.nextBoolean()? this.inputCO2++ : this.inputCO2--,
            "calculation", this.inputNH3 - this.outputNH3
        ));
    }
}
