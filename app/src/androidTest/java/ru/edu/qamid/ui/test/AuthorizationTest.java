package ru.edu.qamid.ui.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.edu.qamid.ui.data.Data;

// запускаем тесты под JUnit4 для Android с сопровождением Allure
@RunWith(AllureAndroidJUnit4.class)
// название данного блока тестов
@Epic("Тестирование экрана авторизации")
public class AuthorizationTest extends BaseTest {

    // перед каждым тестом проверяем выход на экран авторизации
    @Before
    public void verifyOnLoginPage() {
        if (!authorizationPage.isSignInButtonDisplayed()) {
            throw new AssertionError("Не удалось выйти на экран авторизации");
        }
    }

    @Test
    @DisplayName("Авторизация с логином и паролем зарегистрированного пользователя")
    public void validAuthorizationTest() {
        authorizationPage.logIn(Data.VALID_LOGIN, Data.VALID_PASSWORD);
        assertTrue("Главный экран не отображается после авторизации", mainPage.isAllNewsVisible());
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    public void wrongLoginAuthorizationTest() {
        authorizationPage.logIn(Data.WRONG_LOGIN, Data.VALID_PASSWORD);
        authorizationPage.checkErrorMessageDisplay();
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void wrongPasswordAuthorizationTest() {
        authorizationPage.logIn(Data.VALID_LOGIN, Data.WRONG_PASSWORD);
        authorizationPage.checkErrorMessageDisplay();
    }

    @Test
    @DisplayName("Авторизация с пробелом в поле Login")
    public void spaceLoginAuthorizationTest() {
        authorizationPage.logIn(Data.SPACE_FIELD, Data.VALID_PASSWORD);
        authorizationPage.checkEmptyFieldsErrorMessageDisplay();
    }

    @Test
    @DisplayName("Авторизация с пробелом в поле Password")
    public void spacePasswordAuthorizationTest() {
        authorizationPage.logIn(Data.VALID_LOGIN, Data.SPACE_FIELD);
        authorizationPage.checkEmptyFieldsErrorMessageDisplay();
    }

    @Test
    @DisplayName("Авторизация с незаполненным полем Login")
    public void emptyLoginAuthorizationTest() {
        authorizationPage.logIn(Data.EMPTY_FIELD, Data.VALID_PASSWORD);
        authorizationPage.checkEmptyFieldsErrorMessageDisplay();
    }

    @Test
    @DisplayName("Авторизация с незаполненным полем Password")
    public void emptyPasswordAuthorizationTest() {
        authorizationPage.logIn(Data.VALID_LOGIN, Data.EMPTY_FIELD);
        authorizationPage.checkEmptyFieldsErrorMessageDisplay();
    }

    @Test
    @DisplayName("Авторизация с валидным логином, введённым символами разного регистра")
    public void difCasesLoginAuthorizationTest() {
        authorizationPage.logIn(Data.DIF_CASES_LOGIN, Data.VALID_PASSWORD);
        authorizationPage.checkErrorMessageDisplay();
    }

    @Test
    @DisplayName("Авторизация с валидным паролем, введённым символами разного регистра")
    public void difCasesPasswordAuthorizationTest() {
        authorizationPage.logIn(Data.VALID_LOGIN, Data.DIF_CASES_PASSWORD);
        authorizationPage.checkErrorMessageDisplay();
    }
}
