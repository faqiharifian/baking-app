package com.arifian.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Created by faqih on 17/08/17.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class StepListActivityUITest {
    private IdlingResource idlingResource;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        idlingResource = activityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }

    @Test
    public void clickOnRecyclerViewItem_opensStepListActivity() {

        openStepListActivity();

        onView(withId(R.id.recyclerView_step))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.viewPager_stepDetail))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickOnStepWithVideo_showsVideoPlayerView() {

        openStepListActivity();

        onView(withId(R.id.recyclerView_step))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(
                allOf(
                        withId(R.id.exoPlayer_stepDetail_video),
                        withParent(withParent(withParent(withParent(withId(R.id.viewPager_stepDetail))))),
                        isDisplayed()))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickOnStepWithoutVideo_hidesVideoPlayerView() {

        openStepListActivity();

        onView(withId(R.id.recyclerView_step))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(
                allOf(
                        withId(R.id.exoPlayer_stepDetail_video),
                        withParent(withParent(withParent(withParent(withId(R.id.viewPager_stepDetail))))),
                        isDisplayed()))
                .check(doesNotExist());
    }

    private void openStepListActivity(){
        onView(withId(R.id.recyclerView_recipe))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }
}
