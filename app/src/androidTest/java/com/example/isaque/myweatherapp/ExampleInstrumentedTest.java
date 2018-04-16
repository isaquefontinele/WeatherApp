package com.example.isaque.myweatherapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.isaque.myweatherapp.activities.CityListActivity;
import com.example.isaque.myweatherapp.data.SharedPrefs;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<CityListActivity> mActivitRule =
            new ActivityTestRule<>(CityListActivity.class, false, false);


    @Before
    public void clearCacheAndStartActivity() throws InterruptedException {
        clearPreferences();
        mActivitRule.launchActivity(null);
        waitService();
    }

    public void waitService() throws InterruptedException {
        Thread.sleep(1500);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.example.isaque.myweatherapp", appContext.getPackageName());
    }

    @Test
    public void openSettingsAndReturnToHome() throws InterruptedException {
        checkInHomeWithEmptyRecycler();
        openSettings();
        onView(isRoot()).perform(ViewActions.pressBack());
        checkInHomeWithEmptyRecycler();
    }

    @Test
    public void openForecastAndReturnToHome() throws InterruptedException {
        checkInHomeWithEmptyRecycler();
        addCity();

        onView(nthChildOf(withId(R.id.city_list), 0)).perform(click());
        waitService();
        onView(isRoot()).perform(ViewActions.pressBack());
        checkInHomeWithNotEmptyRecycler(1);
    }

    @Test
    public void addCity() throws InterruptedException {
        checkInHomeWithEmptyRecycler();
        openSettings();

        //Types "recife" on the search bar, slowly, so the onTextChange can trigger correctly
        onView(withId(R.id.search_cities_bar)).perform(typeText("recif"));
        waitService();
        onView(withId(R.id.search_cities_bar)).perform(typeText("e"));
        waitService();

        //Add city
        onView(withId(R.id.bt_add_city)).perform(click());

        //Checks recycler from settings
        onView(nthChildOf(withId(R.id.recycler_current_cities), 0))
                .check(matches(hasDescendant(withText("Recife"))));

        onView(isRoot()).perform(ViewActions.pressBack());
        onView(isRoot()).perform(ViewActions.pressBack());
        waitService();

        //Checks recycler from home
        checkInHomeWithNotEmptyRecycler(1);
    }

    @Test
    public void removeCity() throws InterruptedException {
        checkInHomeWithEmptyRecycler();
        addCity();
        openSettings();

        onView(withId(R.id.recycler_current_cities)).check(new RecyclerViewItemCountAssertion(1));

        onView(withId(R.id.recycler_current_cities)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id. bt_delete_city)));

        onView(withId(R.id.recycler_current_cities)).check(new RecyclerViewItemCountAssertion(0));

        onView(isRoot()).perform(ViewActions.pressBack());
        waitService();

        checkInHomeWithEmptyRecycler();
    }

    @Test
    public void changeDefaultUnit() throws InterruptedException {
        checkInHomeWithEmptyRecycler();
        addCity();
        //Check is metric
        onView(nthChildOf(withId(R.id.city_list), 0))
                .check(matches(hasDescendant(withText(endsWith("ºC")))));
        openSettings();
        onView(withId(R.id.radio_imperial)).perform(click());
        onView(isRoot()).perform(ViewActions.pressBack());
        waitService();

        //Check is imperial in home
        onView(nthChildOf(withId(R.id.city_list), 0))
                .check(matches(hasDescendant(withText(endsWith("ºF")))));

        //Check is imperial in forecast
        onView(nthChildOf(withId(R.id.city_list), 0)).perform(click());
        waitService();
        onView(nthChildOf(withId(R.id.view_pager_day_1), 0))
                .check(matches(hasDescendant(withText(endsWith("ºF")))));

    }

    private void checkInHomeWithEmptyRecycler(){
        onView(withId(R.id.city_list)).check(matches(not(isDisplayed())));
        onView(withId(R.id.city_list)).check(new RecyclerViewItemCountAssertion(0));
        onView(withId(R.id.no_cities_registered)).check(matches(isDisplayed()));
    }

    private void checkInHomeWithNotEmptyRecycler(int num){
        onView(withId(R.id.city_list)).check(matches(isDisplayed()));
        onView(withId(R.id.city_list)).check(new RecyclerViewItemCountAssertion(num));
        onView(withId(R.id.no_cities_registered)).check(matches(not(isDisplayed())));
    }

    private void openSettings() throws InterruptedException {
        onView(withId(R.id.settings)).perform(click());
        waitService();
        onView(withId(R.id.search_line)).check(matches(isDisplayed()));
    }


    //Auxiliary methods
    private void clearPreferences(){
        File root = InstrumentationRegistry.getTargetContext().getFilesDir().getParentFile();
        String[] sharedPreferencesFileNames = new File(root, "shared_prefs").list();
        for (String fileName : sharedPreferencesFileNames) {
            InstrumentationRegistry.getTargetContext().getSharedPreferences(fileName.replace(".xml", ""), Context.MODE_PRIVATE).edit().clear().commit();
        }
    }

    private static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("with "+childPosition+" child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }

                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(childPosition).equals(view);
            }
        };
    }

    public class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), is(expectedCount));
        }
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

}

