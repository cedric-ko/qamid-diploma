package ru.edu.qamid.ui.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.edu.qamid.ui.data.Data;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Тестирование экрана Love is all")
public class LoveIsAllTest extends BaseTest {

    @Before
    // перед каждым тестом идём на экран Love Is All
    public void navigateToLoveIsAllPage() {
        // авторизуемся
        authorizationPage.logIn(Data.VALID_LOGIN, Data.VALID_PASSWORD);
        // проверяем, что авторизовались - переход на главную страницу
        mainPage.checkAllNewsButtonIsDisplayed();
        // переходим на экран Love is all через кнопку в виде бабочки в топбаре
        topBar.tapOnLoveIsAllButton();
        // проверяем нахождение на экране Love Is All
        loveIsAllPage.checkPageTitleIsDisplayed();
    }

    @Test
    @DisplayName("Должна отображаться хотя бы одна цитата в ленте")
    public void shouldHaveItemsInQuotesList() {
        loveIsAllPage.checkQuotesListHasItems();
    }

    @Test
    @DisplayName("Цитата должна разворачиваться при тапе на плашку")
    public void shouldExpandQuoteOnTap() {
        // проверяем, что изначально описание цитаты на заданной позиции не видно
        loveIsAllPage.checkQuoteDescriptionAtPositionIsNotDisplayed(0);
        // тап по плашке
        loveIsAllPage.tapOnQuoteByPosition(0);
        // проверяем, что теперь описание цитаты на позиции видно
        loveIsAllPage.checkQuoteDescriptionAtPositionIsDisplayed(0);
    }

    @Test
    @DisplayName("Цитата должна сворачиваться при повторном тапе")
    public void shouldCollapseQuoteOnSecondTap() {
        loveIsAllPage.tapOnQuoteByPosition(0);
        loveIsAllPage.checkQuoteDescriptionAtPositionIsDisplayed(0);
        loveIsAllPage.tapOnQuoteByPosition(0);
        loveIsAllPage.checkQuoteDescriptionAtPositionIsNotDisplayed(0);
    }
}