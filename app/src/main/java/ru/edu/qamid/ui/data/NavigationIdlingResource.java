package ru.edu.qamid.ui.data;

import androidx.fragment.app.Fragment;
import androidx.test.espresso.IdlingResource;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// Этот класс запускает счётчики IdlingResource при смене экранов,
// чтобы дождаться появления view, так как Espresso иногда делает проверку видимости view быстрее,
// чем UI успевает его отрисовать

public class NavigationIdlingResource implements IdlingResource {

    private static NavigationIdlingResource instance;

    public static synchronized NavigationIdlingResource getInstance() {
        if (instance == null) {
            instance = new NavigationIdlingResource();
        }
        return instance;
    }

    // фрагменты, которые уже прошли onPreAttached, но ещё не дошли до onResumed/destroy
    private final Set<Fragment> pending =
            Collections.newSetFromMap(new ConcurrentHashMap<Fragment, Boolean>());

    private volatile IdlingResource.ResourceCallback callback;

    private NavigationIdlingResource() { }

    public synchronized void onPreAttached(Fragment f) {
        pending.add(f);
    }

    public synchronized void onResumed(Fragment f) {
        pending.remove(f);
        notifyIfIdle();
    }

    public synchronized void onDestroyed(Fragment f) {
        // если фрагмент был pre-attached, но так и не дошёл до resume
        pending.remove(f);
        notifyIfIdle();
    }

    // полный сброс — вызывается из BaseTest перед каждым тестом
    public synchronized void reset() {
        pending.clear();
        notifyIfIdle();
    }

    private void notifyIfIdle() {
        if (pending.isEmpty()) {
            ResourceCallback cb = callback;
            if (cb != null) {
                cb.onTransitionToIdle();
            }
        }
    }

    @Override
    public String getName() {
        return "NAVIGATION";
    }

    @Override
    public boolean isIdleNow() {
        return pending.isEmpty();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.callback = resourceCallback;
    }
}
