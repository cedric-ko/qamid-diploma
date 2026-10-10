package ru.edu.qamid.ui.data;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.util.HumanReadables;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

public class CustomView {

    // кастомный ViewMatcher для проверки наличия в ленте хотя бы одного пункта
    public static Matcher<View> recyclerViewHasItems() {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("RecyclerView имеет как минимум один объект");
            }

            @Override
            protected boolean matchesSafely(View view) {
                if (!(view instanceof RecyclerView)) {
                    return false;
                }
                RecyclerView recyclerView = (RecyclerView) view;
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                return adapter != null && adapter.getItemCount() > 0;
            }
        };
    }

    // кастомный ViewAction для клика по дочернему View на плашках новостей
    public static ViewAction clickOnChildViewWithId(@IdRes final int childViewId) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Нажатие на дочерний view с id " + childViewId;
            }

            @Override
            public void perform(UiController uiController, View view) {
                View childView = view.findViewById(childViewId);
                if (childView != null) {
                    childView.performClick();
                } else {
                    throw new PerformException.Builder()
                            .withActionDescription(getDescription())
                            .withViewDescription(HumanReadables.describe(view))
                            .withCause(new RuntimeException("Дочерний view не найден"))
                            .build();
                }
            }
        };
    }

    // кастомный ViewAction для взаимодействия с view описания цитаты
    public static ViewAction checkViewAction(ViewAssertion assertion) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return Matchers.any(View.class);
            }

            @Override
            public String getDescription() {
                return "Проверка view";
            }

            @Override
            public void perform(UiController uiController, View view) {
                assertion.check(view, null);
            }
        };
    }

    // метод клика по объекту, где сlick "плавает" из-за диалогового окна
    public static ViewAction performClickOnView() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "performClick() на соответствующем view";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadUntilIdle();
                view.performClick();
                uiController.loopMainThreadUntilIdle();
            }
        };
    }
}