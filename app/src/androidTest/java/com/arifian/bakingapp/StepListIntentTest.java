package com.arifian.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by faqih on 17/08/17.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class StepListIntentTest {
    private IdlingResource idlingResource;

    @Rule
    public IntentsTestRule<MainActivity> activityTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        idlingResource = activityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }

    @Test
    public void clickOnRecyclerViewItem_runsRecipeStepActivityIntent() {

        openStepListActivity();

        onView(withId(R.id.recyclerView_step))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        intended(allOf(
                hasExtraWithKey(StepDetailActivity.KEY_RECIPE),
                hasExtraWithKey(StepDetailActivity.KEY_POSITION)
        ));
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
