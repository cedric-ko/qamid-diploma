package ru.edu.qamid.ui.pageObject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;

import io.qameta.allure.kotlin.Allure;
import ru.edu.qamid.R;
import ru.edu.qamid.ui.data.Helper;

public class NewsPage extends BaseNewsPage {

    private static final ViewInteraction CONTROL_PANEL_BUTTON = onView(withId(R.id.news_edit_button));

    public void checkControlPanelButtonIsDisplayed() {
        Allure.step("Проверка отображения кнопки Control panel");
        Helper.waitUntilVisible(withId(R.id.news_edit_button), 10000);
        CONTROL_PANEL_BUTTON.check(matches(isDisplayed()));
    }

    public void tapControlPanelButton() {
        Allure.step("Нажатие кнопки Control panel");
        Helper.waitUntilVisible(
                allOf(withId(R.id.news_edit_button), withEffectiveVisibility(VISIBLE)),
                10000);
        CONTROL_PANEL_BUTTON.check(matches(isDisplayed())).perform(click());
    }
}
