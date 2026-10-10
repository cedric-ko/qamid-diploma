package ru.edu.qamid.ui.pageObject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;

import io.qameta.allure.kotlin.Allure;
import ru.edu.qamid.R;
import ru.edu.qamid.ui.data.Helper;


public class TopBar {
    private static final ViewInteraction MENU_BUTTON = onView(withId(R.id.main_menu_image_button));
    private static final ViewInteraction NEWS_BUTTON = onView(withText("News"));
    private static final ViewInteraction MAIN_BUTTON = onView(withText("Main"));
    private static final ViewInteraction ABOUT_BUTTON = onView(withText("About"));
    private static final ViewInteraction LOVE_IS_ALL_BUTTON = onView(withId(R.id.our_mission_image_button));
    private static final ViewInteraction AUTH_BUTTON = onView(withId(R.id.authorization_image_button));
    private static final ViewInteraction LOG_OUT = onView(withText("Log out"));


    public void tapOnAppMenu() {
        Allure.step("Нажатие на меню приложения");
        Helper.waitUntilVisible(
                allOf(withId(R.id.main_menu_image_button),
                        withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)),
                10000);
        MENU_BUTTON.check(matches(isDisplayed())).perform(click());
    }

    public void tapOnMain() {
        Allure.step("Нажатие Main в выпадающем меню");
        MAIN_BUTTON.check(matches(isDisplayed())).perform(click());
    }

    // переход на главный экран, метод объединяет нажатие на меню и нажатие Main  в нём
    public void goToMain() {
        tapOnAppMenu();
        tapOnMain();
    }

    public void tapOnNews() {
        Allure.step("Выбрать раздел News из списка разделов");
        NEWS_BUTTON.check(matches(isDisplayed())).perform(click());
    }

    // переход на экран новостей, метод объединяет нажатие на меню и нажатие News  в нём
    public void goToNews() {
        tapOnAppMenu();
        tapOnNews();
    }

    public void checkLoveIsAllButtonIsVisible() {
        Allure.step("Проверка видимости кнопки перехода на экран Love Is All");
        LOVE_IS_ALL_BUTTON.check(matches(isDisplayed()));
    }

    public void tapOnLoveIsAllButton() {
        Allure.step("Нажатие на символ бабочки в верхней панели приложения");
        LOVE_IS_ALL_BUTTON.check(matches(isDisplayed())).perform(click());
    }

    public void tapOnProfileImage() {
        Allure.step("Нажатие на символ человека");
        AUTH_BUTTON.check(matches(isDisplayed())).perform(click());
    }

    public void tapOnLogOut() {
        Allure.step("Нажатие на выход из учётки Log out");
        LOG_OUT.check(matches(isDisplayed())).perform(click());
    }

    // метод разлогинивания, объединяет нажатие на символ человека и, затем, нажатие на Log out
    public void logOut() {
        tapOnProfileImage();
        tapOnLogOut();
    }
}
