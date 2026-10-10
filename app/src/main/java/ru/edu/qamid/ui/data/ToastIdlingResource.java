package ru.edu.qamid.ui.data;

import androidx.test.espresso.IdlingResource;

import java.util.ArrayList;
import java.util.List;

public class ToastIdlingResource implements IdlingResource {

    private static ToastIdlingResource instance;

    public static synchronized ToastIdlingResource getInstance() {
        if (instance == null) {
            instance = new ToastIdlingResource();
        }
        return instance;
    }

    // список всех тостов, перехваченных во время теста
    private final List<String> captured = new ArrayList<>();

    // тест, который мы ждём в данный момент (null = не ждём)
    private String waitingFor = null;

    private volatile ResourceCallback callback;

    private ToastIdlingResource() { }

    // метод вызывается из листнера UiAutomation
    public synchronized void onToast(String text) {
        captured.add(text);
        if (waitingFor != null && text.contains(waitingFor)) {
            waitingFor = null;
            ResourceCallback cb = callback;
            if (cb != null) {
                cb.onTransitionToIdle();
            }
        }
    }

    // метод устанавливает ожидание перехвата тоста
    public synchronized void expect(String text) {
        for (String c : captured) {
            if (c.contains(text)) {
                return;
            }
        }
        waitingFor = text;
    }

    // сброс между тестами
    public synchronized void reset() {
        captured.clear();
        waitingFor = null;
    }

    @Override
    public String getName() {
        return "TOAST";
    }

    @Override
    public synchronized boolean isIdleNow() {
        return waitingFor == null;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.callback = resourceCallback;
    }
}