package ru.edu.qamid.ui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeFalse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.edu.qamid.ui.data.Data;
import ru.edu.qamid.ui.data.Helper;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Тестирование экрана Control panel")
public class ControlPanelTest extends BaseTest {

    private static final String category = Data.ANNOUNCEMENT_CATEGORY;
    private final String title = Data.generateUniqueTitle();
    // вчерашний день
    private final String pastDate = Helper.getDate(-1);
    // текущая дата
    private final String date = Helper.getDate(0);
    // завтрашний день
    private final String futureDate = Helper.getDate(1);
    // текущее время
    private final String time = Helper.getTime(0, 0);
    private final String description = Data.generateUniqueDescription();

    @Before
    // перед каждым тестом идём на Control panel
    public void navigateToControlPanelPage() {
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
    }

    @Test
    @DisplayName("Должен удалить новость")
    public void shouldDeleteNews() {
        // создаём новость для удаления
        controlPanelPage.tapCreatingNewsButton();
        // проверяем, что мы на экране создания новости
        creatingNewsPage.checkCreatingNewsPageByTopBarTitle();
        // создаём новость
        creatingNewsPage.fillInNewsFields(category, title, date, time, description);
        creatingNewsPage.tapSave();
        // проверяем, что мы на экране Control panel
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        // проматываем ленту до созданной новости
        controlPanelPage.scrollNewsFeedToTitle(title);
        // удаляем новость с заданным заголовком
        controlPanelPage.deleteNewsWithTitle(title);
        // проверяем удаление
        controlPanelPage.checkNewsWithTitleIsAbsent(title);
    }

    @Test
    @DisplayName("Не должен удалить новость при нажатии CANCEL")
    public void shouldNotDeleteNewsOnCancel() {
        // создаём новость для удаления
        controlPanelPage.tapCreatingNewsButton();
        // проверяем, что мы на экране создания новости
        creatingNewsPage.checkCreatingNewsPageByTopBarTitle();
        // создаём новость
        creatingNewsPage.fillInNewsFields(category, title, date, time, description);
        creatingNewsPage.tapSave();
        // проверяем, что мы на экране Control panel
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        // проматываем ленту до созданной новости
        controlPanelPage.scrollNewsFeedToTitle(title);
        // нажимаем кнопку удаления новости
        controlPanelPage.tapDeleteButton(title);
        // нажимаем CANCEL
        controlPanelPage.cancelToDeleteNews();
        // проверяем, что новость не удалена
        controlPanelPage.checkNewsWithTitleIsPresent(title);

        //теперь удаляем тестовую новость
        controlPanelPage.deleteNewsWithTitle(title);
        controlPanelPage.checkNewsWithTitleIsAbsent(title);
    }

    @Test
    @DisplayName("Должен сортировать новости по возрастанию даты (сверху старые новости) в Control panel")
    public void shouldSortAscendingOnControlPanelTest() {
        // создаём новости для сортировки, так как лента может быть пуста
        controlPanelPage.tapCreatingNewsButton();
        // проверяем, что мы на экране создания новости
        creatingNewsPage.checkCreatingNewsPageByTopBarTitle();
        // новость со вчерашней датой,
        // добавляем к заголовкам суффиксы, чтобы созданные новости легче идентифицировались
        creatingNewsPage.fillInNewsFields(category, title + "-past", pastDate, time, description);
        creatingNewsPage.tapSave();
        // проверяем, что мы на экране Control panel
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        // новость с текущей датой
        controlPanelPage.tapCreatingNewsButton();
        creatingNewsPage.checkCreatingNewsPageByTopBarTitle();
        creatingNewsPage.fillInNewsFields(category, title + "-today", date, time, description);
        creatingNewsPage.tapSave();
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        // новость с завтрашней датой
        controlPanelPage.tapCreatingNewsButton();
        creatingNewsPage.checkCreatingNewsPageByTopBarTitle();
        creatingNewsPage.fillInNewsFields(category, title + "-future", futureDate, time, description);
        creatingNewsPage.tapSave();
        controlPanelPage.checkControlPanelPageByAddNewsButton();

        // записываем даты до сортировки
        String topDateBefore = controlPanelPage.getTopNewsDate();
        String bottomDateBefore = controlPanelPage.getBottomNewsDate();
        // если в ленте по какой-то причине все новости от одной даты:
        assumeFalse("Новости от одной даты - сортировка не работает", topDateBefore.equals(bottomDateBefore));

        // нажимаем кнопку сортировки
        controlPanelPage.tapSortButton();
        // записываем даты после сортировки
        String topDateAfter = controlPanelPage.getTopNewsDate();
        String bottomDateAfter = controlPanelPage.getBottomNewsDate();

        // проверяем, что верхняя и нижняя даты ленты поменялись местами
        assertEquals("Верхняя дата после сортировки должна быть равна нижней до сортировки",
                bottomDateBefore, topDateAfter);
        assertEquals("Нижняя дата после сортировки должна быть равна верхней до сортировки",
                topDateBefore, bottomDateAfter);

        //удаляем тестовые новости
        controlPanelPage.deleteNewsWithTitle(title + "-past");
        controlPanelPage.checkNewsWithTitleIsAbsent(title + "-past");
        controlPanelPage.deleteNewsWithTitle(title + "-today");
        controlPanelPage.checkNewsWithTitleIsAbsent(title + "-today");
        controlPanelPage.deleteNewsWithTitle(title + "-future");
        controlPanelPage.checkNewsWithTitleIsAbsent(title + "-future");
    }

    @Test
    @DisplayName("Должен сортировать новости по убыванию даты (сверху новые) в Control panel")
    public void shouldSortDescendingOnControlPanelTest() {
        // создаём новости для сортировки, так как лента может быть пуста
        controlPanelPage.tapCreatingNewsButton();
        // проверяем, что мы на экране создания новости
        creatingNewsPage.checkCreatingNewsPageByTopBarTitle();
        // новость со вчерашней датой,
        // добавляем к заголовкам суффиксы, чтобы созданные новости легче идентифицировались
        creatingNewsPage.fillInNewsFields(category, title + "-past", pastDate, time, description);
        creatingNewsPage.tapSave();
        // проверяем, что мы на экране Control panel
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        // новость с текущей датой
        controlPanelPage.tapCreatingNewsButton();
        creatingNewsPage.checkCreatingNewsPageByTopBarTitle();
        creatingNewsPage.fillInNewsFields(category, title + "-today", date, time, description);
        creatingNewsPage.tapSave();
        controlPanelPage.checkControlPanelPageByAddNewsButton();
        // новость с завтрашней датой
        controlPanelPage.tapCreatingNewsButton();
        creatingNewsPage.checkCreatingNewsPageByTopBarTitle();
        creatingNewsPage.fillInNewsFields(category, title + "-future", futureDate, time, description);
        creatingNewsPage.tapSave();
        controlPanelPage.checkControlPanelPageByAddNewsButton();

        String topDateBefore = controlPanelPage.getTopNewsDate();
        String bottomDateBefore = controlPanelPage.getBottomNewsDate();
        assumeFalse("Новости от одной даты - сортировка не работает", topDateBefore.equals(bottomDateBefore));

        // нажимаем кнопку для сортировки по возрастанию
        controlPanelPage.tapSortButton();
        // второе нажатие для сортировки по убыванию
        controlPanelPage.tapSortButton();
        String topDateAfter = controlPanelPage.getTopNewsDate();
        String bottomDateAfter = controlPanelPage.getBottomNewsDate();

        // после сортировки даты должны вернуться к первоначальному значению (расположению)
        assertEquals("Верхняя дата после сортировки должна быть равна верхней до сортировки",
                topDateBefore, topDateAfter);
        assertEquals("Нижняя дата после сортировки должна быть равна нижней до сортировки",
                bottomDateBefore, bottomDateAfter);

        //удаляем тестовые новости
        controlPanelPage.deleteNewsWithTitle(title + "-past");
        controlPanelPage.checkNewsWithTitleIsAbsent(title + "-past");
        controlPanelPage.deleteNewsWithTitle(title + "-today");
        controlPanelPage.checkNewsWithTitleIsAbsent(title + "-today");
        controlPanelPage.deleteNewsWithTitle(title + "-future");
        controlPanelPage.checkNewsWithTitleIsAbsent(title + "-future");
    }
}