package ru.edu.qamid.ui.pageObject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewInteraction;

import io.qameta.allure.kotlin.Allure;
import ru.edu.qamid.R;
import ru.edu.qamid.ui.data.Data;
import ru.edu.qamid.ui.data.Helper;

public class AuthorizationPage {
    // элементы экрана
    private static final ViewInteraction LOGIN_FIELD = onView((withId(R.id.login_edit_text)));
    private static final ViewInteraction PASSWORD_FIELD = onView((withId(R.id.password_edit_text)));
    private static final ViewInteraction SIGN_IN_BUTTON = onView(withId(R.id.enter_button));

    // метод проверки видимости кнопки SIGN IN, чтобы определить, что находимся на экране авторизации
    public boolean isSignInButtonDisplayed() {
        try {
            SIGN_IN_BUTTON.check(matches(isDisplayed()));
            return true;
        } catch (NoMatchingViewException | AssertionError e) {
            return false;
        }
    }

    // метод заполнения полей логина и пароля
    public void authorizationFieldsInput(String loginText, String passwordText) {
        Allure.step("Ввод логина " + loginText);
        LOGIN_FIELD.check(matches(isDisplayed())).perform(replaceText(loginText), closeSoftKeyboard());
        Allure.step("Ввод пароля " + passwordText);
        PASSWORD_FIELD.check(matches(isDisplayed())).perform(replaceText(passwordText), closeSoftKeyboard());
    }

    // метод нажатия на кнопку SIGN IN
    public void tapOnSignIn() {
        Allure.step("Нажатие кнопку SIGN IN");
        SIGN_IN_BUTTON.check(matches(isDisplayed())).perform(click());
    }

    // метод объединяет заполнение полей и нажатие на SIGN IN
    public void logIn(String loginText, String passwordText) {
        authorizationFieldsInput(loginText, passwordText);
        tapOnSignIn();
    }

    // метод отображения сообщения об ошибке
    public void checkErrorMessageDisplay() {
        Allure.step("Отображение сообщения об ошибке авторизации");
        Helper.checkToastMessage(Data.SOME_ERROR_MESSAGE);
    }

    // метод отображения ошибки о незаполненных полях
    public void checkEmptyFieldsErrorMessageDisplay() {
        Allure.step("Отображение сообщения о пустом поле ввода");
        Helper.checkToastMessage(Data.EMPTY_AUTH_ERROR_MESSAGE);
    }
}
