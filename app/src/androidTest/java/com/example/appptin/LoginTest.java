package com.example.appptin;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginTest {
    @Rule
    public ActivityScenarioRule<login> activityRule =
            new ActivityScenarioRule<>(login.class);

    @Before
    public void setUp() {
        // Initialize the Intents API
        Intents.init();
    }

    @After
    public void tearDown() {
        // Release the Intents API
        Intents.release();
    }

    @Test
    public void testValidLogin() {
        // Find the views and enter valid credentials
        onView(withId(R.id.email)).perform(typeText("josep@gmail.com"));
        onView(withId(R.id.password)).perform(typeText("josep"));
        closeSoftKeyboard();

        // Click the login button
        onView(withId(R.id.boton_login)).perform(click());

        // Check that the MainActivity is launched
        intended(hasComponent(MainActivity.class.getName()));
    }

}