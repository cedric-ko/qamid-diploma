package ru.edu.qamid.ui.pageObject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewInteraction;

import io.qameta.allure.kotlin.Allure;
import ru.edu.qamid.R;
import ru.edu.qamid.ui.data.Helper;

public class MainPage {

    private static final ViewInteraction ALL_NEWS_BUTTON = onView(withId(R.id.all_news_text_view));

    private static final ViewInteraction EXPAND_BUTTON = onView(withId(R.id.expand_material_button));

    public void checkAllNewsButtonIsDisplayed() {
        Allure.step("Проверка, что видна кнопка ALL NEWS, значит, мы на главном экране");
        Helper.waitUntilVisible(
                allOf(withId(R.id.all_news_text_view), withEffectiveVisibility(VISIBLE)),
                10000);
        ALL_NEWS_BUTTON.check(matches(isDisplayed()));
    }

    public void collapseOrExpandNewsFeed() {
        Allure.step("Сворачивание-разворачивание ленты новостей");
        EXPAND_BUTTON.check(matches(isDisplayed())).perform(click());
    }

    public boolean isAllNewsVisible() {
        Allure.step("Проверка видимости элемента ALL NEWS");
        try {
            ALL_NEWS_BUTTON.check(matches(isDisplayed()));
            return true;
        } catch (NoMatchingViewException | AssertionError e) {
            return false;
        }
    }

    public void tapOnAllNewsButton() {
        Allure.step("Нажатие на элемент ALL NEWS");
        ALL_NEWS_BUTTON.check(matches(isDisplayed())).perform(click());
    }
}
