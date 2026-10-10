package ru.edu.qamid.ui.data;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.app.UiAutomation;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matcher;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeoutException;

import io.qameta.allure.kotlin.Allure;

public class Helper {

    public static int getRecyclerViewItemCount(@IdRes int recyclerViewId) {
        Allure.step("Получение количества элементов в RecyclerView");
        final int[] count = new int[1];
        onView(withId(recyclerViewId))
                .check((view, noViewFoundException) -> {
                    // проверяем на исключение, что View не найден
                    if (noViewFoundException != null) {
                        throw noViewFoundException;
                    }
                    // проверяем, что View относится к RecyclerView
                    if (!(view instanceof RecyclerView)) {
                        throw new AssertionError("View с id " + recyclerViewId + " не является RecyclerView");
                    }
                    RecyclerView recyclerView = (RecyclerView) view;
                    RecyclerView.Adapter adapter = recyclerView.getAdapter();
                    count[0] = adapter != null ? adapter.getItemCount() : 0;
                });
        return count[0];
    }

    public static String getTextFromRecyclerViewItem(@IdRes int recyclerViewId,
                                                     @IdRes int targetViewId,
                                                     int position) {
        Allure.step("Получение текста из элемента RecyclerView по заданной позиции");
        final String[] text = new String[1];

        onView(withId(recyclerViewId))
                .perform(RecyclerViewActions.scrollToPosition(position))
                .check((view, noViewFoundException) -> {
                    // проверяем на исключение, что View не найден
                    if (noViewFoundException != null) {
                        throw noViewFoundException;
                    }
                    // проверяем, что View относится к RecyclerView
                    if (!(view instanceof RecyclerView)) {
                        throw new AssertionError("View с id " + recyclerViewId + " не является RecyclerView");
                    }
                    RecyclerView recyclerView = (RecyclerView) view;

                    // получаем ViewHolder для позиции, если его нет - выбрасываем исключение
                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                    if (viewHolder == null) {
                        throw new AssertionError("ViewHolder не найден для позиции " + position);
                    }

                    // ищем целевой TextView внутри элемента, если его нет - выбрасываем исключение
                    TextView targetView = viewHolder.itemView.findViewById(targetViewId);
                    if (targetView == null) {
                        throw new AssertionError("View с id " + targetViewId
                                + " не найден в элементе на позиции " + position);
                    }

                    text[0] = targetView.getText().toString();
                });

        return text[0];
    }

    public static void scrollRecyclerViewToItem(@IdRes int recyclerViewId, Matcher<View> itemMatcher) {
        Allure.step("Прокручивание списка к элементу по матчеру");
        onView(withId(recyclerViewId))
                .perform(RecyclerViewActions.scrollTo(itemMatcher));
    }

    public static void performOnRecyclerViewItem(@IdRes int recyclerViewId,
                                                 Matcher<View> itemMatcher,
                                                 ViewAction action) {
        Allure.step("Выполнение действия над элементом RecyclerView");
        onView(withId(recyclerViewId))
                .perform(RecyclerViewActions.actionOnItem(itemMatcher, action));
    }

    public static void scrollRecyclerViewToItemWithText(@IdRes int recyclerViewId, String text) {
        Allure.step("Прокручивание списка к элементу с указанным текстом");
        scrollRecyclerViewToItem(recyclerViewId, hasDescendant(withText(text)));
    }

    // метод получения даты, позволяет получать как прошлые даты, так и настоящую или будущие
    public static String getDate(int daysOffset) {
        return LocalDate.now()
                .plusDays(daysOffset)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    // метод получения времени, позволяет получать как прошлое, так и будущее время
    public static String getTime(int hoursOffset, int minutesOffset) {
        return LocalTime.now()
                .plusHours(hoursOffset)
                .plusMinutes(minutesOffset)
                .format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public static void checkRecyclerViewHasItems(@IdRes int recyclerViewId) {
        Allure.step("Проверка, что в Recycler View есть объекты");
        onView(withId(recyclerViewId)).check(matches(CustomView.recyclerViewHasItems()));
    }

    // метод ожидания сворачивания диалогового окна
    public static void waitUntilGone(@IdRes int viewId, long millis) {
        onView(isRoot()).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "ждать, пока view с id " + viewId + " не уйдёт (" + millis + " мс)";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadUntilIdle();
                long endTime = System.currentTimeMillis() + millis;
                Matcher<View> matchId = withId(viewId);
                do {
                    boolean found = false;
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        if (matchId.matches(child)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) return;
                    uiController.loopMainThreadForAtLeast(50);
                } while (System.currentTimeMillis() < endTime);
                throw new PerformException.Builder()
                        .withActionDescription(getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        });
    }

    // метод ожидает исчезновения текста - необходим в связи с тем, что из-за
    // медленной работы эмулятора UI не успевает отрисовывать новый Recycler View,
    // и изменения отображаются медленнее, чем их проверяет тест
    public static void waitUntilTextGone(String text, long timeoutMillis) {
        onView(isRoot()).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "ждать исчезновения текста \"" + text + "\"";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadUntilIdle();
                long endTime = System.currentTimeMillis() + timeoutMillis;

                do {
                    boolean found = false;
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        if (child instanceof TextView) {
                            CharSequence t = ((TextView) child).getText();
                            if (t != null && t.toString().equals(text)) {
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) return;
                    uiController.loopMainThreadForAtLeast(50);
                } while (System.currentTimeMillis() < endTime);

                throw new PerformException.Builder()
                        .withActionDescription(getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        });
    }

    // метод ожидания, основной поток активности будет прокручиваться, пока соответствующий view не станет видимым
    public static void waitUntilVisible(Matcher<View> matcher, long timeoutMs) {
        onView(isRoot()).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "ждать появления view: " + matcher + " (" + timeoutMs + " мс)";
            }

            @Override
            public void perform(UiController uiController, View root) {
                uiController.loopMainThreadUntilIdle();
                long endTime = System.currentTimeMillis() + timeoutMs;
                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(root)) {
                        if (matcher.matches(child)
                                && child.getVisibility() == View.VISIBLE
                                && child.getWidth() > 0
                                && child.getHeight() > 0) {
                            return;
                        }
                    }
                    uiController.loopMainThreadForAtLeast(50);
                } while (System.currentTimeMillis() < endTime);

                throw new PerformException.Builder()
                        .withActionDescription(getDescription())
                        .withViewDescription(HumanReadables.describe(root))
                        .withCause(new TimeoutException())
                        .build();
            }
        });
    }

    // для обработки Toast-сообщений в API 36:
    public static void startCatchToasts() {
        UiAutomation uiAutomation = InstrumentationRegistry.getInstrumentation().getUiAutomation();
        uiAutomation.setOnAccessibilityEventListener(event -> {
            if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
                ToastIdlingResource.getInstance().onToast(event.getText().toString());
            }
        });
    }

    public static void checkToastMessage(String expectedText) {
        Allure.step("Проверка сообщения: " + expectedText);
        ToastIdlingResource.getInstance().expect(expectedText);

        // эта проверка заставит Espresso дождаться ToastIdlingResource
        onView(isRoot()).check(matches(isDisplayed()));
    }

    // метод принудительно открывает выпадающее меню - необходим для выбора категорий
    public static ViewAction showDropdown() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                // MaterialAutoCompleteTextView -> AppCompatAutoCompleteTextView -> AutoCompleteTextView
                return isAssignableFrom(AutoCompleteTextView.class);
            }

            @Override
            public String getDescription() {
                return "принудительно раскрыть дропдаун AutoCompleteTextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((AutoCompleteTextView) view).showDropDown();
                uiController.loopMainThreadUntilIdle();
            }
        };
    }

    // метод выбора в MaterialAutoCompleteTextView нужного пункта из списка
    public static void selectFromDropdown(@IdRes int fieldId, String value) {
        Allure.step("Выбор '" + value + "' из выпадающего списка");

        // переходим на поле ввода
        onView(withId(fieldId)).perform(click());

        // вводим текст
        onView(withId(fieldId)).perform(replaceText(value));

        // блок для очистки поля вместо выбора значения
        if (value == null || value.isEmpty()) {
            onView(withId(fieldId)).perform(closeSoftKeyboard());
            return;
        }

        // убираем клавиатуру
        onView(withId(fieldId)).perform(closeSoftKeyboard());

        // принудительно открываем выпадающий список
        onView(withId(fieldId)).perform(showDropdown());

        // выбираем пункт из списка по TextView
        onView(allOf(withText(value), isAssignableFrom(TextView.class)))
                .inRoot(isPlatformPopup())
                .check(matches(isDisplayed()))
                .perform(click());
    }
}
