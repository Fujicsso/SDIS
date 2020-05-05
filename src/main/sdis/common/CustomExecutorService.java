package main.sdis.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class CustomExecutorService {

    private static ScheduledExecutorService instance;

    public static ScheduledExecutorService getInstance() {
        if (instance == null)
            instance = Executors.newScheduledThreadPool(50);

        return instance;
    }
}
