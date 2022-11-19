package com.example.homemenu.main;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.homemenu.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.txt_create_account), withText("+ Create account"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction materialEditText = onView(
                allOf(withId(R.id.etd_name),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.md_styled_dialog_custom_view),
                                        0),
                                0)));
        materialEditText.perform(scrollTo(), replaceText("testare"), closeSoftKeyboard());

        ViewInteraction materialEditText2 = onView(
                allOf(withId(R.id.etd_email),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.md_styled_dialog_custom_view),
                                        0),
                                1)));
        materialEditText2.perform(scrollTo(), replaceText("test@test.com"), closeSoftKeyboard());

        ViewInteraction materialEditText3 = onView(
                allOf(withId(R.id.etd_address),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.md_styled_dialog_custom_view),
                                        0),
                                2)));
        materialEditText3.perform(scrollTo(), replaceText("testare"), closeSoftKeyboard());

        ViewInteraction materialEditText4 = onView(
                allOf(withId(R.id.etd_password),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.md_styled_dialog_custom_view),
                                        0),
                                3)));
        materialEditText4.perform(scrollTo(), replaceText("testare"), closeSoftKeyboard());

        ViewInteraction materialEditText5 = onView(
                allOf(withId(R.id.etd_phone),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.md_styled_dialog_custom_view),
                                        0),
                                4)));
        materialEditText5.perform(scrollTo(), replaceText("testare"), closeSoftKeyboard());

        ViewInteraction editText = onView(
                allOf(withId(R.id.etd_phone), withText("testare"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.md_styled_dialog_custom_view),
                                        0),
                                4),
                        isDisplayed()));
        editText.check(matches(isDisplayed()));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.etd_password), withText("•••••••"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.md_styled_dialog_custom_view),
                                        0),
                                3),
                        isDisplayed()));
        editText2.check(matches(withText("•••••••")));

        ViewInteraction mDButton = onView(
                allOf(withId(R.id.md_buttonDefaultPositive), withText("Register"),
                        childAtPosition(
                                allOf(withId(R.id.md_root),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        mDButton.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
