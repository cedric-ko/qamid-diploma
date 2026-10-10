package ru.edu.qamid.ui.pageObject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;

import io.qameta.allure.kotlin.Allure;
import ru.edu.qamid.R;
import ru.edu.qamid.ui.data.CustomView;
import ru.edu.qamid.ui.data.Data;
import ru.edu.qamid.ui.data.Helper;

// класс содержит общие поля и методы классов CreatingNewsPage и EditingNewsPage
public class BaseCreateEditPage {
    private static final int CATEGORY_FIELD = R.id.news_category_auto_complete;
    private static final ViewInteraction TITLE_FIELD = onView(withId(R.id.news_title_edit_text));
    private static final ViewInteraction PUBLISH_DATE = onView(withId(R.id.news_publish_date_edit_text));
    private static final ViewInteraction PUBLISH_TIME = onView(withId(R.id.news_publish_time_edit_text));
    private static final ViewInteraction DESCRIPTION_FIELD = onView(withId(R.id.news_description_edit_text));
    private static final ViewInteraction SAVE_BUTTON = onView(withId(R.id.news_save_button));
    private static final ViewInteraction CANCEL_BUTTON = onView(withId(R.id.news_cancel_button));
    private static final ViewInteraction OK_IN_DIALOG =
            onView(withText("OK")).inRoot(isDialog());
    private static final ViewInteraction CANCEL_IN_DIALOG =
            onView(withText("CANCEL")).inRoot(isDialog());

    public void fillInNewsCategory(String category) {
        Allure.step("Ввод категории новости " + category);
        Helper.selectFromDropdown(CATEGORY_FIELD, category);
    }

    public void fillInNewsTitle(String title) {
        Allure.step("Ввод заголовка новости " + title);
        TITLE_FIELD.check(matches(isDisplayed()))
                .perform(replaceText(title));
    }

    public void setPublishDate(String date) {
        Allure.step("Ввод даты публикации " + date);
        PUBLISH_DATE.check(matches(isDisplayed()))
                .perform(replaceText(date));
    }

    public void setPublishTime(String time) {
        Allure.step("Ввод времени публикации " + time);
        PUBLISH_TIME.check(matches(isDisplayed())).perform(replaceText(time));
    }

    public void fillInNewsDescription(String description) {
        Allure.step("Ввод описания новости " + description);
        DESCRIPTION_FIELD.check(matches(isDisplayed())).perform(replaceText(description));
    }

    public void tapSave() {
        Allure.step("Сохранение новости");
        SAVE_BUTTON.check(matches(isDisplayed())).perform(click());
    }

    public void fillInNewsFields(String category, String title, String date, String time, String description) {
        // метод объединяет методы заполнения формы создания новости
        fillInNewsCategory(category);
        fillInNewsTitle(title);
        setPublishDate(date);
        setPublishTime(time);
        fillInNewsDescription(description);
    }

    public void checkErrorMessage() {
        Allure.step("Сообщение о незаполненных обязательных полях при создании/ редактировании новости");
        Helper.checkToastMessage(Data.EMPTY_FIELDS_ERROR_MESSAGE);
    }

    public void tapCancel() {
        Allure.step("Отмена создания/ редактирования новости");
        CANCEL_BUTTON.check(matches(isDisplayed())).perform(click());
    }

    public void tapCancelInDialog() {
        Allure.step("Нажатие CANCEL в диалоговом окне отмены создания/ редактирования новости");
        CANCEL_IN_DIALOG.check(matches(isDisplayed())).perform(CustomView.performClickOnView());

    }
    public void tapOkInDialog() {
        Allure.step("Нажатие OK в диалоговом окне отмены создания/ редактирования новости");
        OK_IN_DIALOG.check(matches(isDisplayed())).perform(CustomView.performClickOnView());
    }
}
