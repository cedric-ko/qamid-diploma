package ru.edu.qamid.ui.data;

public class Data {
    public static final String VALID_LOGIN = "login2";
    public static final String VALID_PASSWORD = "password2";
    public static final String WRONG_LOGIN = "login3";
    public static final String WRONG_PASSWORD = "password3";
    public static final String SPACE_FIELD = " ";
    public static final String EMPTY_FIELD = "";
    public static final String DIF_CASES_LOGIN = "LogiN2";
    public static final String DIF_CASES_PASSWORD = "pasSWord2";

    /* Ниже объявлены переменные категорий новостей.
    Для целей UI-тестирования используется первая из категорий - 'Объявление'.
    Такой подход позволяет избежать избыточного рандома в тестах и соответствует
    принципу детерминированности. При этом, если такое будет необходимо,
    в методах классов Page Object и в тестах возможно использование и
    других категорий, для чего они и объявлены здесь, в классе Data.
    */
    public static final String ANNOUNCEMENT_CATEGORY = "Объявление";
    public static final String BIRTHDAY_CATEGORY = "День рождения";
    public static final String SALARY_CATEGORY = "Зарплата";
    public static final String LABOR_UNION_CATEGORY = "Профсоюз";
    public static final String HOLIDAY_CATEGORY = "Праздник";
    public static final String MASSAGE_CATEGORY = "Массаж";
    public static final String THANKS_CATEGORY = "Благодарность";
    public static final String HELP_CATEGORY = "Нужна помощь";
    public static final String SOME_ERROR_MESSAGE = "Something went wrong. Try again later.";
    public static final String EMPTY_AUTH_ERROR_MESSAGE = "Login and password cannot be empty";
    public static final String EMPTY_FIELDS_ERROR_MESSAGE = "Fill empty fields";
    public static final String EMPTY_FEED_MESSAGE = "There is nothing here yet…";
    public static final String NEWS_STATUS_ACTIVE = "Active";
    public static final String NEWS_STATUS_NOT_ACTIVE = "Not active";

    public static String generateUniqueTitle() {
        return "Тестовый заголовок " + System.currentTimeMillis();
    }

    public static String generateUniqueDescription() {
        return "Тестовое описание " + System.currentTimeMillis();
    }
}
