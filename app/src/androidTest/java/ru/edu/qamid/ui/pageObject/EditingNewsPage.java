package ru.edu.qamid.ui.pageObject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;

import io.qameta.allure.kotlin.Allure;
import ru.edu.qamid.R;

public class EditingNewsPage extends BaseCreateEditPage {
    private static final ViewInteraction ACTIVE_SWITCH = onView(withId(R.id.news_active_switch));

    public void switchActivityStatus() {
        Allure.step("Переключение статуса активности");
        ACTIVE_SWITCH.check(matches(isDisplayed())).perform(click());
    }

    public void checkEditingNewsPageByTopBarTitle() {
        Allure.step("Проверка нахождения на экране редактирования новости");
        onView(allOf(withId(R.id.custom_app_bar_title_text_view), withText("Editing")))
                .check(matches(isDisplayed()));
    }
}
