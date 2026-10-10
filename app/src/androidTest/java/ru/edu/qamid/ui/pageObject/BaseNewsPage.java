package ru.edu.qamid.ui.pageObject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.view.View;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;

import org.hamcrest.Matcher;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import io.qameta.allure.kotlin.Allure;
import ru.edu.qamid.R;
import ru.edu.qamid.ui.data.CustomView;
import ru.edu.qamid.ui.data.Data;
import ru.edu.qamid.ui.data.Helper;

public class BaseNewsPage {
    // класс содержит общие поля и методы классов NewsPage и ControlPanelPage
    public static final int NEWS_LIST = R.id.news_list_recycler_view;
    public static final int NEWS_ITEM_DATE = R.id.news_item_publication_date_text_view;
    public static final int NEWS_ITEM_CATEGORY = R.id.news_item_category_text_view;
    private static final int EXPAND_ITEM = R.id.news_item_expand_image_view;
    private static final ViewInteraction FILTER_BUTTON = onView(withId(R.id.news_filter_button));
    private static final ViewInteraction SORT_BUTTON = onView(withId(R.id.news_sort_button));
    private static final Matcher<View> PUBLICATION_DATE = withId(R.id.news_item_publication_date_text_view);
    private static final Matcher<View> DESCRIPTION_TEXT = withId(R.id.news_item_description_text_view);

    public void tapSortButton() {
        Allure.step("Нажатие кнопки сортировки");
        SORT_BUTTON.perform(click());
    }

    public void tapFilterButton() {
        Allure.step("Нажатие кнопки фильтрации новостей");
        FILTER_BUTTON.perform(click());
    }

    public void checkNewsFeedHasItems() {
        Allure.step("Проверка наличия в ленте хотя бы одной новости");
        Helper.checkRecyclerViewHasItems(NEWS_LIST);
    }

    public void scrollNewsFeedToTitle(String title) {
        Allure.step("Прокручивание ленты до новости с заголовком: " + title);
        onView(withId(NEWS_LIST)).check(matches(isDisplayed()));
        Helper.scrollRecyclerViewToItemWithText(NEWS_LIST, title);
    }

    public void checkNewsIsDisplayed(String title) {
        Allure.step("Проверка видимости новости по заголовку " + title);
        ViewInteraction titleView = onView(allOf(withText(title), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        titleView.check(matches(isDisplayed()));
    }

    public void hasNewsWithTitleExpectedDate(String title, String expectedDate) {
        Allure.step("Проверка даты новости с заголовком: " + title);
        Helper.scrollRecyclerViewToItem(NEWS_LIST,
                allOf(
                        hasDescendant(withText(title)),
                        hasDescendant(allOf(PUBLICATION_DATE, withText(expectedDate)))
                ));
    }

    public void hasNewsWithTitleExpectedDescription(String title, String expectedDescription) {
        Allure.step("Проверка описания новости с заголовком: " + title);
        Helper.scrollRecyclerViewToItem(NEWS_LIST,
                allOf(
                        hasDescendant(withText(title)),
                        hasDescendant(allOf(DESCRIPTION_TEXT, withText(expectedDescription)))
                ));
    }

    public void tapCollapseOrExpandNewsWithTitle(String title) {
        Allure.step("Сворачивание-разворачивание описания новости с заголовком: " + title);
        Helper.performOnRecyclerViewItem(
                NEWS_LIST,
                hasDescendant(withText(title)),
                CustomView.clickOnChildViewWithId(EXPAND_ITEM)
        );
    }

    public void checkNewsWithTitleIsPresent(String title) {
        Allure.step("Проверка, что новость с заголовком '" + title + "' присутствует в ленте");
        Helper.scrollRecyclerViewToItemWithText(NEWS_LIST, title);
        onView(withText(title)).check(matches(isDisplayed()));
    }

    public void checkNewsWithTitleIsAbsent(String title) {
        Allure.step("Проверка, что новость с заголовком '" + title + "' отсутствует в ленте");
        // проверяем отсутствие через булево значение (присутствует-отсутствует, да-нет),
        // и если проверка не проходит - выбрасываем исключение, чтобы тест не упал с ошибкой
        boolean isPresent;
        try {
            Helper.waitUntilTextGone(title, 5000);
            isPresent = false;
        } catch (PerformException e) {
            isPresent = true;
        }

        assertFalse("Новость с заголовком '" + title + "' не должна отображаться, но найдена", isPresent);
    }


    public int getNewsCount() {
        Allure.step("Получение количества элементов в ленте новостей");
        return Helper.getRecyclerViewItemCount(NEWS_LIST);
    }

    public String getNewsDateAtPosition(int position) {
        Allure.step("Получение даты новости по позиции " + position);
        return Helper.getTextFromRecyclerViewItem(NEWS_LIST, NEWS_ITEM_DATE, position);
    }

    public String getTopNewsDate() {
        Allure.step("Получение даты верхней новости ленты");
        return getNewsDateAtPosition(0);
    }

    public String getBottomNewsDate() {
        Allure.step("Получение даты нижней новости ленты");
        int count = getNewsCount();
        return count > 0 ? getNewsDateAtPosition(count - 1) : null;
    }

    public void checkAllNewsDateInRange(int fromDays, int toDays) {
        Allure.step("Проверка, что даты всех новостей в диапазоне от " + fromDays + " до " + toDays + " дней");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate startDate = LocalDate.parse(Helper.getDate(fromDays), formatter);
        LocalDate endDate = LocalDate.parse(Helper.getDate(toDays), formatter);

        int count = Helper.getRecyclerViewItemCount(NEWS_LIST);
        for (int i = 0; i < count; i++) {
            String actualDateStr = Helper.getTextFromRecyclerViewItem(NEWS_LIST, NEWS_ITEM_DATE, i);

            LocalDate actualDate = LocalDate.parse(actualDateStr, formatter);
            assertTrue("Новость на позиции " + i + " имеет дату " + actualDate + " вне диапазона",
                    !actualDate.isBefore(startDate) && !actualDate.isAfter(endDate));
        }
    }

    public void checkNewsFeedIsEmpty() {
        Allure.step("Проверка, что лента новостей пуста");
        onView(withText(Data.EMPTY_FEED_MESSAGE)).check(matches(isDisplayed()));
    }
}
