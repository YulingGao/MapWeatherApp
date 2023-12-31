package edu.uiuc.cs427app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;

/*
 * Please Note: the account testweather is used for testing the weather api.
 * Do not add/remove any locations in the account.
 * */

public class WeatherCityTest {
    static Intent initIntent;
    static {
        initIntent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        initIntent.putExtra("theme", false);
        initIntent.putExtra("userName", "Team#37-testweather");
    }

    final static String CITY_1 = "San Jose";
    final static String CITY_2 = "Los Angeles";
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(initIntent);

    @Test
    public void  WeatherCityTest() throws InterruptedException {
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
        Cursor beforeCursor_2 = db.rawQuery("Select * from UserCity where city = ? and username = ?", new String[] {CITY_2, username});
        if(beforeCursor_2.getCount() == 0){
            onView(withId(R.id.buttonAddLocation)).perform(click());
            onView(withText("ADD CITY")).inRoot(isDialog()).check(matches(isDisplayed()));
            Thread.sleep(1000);
            onView(withId(R.id.editText)).perform(typeText(CITY_2), closeSoftKeyboard());
            Thread.sleep(1000);
            onView(withText("ADD CITY")).perform(click());
        }

        // Test Location San Jose
        onView(withId(R.id.mBtn)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.weatherinfo)).check(matches(withText(containsString(CITY_1))));
        // Since weather page is Activity type, need to use pressBack twice
        pressBack();

        // Test Location Los Angeles
        onView(withId(R.id.mBtn - 1)).perform(click());
        Thread.sleep(1500);
        onView(withId(R.id.weatherinfo)).check(matches(withText(containsString(CITY_2))));

    }


}
