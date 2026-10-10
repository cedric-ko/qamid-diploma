package ru.edu.qamid.ui.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.edu.qamid.ui.data.Data;
import ru.edu.qamid.ui.data.Helper;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Тестирование экрана создания новости")
public class CreatingNewsTest extends BaseTest {

    private static final String category = Data.ANNOUNCEMENT_CATEGORY;
    private final String title = Data.generateUniqueTitle();
    // текущая дата
    private final String date = Helper.getDate(0);
    // вчерашний день
    private final String pastDate = Helper.getDate(-1);
    // завтрашний день
    private final String futureDate = Helper.getDate(1);
    // текущее время
    private final String time = Helper.getTime(0, 0);
    private final String description = Data.generateUniqueDescription();

    @Before
    // перед каждым тестом идём на экран создания новости
    public void navigateToCreatingNewsPage() {
        // авторизуемся
        authorizationPage.logIn(Data.VALID_LOGIN, Data.VALID_PASSWORD);
        // через меню приложения переходим на экран News
        topBar.goToNews();
        // проверяем видимость кнопки Control panel
        newsPage.checkControlPanelButtonIsDisplayed();
        // нажимаем кнопку Control panel
        newsPage.tapControlPanelButton();
        // проверяем, что мы на экране Control panel - видим кнопку добавления новости
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        // нажимаем кнопку добавления новости
        controlPanelPage.tapCreatingNewsButton();
        // проверяем, что мы на экране создания новости
        creatingNewsPage.checkCreatingNewsPageByTopBarTitle();
    }

    @Test
    @DisplayName("Должен создать новость")
    public void shouldCreateNews() {
        creatingNewsPage.fillInNewsFields(category, title, date, time, description);
        creatingNewsPage.tapSave();
        // проверяем переход на Control panel
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        controlPanelPage.scrollNewsFeedToTitle(title);
        controlPanelPage.checkNewsIsDisplayed(title);
        //удаляем тестовую новость
        controlPanelPage.deleteNewsWithTitle(title);
        controlPanelPage.checkNewsWithTitleIsAbsent(title);
    }

    @Test
    @DisplayName("Должен создать новость от прошедшего дня")
    public void shouldCreateNewsByPast() {
        // создаём новость со вчерашней датой
        creatingNewsPage.fillInNewsFields(category, title, pastDate, time, description);
        creatingNewsPage.tapSave();
        // находим новость по заголовку и проверяем, что она отображается
        controlPanelPage.scrollNewsFeedToTitle(title);
        controlPanelPage.checkNewsIsDisplayed(title);
        // проверяем, что фактическая дата публикации совпадает с введённой
        controlPanelPage.hasNewsWithTitleExpectedDate(title, pastDate);
        // удаляем тестовую новость
        controlPanelPage.deleteNewsWithTitle(title);
        controlPanelPage.checkNewsWithTitleIsAbsent(title);
    }

    @Test
    @DisplayName("Должен создать новость от завтрашнего дня")
    public void shouldCreateNewsByFuture() {
        // создаём новость с завтрашней датой
        creatingNewsPage.fillInNewsFields(category, title, futureDate, time, description);
        creatingNewsPage.tapSave();
        // находим новость по заголовку и проверяем, что она отображается
        controlPanelPage.scrollNewsFeedToTitle(title);
        controlPanelPage.checkNewsIsDisplayed(title);
        // проверяем, что фактическая дата публикации совпадает с введённой
        controlPanelPage.hasNewsWithTitleExpectedDate(title, futureDate);
        // удаляем тестовую новость
        controlPanelPage.deleteNewsWithTitle(title);
        controlPanelPage.checkNewsWithTitleIsAbsent(title);
    }

    @Test
    @DisplayName("Переключатель Active на экране создания новости включён и заблокирован")
    public void activeSwitchShouldBeOnAndDisabled() {
        creatingNewsPage.checkActiveSwitchIsOnAndDisabled();
    }

    @Test
    @DisplayName("Не должен создать новость с незаполненными полями")
    public void shouldNotCreateNewsWithEmptyFields() {
        creatingNewsPage.tapSave();
        creatingNewsPage.checkErrorMessage();
    }

    @Test
    @DisplayName("Не должен сохранять новость при отказе от её создания")
    public void shouldNotSaveNewsAfterCancelOfCreating() {
        creatingNewsPage.fillInNewsFields(category, title, date, time, description);
        // заполнили поля создания новости и нажимаем CANCEL
        creatingNewsPage.tapCancel();
        // в открывшемся диалоговом окне нажимаем OK
        creatingNewsPage.tapOkInDialog();
        // проверяем, что в ленте отсутствует несохранённая новость
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        controlPanelPage.checkNewsWithTitleIsAbsent(title);
    }

    @Test
    @DisplayName("Должен вернуться к созданию новости, если нажать CANCEL при отказе от создания")
    public void shouldReturnToCreatingNewsAfterCancelInDialog() {
        creatingNewsPage.fillInNewsFields(category, title, date, time, description);
        // заполнили поля создания новости и нажимаем CANCEL
        creatingNewsPage.tapCancel();
        // в открывшемся диалоговом окне нажимаем CANCEL
        creatingNewsPage.tapCancelInDialog();
        // проверяем возвращение на экран создания новости
        creatingNewsPage.checkCreatingNewsPageByTopBarTitle();
    }
}
