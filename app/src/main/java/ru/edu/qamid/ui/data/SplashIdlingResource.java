package ru.edu.qamid.ui.data;

import androidx.test.espresso.idling.CountingIdlingResource;

// это Idling Resource для стартового сплэшскрина - загрузка занимает больше времени,
// чем Espresso может ждать, поэтому используем Idling, чтобы тест "догнал" приложение
public class SplashIdlingResource {

    private static final String RESOURCE = "SPLASH";

    private static final CountingIdlingResource idlingResource =
            new CountingIdlingResource(RESOURCE, true);

    public static CountingIdlingResource getInstance() {
        return idlingResource;
    }

    public static void increment() {
        idlingResource.increment();
    }

    public static void decrement() {
        idlingResource.decrement();
    }
}