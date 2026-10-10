package ru.edu.qamid.ui.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.edu.qamid.ui.data.Data;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Тестирование экрана новостей")
public class NewsTest extends BaseTest {

    @Before
    // перед каждым тестом идём на экран новостей
    public void navigateToNewsPage() {
        // авторизуемся
        authorizationPage.logIn(Data.VALID_LOGIN, Data.VALID_PASSWORD);
        // через меню приложения переходим на экран News
        topBar.goToNews();
        // проверяем, что находимся на экране News по видимости кнопки Control panel
        newsPage.checkControlPanelButtonIsDisplayed();
    }

    // функции сортировки новости тестируются в классе ControlPanelPage - это
    // позволяет избежать постоянного переключения между News - Control panel -
    // Creating News - и обратно, что усложнит тест

    @Test
    @DisplayName("Должна отображаться хотя бы одна новость в ленте новостей")
    public void newsFeedShouldHaveItems() {
        newsPage.checkNewsFeedHasItems();
    }

    @Test
    @DisplayName("Должен перейти с экрана новостей на главный экран через меню приложения")
    public void shouldGoToMainByAppMenu() {
        topBar.goToMain();
        assertTrue("Главный экран не отображается", mainPage.isAllNewsVisible());
    }
}