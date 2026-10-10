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
@Epic("Тестирование страницы редактирования новости")
public class EditingNewsTest extends BaseTest {

    private static final String category = Data.ANNOUNCEMENT_CATEGORY;
    private final String title = Data.generateUniqueTitle();
    private final String editTitle = Data.generateUniqueTitle() + "Edited";
    // текущая дата
    private final String date = Helper.getDate(0);
    // завтрашний день
    private final String futureDate = Helper.getDate(1);
    // текущее время
    private final String time = Helper.getTime(0, 0);
    private final String description = Data.generateUniqueDescription();
    private final String editDescription = Data.generateUniqueDescription() + "Edited";

    @Before
    // перед каждым тестом идём на экран редактирования новости
    public void navigateToEditingNewsPage() {
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
        // заполняем поля создания новости
        creatingNewsPage.fillInNewsFields(category, title, date, time, description);
        // нажимаем кнопку сохранения
        creatingNewsPage.tapSave();
        // проверяем, что перешли на Control panel
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        // находим новость и тапаем кнопку редактирования
        controlPanelPage.tapEditNewsWithTitle(title);
        // проверяем, что мы на экране редактирования
        editingNewsPage.checkEditingNewsPageByTopBarTitle();
    }

    @Test
    @DisplayName("Должен отредактировать категорию новости")
    public void shouldEditNewsCategory() {
        editingNewsPage.fillInNewsCategory(Data.BIRTHDAY_CATEGORY);
        // вводим также и заголовок, чтобы автозамена не уронила тест
        editingNewsPage.fillInNewsTitle(title);
        editingNewsPage.tapSave();
        // проверяем переход на Control panel
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        // ищем отредактированную новость по заголовку, так как
        // в ленте категория новости отображается графическим значком
        controlPanelPage.scrollNewsFeedToTitle(title);
        controlPanelPage.checkNewsIsDisplayed(title);
        //удаляем тестовую новость
        controlPanelPage.deleteNewsWithTitle(title);
        controlPanelPage.checkNewsWithTitleIsAbsent(title);
    }

    @Test
    @DisplayName("Должен отредактировать заголовок новости")
    public void shouldEditNewsTitle() {
        editingNewsPage.fillInNewsTitle(editTitle);
        editingNewsPage.tapSave();
        // проверяем переход на Control panel
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        controlPanelPage.scrollNewsFeedToTitle(editTitle);
        controlPanelPage.checkNewsIsDisplayed(editTitle);
        //удаляем тестовую новость
        controlPanelPage.deleteNewsWithTitle(editTitle);
        controlPanelPage.checkNewsWithTitleIsAbsent(editTitle);
    }

    @Test
    @DisplayName("Должен отредактировать дату новости")
    public void shouldEditNewsDate() {
        editingNewsPage.setPublishDate(futureDate);
        editingNewsPage.tapSave();
        // проверяем переход на Control panel
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        controlPanelPage.scrollNewsFeedToTitle(title);
        controlPanelPage.hasNewsWithTitleExpectedDate(title, futureDate);
        //удаляем тестовую новость
        controlPanelPage.deleteNewsWithTitle(title);
        controlPanelPage.checkNewsWithTitleIsAbsent(title);
    }

    @Test
    @DisplayName("Должен отредактировать описание новости")
    public void shouldEditNewsDescription() {
        editingNewsPage.fillInNewsDescription(editDescription);
        editingNewsPage.tapSave();
        // проверяем переход на Control panel
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        controlPanelPage.tapCollapseOrExpandNewsWithTitle(title);
        controlPanelPage.hasNewsWithTitleExpectedDescription(title, editDescription);
        //удаляем тестовую новость
        controlPanelPage.deleteNewsWithTitle(title);
        controlPanelPage.checkNewsWithTitleIsAbsent(title);
    }

    @Test
    @DisplayName("Должен изменить статус новости на Not Active")
    public void shouldEditNewsStatusOnNotActive() {
        editingNewsPage.switchActivityStatus();
        editingNewsPage.tapSave();
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        controlPanelPage.isNewsNotActive(title);
        //удаляем тестовую новость
        controlPanelPage.deleteNewsWithTitle(title);
        controlPanelPage.checkNewsWithTitleIsAbsent(title);
    }

    @Test
    @DisplayName("Должен изменить статус неактивной новости на Active")
    public void shouldEditNotActiveNewsStatusOnActive() {
        // делаем новость неактивной
        editingNewsPage.switchActivityStatus();
        editingNewsPage.tapSave();
        // находим новость по заголовку и снова редактируем
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        controlPanelPage.isNewsNotActive(title);
        controlPanelPage.tapEditNewsWithTitle(title);
        // снова переключаем статус - должен измениться на Active
        editingNewsPage.switchActivityStatus();
        editingNewsPage.tapSave();
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        controlPanelPage.isNewsActive(title);
        //удаляем тестовую новость
        controlPanelPage.deleteNewsWithTitle(title);
        controlPanelPage.checkNewsWithTitleIsAbsent(title);
    }

    @Test
    @DisplayName("Не должен сохранить отредактированную новость с незаполненными полями")
    public void shouldNotSaveEditedNewsWithEmptyFields() {
        editingNewsPage.fillInNewsFields(Data.EMPTY_FIELD, Data.EMPTY_FIELD, Data.EMPTY_FIELD, Data.EMPTY_FIELD, Data.EMPTY_FIELD);
        editingNewsPage.tapSave();
        editingNewsPage.checkErrorMessage();
        //удаляем тестовую новость
        editingNewsPage.tapCancel();
        editingNewsPage.tapOkInDialog();
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        controlPanelPage.deleteNewsWithTitle(title);
        controlPanelPage.checkNewsWithTitleIsAbsent(title);
    }

    @Test
    @DisplayName("Не должен сохранять изменения при отказе от редактирования")
    public void shouldNotSaveNewsAfterCancelOfEditing() {
        // редактируем заголовок
        editingNewsPage.fillInNewsTitle(editTitle);
        editingNewsPage.tapCancel();
        // в открывшемся диалоговом окне нажимаем OK
        editingNewsPage.tapOkInDialog();
        // проверяем, что заголовок новости не изменился
        controlPanelPage.checkNewsWithTitleIsAbsent(editTitle);
        controlPanelPage.checkNewsWithTitleIsPresent(title);
        //удаляем тестовую новость
        controlPanelPage.deleteNewsWithTitle(title);
        controlPanelPage.checkNewsWithTitleIsAbsent(title);
    }

    @Test
    @DisplayName("Должен вернуться к редактированию новости, если нажать CANCEL при отказе от редактирования")
    public void shouldReturnToEditingNewsAfterCancelInDialog() {
        editingNewsPage.tapCancel();
        // в открывшемся диалоговом окне нажимаем CANCEL
        editingNewsPage.tapCancelInDialog();
        // проверяем возвращение на экран редактирования новости
        editingNewsPage.checkEditingNewsPageByTopBarTitle();
        //удаляем тестовую новость
        editingNewsPage.tapCancel();
        editingNewsPage.tapOkInDialog();
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        controlPanelPage.deleteNewsWithTitle(title);
        controlPanelPage.checkNewsWithTitleIsAbsent(title);
    }
}
