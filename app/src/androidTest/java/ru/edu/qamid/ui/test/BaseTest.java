package ru.edu.qamid.ui.test;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import ru.edu.qamid.ui.AppActivity;
import ru.edu.qamid.ui.data.Helper;
import ru.edu.qamid.ui.data.NavigationIdlingResource;
import ru.edu.qamid.ui.data.NetworkIdlingResource;
import ru.edu.qamid.ui.data.SplashIdlingResource;
import ru.edu.qamid.ui.data.ToastIdlingResource;
import ru.edu.qamid.ui.pageObject.AuthorizationPage;
import ru.edu.qamid.ui.pageObject.ControlPanelPage;
import ru.edu.qamid.ui.pageObject.CreatingNewsPage;
import ru.edu.qamid.ui.pageObject.EditingNewsPage;
import ru.edu.qamid.ui.pageObject.FilterNewsPage;
import ru.edu.qamid.ui.pageObject.LoveIsAllPage;
import ru.edu.qamid.ui.pageObject.MainPage;
import ru.edu.qamid.ui.pageObject.NewsPage;
import ru.edu.qamid.ui.pageObject.TopBar;

// Это родительский класс для тестовых классов, куда вынесена общая логика
public class BaseTest {

    @Rule // правило для запуска главной активности приложения
    public ActivityScenarioRule<AppActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);

    private IdlingResource splashIdlingResource; // используем Idling Resource для сплэшскрина
    private IdlingResource networkIdlingResource; // используем Idling Resource для сетевой активности
    private IdlingResource navigationIdlingResource;
    private IdlingResource toastIdlingResource;


    // создаём экземпляры PageObject для работы с элементами страницы
    protected AuthorizationPage authorizationPage = new AuthorizationPage();
    protected TopBar topBar = new TopBar();
    protected MainPage mainPage = new MainPage();
    protected NewsPage newsPage = new NewsPage();
    protected ControlPanelPage controlPanelPage = new ControlPanelPage();
    protected CreatingNewsPage creatingNewsPage = new CreatingNewsPage();
    protected EditingNewsPage editingNewsPage = new EditingNewsPage();
    protected FilterNewsPage filterNewsPage = new FilterNewsPage();
    protected LoveIsAllPage loveIsAllPage = new LoveIsAllPage();

    @Before
    public void setUp() {
        // регистрируем ресурсы Idling
        splashIdlingResource = SplashIdlingResource.getInstance();
        IdlingRegistry.getInstance().register(splashIdlingResource);
        networkIdlingResource = NetworkIdlingResource.getInstance();
        IdlingRegistry.getInstance().register(networkIdlingResource);
        navigationIdlingResource = NavigationIdlingResource.getInstance();
        IdlingRegistry.getInstance().register(navigationIdlingResource);
        ToastIdlingResource.getInstance().reset();
        toastIdlingResource = ToastIdlingResource.getInstance();
        IdlingRegistry.getInstance().register(toastIdlingResource);
        // запускаем перехват тостов
        Helper.startCatchToasts();

        // сброс NavigationIdlingResource, если он остался от предыдущего теста
        NavigationIdlingResource.getInstance().reset();

        // перед каждым тестом проверяем, не залогинены ли уже
        if (!authorizationPage.isSignInButtonDisplayed()) {
            // если да - разлогиниваемся
            topBar.logOut();
        }
    }

    @After
    public void tearDown() {
        // после каждого теста дерегистрируем Idling Resource
        if (splashIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(splashIdlingResource);
        }
        if (networkIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(networkIdlingResource);
        }
        if (navigationIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(navigationIdlingResource);
        }
        if (toastIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(toastIdlingResource);
        }
        NavigationIdlingResource.getInstance().reset();
        ToastIdlingResource.getInstance().reset();
    }
}
