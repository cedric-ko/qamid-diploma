package ru.edu.qamid.ui.pageObject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;

import io.qameta.allure.kotlin.Allure;
import ru.edu.qamid.R;
import ru.edu.qamid.ui.data.CustomView;
import ru.edu.qamid.ui.data.Helper;

public class ControlPanelPage extends BaseNewsPage {
    private static final ViewInteraction ADD_NEWS_BUTTON = onView(withId(R.id.add_news_image_view));
    private static final int EDIT_BUTTON = R.id.news_item_edit_image_view;
    private static final int DELETE_BUTTON = R.id.news_item_delete_image_view;
    private static final int NEWS_STATUS_VIEW = R.id.news_item_published_text_view;
    private static final ViewInteraction DIALOG_OK =
            onView(allOf(withId(android.R.id.button1), withText("OK"))).inRoot(isDialog());
    public static final String DIALOG_CANCEL = "CANCEL";
    public static final String NEWS_STATUS_ACTIVE = "ACTIVE";
    public static final String NEWS_STATUS_NOT_ACTIVE = "NOT ACTIVE";

    public void checkControlPanelPageByAddNewsButton() {
        Allure.step("Проверка по наличию кнопки добавления новости, что открыт экран Control panel");
        Helper.waitUntilVisible(
                allOf(withId(R.id.add_news_image_view), withEffectiveVisibility(VISIBLE)),
                10000);
        ADD_NEWS_BUTTON.check(matches(isDisplayed()));
    }

    public void tapCreatingNewsButton() {
        Allure.step("Нажатие кнопки добавления новости");
        Helper.waitUntilVisible(
                allOf(withId(R.id.add_news_image_view), withEffectiveVisibility(VISIBLE)),
                10000);
        ADD_NEWS_BUTTON.check(matches(isDisplayed())).perform(click());
    }

    public void tapEditNewsWithTitle(String title) {
        Allure.step("Нажатие кнопки редактирования новости с заголовком " + title);
        onView(withId(NEWS_LIST))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(title)),
                        CustomView.clickOnChildViewWithId(EDIT_BUTTON)
                ));
    }

    public void isNewsActive(String title) {
        Allure.step("Проверка, что новость " + title + " активна");
        onView(withId(NEWS_LIST))
                .perform(RecyclerViewActions.scrollTo(
                        allOf(
                                hasDescendant(withText(title)),
                                hasDescendant(allOf(
                                        withId(NEWS_STATUS_VIEW),
                                        withText(NEWS_STATUS_ACTIVE)
                                ))
                        )
                ));
    }

    public void isNewsNotActive(String title) {
        Allure.step("Проверка, что новость " + title + " неактивна");
        onView(withId(NEWS_LIST))
                .perform(RecyclerViewActions.scrollTo(
                        allOf(
                                hasDescendant(withText(title)),
                                hasDescendant(allOf(
                                        withId(NEWS_STATUS_VIEW),
                                        withText(NEWS_STATUS_NOT_ACTIVE)
                                ))
                        )
                ));
    }

    public void deleteNewsWithTitle(String title) {
        Allure.step("Удаление новости с заголовком: " + title);

        onView(withId(NEWS_LIST))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(title)),
                        CustomView.clickOnChildViewWithId(DELETE_BUTTON)
                ));

        DIALOG_OK.check(matches(isDisplayed())).perform(CustomView.performClickOnView());

        Helper.waitUntilGone(android.R.id.button1, 5000); // столь большое значение таймаута вызвано медленной работой эмулятора
    }

    public void tapDeleteButton(String title) {
        Allure.step("Нажатие кнопки удаления новости с заголовком: " + title);
        onView(withId(NEWS_LIST))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(title)),
                        CustomView.clickOnChildViewWithId(DELETE_BUTTON)
                ));
    }
    public void cancelToDeleteNews() {
        Allure.step("Отказ от удаления новости");
        onView(withText(DIALOG_CANCEL))
                .inRoot(isDialog())
                .check(matches(isDisplayed())).perform(CustomView.performClickOnView());
    }

    public void checkAllNewsHaveStatus(String expectedStatus) {
        Allure.step("Проверка, что все новости имеют статус: " + expectedStatus);
        int count = Helper.getRecyclerViewItemCount(NEWS_LIST);
        for (int i = 0; i < count; i++) {
            String actualStatus = Helper.getTextFromRecyclerViewItem(NEWS_LIST, NEWS_STATUS_VIEW, i);
            assertEquals("Новость на позиции " + i + " должна иметь статус " + expectedStatus,
                    expectedStatus, actualStatus);
        }
    }
}
