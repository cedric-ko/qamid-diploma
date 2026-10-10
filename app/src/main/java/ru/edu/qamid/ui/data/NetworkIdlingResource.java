package ru.edu.qamid.ui.data;

import androidx.test.espresso.idling.CountingIdlingResource;

// Этот класс - Idling Resource для асинхронных операций обращения приложения к сети помимо сплэшскрина
public class NetworkIdlingResource {

    private static final String RESOURCE = "NETWORK";

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