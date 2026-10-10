package ru.edu.qamid.ui.pageObject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.not;

import androidx.test.espresso.contrib.RecyclerViewActions;

import io.qameta.allure.kotlin.Allure;
import ru.edu.qamid.R;
import ru.edu.qamid.ui.data.CustomView;
import ru.edu.qamid.ui.data.Helper;

public class LoveIsAllPage {

    // элементы экрана Love is all
    private static final int PAGE_TITLE_ID = R.id.our_mission_title_text_view;
    private static final int QUOTES_RECYCLER = R.id.our_mission_item_list_recycler_view;
    private static final int QUOTE_TITLE = R.id.our_mission_item_title_text_view;
    private static final int QUOTE_DESCRIPTION = R.id.our_mission_item_description_text_view;
    private static final String OUR_MISSION_TITLE = "Love is all";

    public void checkPageTitleIsDisplayed() {
        Allure.step("Проверка отображения заголовка Love is all");
        onView(allOf(withId(PAGE_TITLE_ID), withText(OUR_MISSION_TITLE)))
                .check(matches(isDisplayed()));
    }

    public void checkQuotesListHasItems() {
        Allure.step("Проверка наличия в ленте хотя бы одной цитаты");
        Helper.checkRecyclerViewHasItems(QUOTES_RECYCLER);
    }

    public void checkQuoteDescriptionAtPositionIsDisplayed(int position) {
        Allure.step("Проверка, что описание цитаты на позиции " + position + " отображается");
        onView(withId(QUOTES_RECYCLER))
                .perform(RecyclerViewActions.actionOnItemAtPosition(
                        position,
                        CustomView.checkViewAction(
                                matches(hasDescendant(allOf(withId(QUOTE_DESCRIPTION), isDisplayed())))
                        )
                ));
    }

    public void checkQuoteDescriptionAtPositionIsNotDisplayed(int position) {
        Allure.step("Проверка, что описание цитаты на позиции " + position + " скрыто");
        onView(withId(QUOTES_RECYCLER))
                .perform(RecyclerViewActions.actionOnItemAtPosition(
                        position,
                        CustomView.checkViewAction(
                                matches(not(hasDescendant(allOf(withId(QUOTE_DESCRIPTION), isDisplayed()))))
                        )
                ));
    }

    public void tapOnQuoteByPosition(int position) {
        Allure.step("Нажатие на плашку цитаты на позиции " + position);
        onView(withId(QUOTES_RECYCLER))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position, click()));
    }
}