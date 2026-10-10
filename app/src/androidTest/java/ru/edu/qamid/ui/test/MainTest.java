package ru.edu.qamid.ui.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.edu.qamid.ui.data.Data;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Тестирование главного экрана")
public class MainTest extends BaseTest {

    @Before
    // перед каждым тестом идём на главный экран
    public void navigateToMainPage() {
        // авторизуемся
        authorizationPage.logIn(Data.VALID_LOGIN, Data.VALID_PASSWORD);
        // проверяем, что попали на главный экран по кнопке ALL NEWS, которой нет на других экранах
        mainPage.checkAllNewsButtonIsDisplayed();

        // если лента свёрнута — разворачиваем, чтобы стартовое состояние было известно
        if (!mainPage.isAllNewsVisible()) {
            mainPage.collapseOrExpandNewsFeed();
        }
    }

    @Test
    @DisplayName("Должен перейти на News при нажатии на ALL NEWS")
    public void shouldGoToNewsPageByAllNewsTap() {
        mainPage.tapOnAllNewsButton();
        // проверяем переход на News по видимости кнопки Control panel
        newsPage.checkControlPanelButtonIsDisplayed();
    }

    @Test
    @DisplayName("Лента новостей должна сворачиваться")
    public void shouldCollapseNewsFeed() {
        mainPage.collapseOrExpandNewsFeed();
        assertFalse("Лента новостей не свернулась", mainPage.isAllNewsVisible());
    }

    @Test
    @DisplayName("Свёрнутая лента новостей должна разворачиваться")
    public void shouldExpandCollapsedNewsFeed() {
        // сворачиваем ленту
        mainPage.collapseOrExpandNewsFeed();
        // разворачиваем обратно
        mainPage.collapseOrExpandNewsFeed();
        assertTrue("Лента новостей не развернулась", mainPage.isAllNewsVisible());
    }
}