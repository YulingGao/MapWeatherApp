package edu.uiuc.cs427app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;


public class MapTest {

    static Intent initIntent;
    static {
        initIntent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        initIntent.putExtra("theme", false);
        initIntent.putExtra("userName", "Team#37-testmap");
    }

    final static String CITY_1 = "Chicago";
    final static String CITY_2 = "Los Angeles";

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(initIntent);

    @Test
    public void  MapCityTest() throws InterruptedException {
        // check if db exist this city
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/edu.uiuc.cs427app/databases/UserCity.db", null, SQLiteDatabase.OPEN_READWRITE);
        String username = initIntent.getStringExtra("userName");

        Cursor beforeCursor = db.rawQuery("Select * from UserCity where city = ? and username = ?", new String[] {CITY_1, username});
        if(beforeCursor.getCount() == 0){
            onView(withId(R.id.buttonAddLocation)).perform(click());
            onView(withText("ADD CITY")).inRoot(isDialog()).check(matches(isDisplayed()));
            Thread.sleep(1000);
            onView(withId(R.id.editText)).perform(typeText(CITY_1), closeSoftKeyboard());
            Thread.sleep(1000);
            onView(withText("ADD CITY")).perform(click());
        }

        // Test Location Chicago
        onView(withId(R.id.mBtnMap)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.map)).check(matches(isDisplayed()));

        pressBack();
        onView(withId(R.id.buttonRemoveLocation)).perform(click());
        onView(withId(R.id.editText)).perform(typeText(CITY_1), closeSoftKeyboard());
        onView(withText("Remove City")).perform(click());

        Cursor beforeCursor_2 = db.rawQuery("Select * from UserCity where city = ? and username = ?", new String[] {CITY_2, username});
        if(beforeCursor_2.getCount() == 0){
            onView(withId(R.id.buttonAddLocation)).perform(click());
            onView(withText("ADD CITY")).inRoot(isDialog()).check(matches(isDisplayed()));
            Thread.sleep(1000);
            onView(withId(R.id.editText)).perform(typeText(CITY_2), closeSoftKeyboard());
            Thread.sleep(1000);
            onView(withText("ADD CITY")).perform(click());
        }

        // Test Location Los Angeles
        onView(withId(R.id.mBtnMap)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.map)).check(matches(isDisplayed()));

    }
}
