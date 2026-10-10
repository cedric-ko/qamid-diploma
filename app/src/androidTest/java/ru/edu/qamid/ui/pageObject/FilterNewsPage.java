package ru.edu.qamid.ui.pageObject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import io.qameta.allure.kotlin.Allure;
import ru.edu.qamid.R;
import ru.edu.qamid.ui.data.Helper;

public class FilterNewsPage {
    // элементы экрана FilterNews
    private static final int CATEGORY_FIELD = R.id.filter_news_category_auto_complete;
    private static final int DATE_START_FIELD = R.id.filter_news_date_start_edit_text;
    private static final int DATE_END_FIELD = R.id.filter_news_date_end_edit_text;
    private static final int ACTIVE_CHECK_BOX = R.id.filter_news_active_check_box;
    private static final int NOT_ACTIVE_CHECK_BOX = R.id.filter_news_inactive_check_box;
    private static final int APPLY_FILTER_BUTTON = R.id.filter_news_apply_button;
    private static final int CANCEL_BUTTON = R.id.filter_news_cancel_button;

    public void fillInCategory(String category) {
        Allure.step("Ввод категории " + category);
        Helper.selectFromDropdown(CATEGORY_FIELD, category);
    }

    public void fillInStartDate(int daysOffset) {
        Allure.step("Ввод даты начала периода: " + daysOffset + " дней от сегодня");
        onView(withId(DATE_START_FIELD))
                .check(matches(isDisplayed()))
                .perform(replaceText(Helper.getDate(daysOffset)));
    }

    public void fillInEndDate(int daysOffset) {
        Allure.step("Ввод даты окончания периода: " + daysOffset + " дней от сегодня");
        onView(withId(DATE_END_FIELD))
                .check(matches(isDisplayed()))
                .perform(replaceText(Helper.getDate(daysOffset)));
    }

    public void tapActiveCheckBox() {
        Allure.step("Нажатие на чек-бокс Active");
        onView(withId(ACTIVE_CHECK_BOX)).check(matches(isDisplayed())).perform(click());
    }

    public void tapNotActiveCheckBox() {
        Allure.step("Нажатие на чек-бокс Not active");
        onView(withId(NOT_ACTIVE_CHECK_BOX)).check(matches(isDisplayed())).perform(click());
    }

    public void tapApplyFilterButton() {
        Allure.step("Нажатие кнопки FILTER");
        onView(withId(APPLY_FILTER_BUTTON)).check(matches(isDisplayed())).perform(click());
    }

    public void tapCancelButton() {
        Allure.step("Нажатие кнопки CANCEL");
        onView(withId(CANCEL_BUTTON)).check(matches(isDisplayed())).perform(click());
    }

    public void checkFilterPageByApplyFilterButton() {
        Allure.step("Проверка по наличию кнопки FILTER, что открыт экран настройки фильтра");
        onView(withId(APPLY_FILTER_BUTTON)).check(matches(isDisplayed()));
    }
}
