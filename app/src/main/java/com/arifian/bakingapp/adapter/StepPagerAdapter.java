package com.arifian.bakingapp.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.arifian.bakingapp.entities.Step;
import com.arifian.bakingapp.fragments.StepDetailFragment;

import java.util.List;

/**
 * Created by faqih on 16/08/17.
 */

public class StepPagerAdapter extends FragmentStatePagerAdapter {
    Activity activity;
    List<Step> steps;
    StepDetailFragment[] stepDetailFragments;

    public StepPagerAdapter(FragmentManager fm, Activity activity, List<Step> steps) {
        super(fm);
        this.activity = activity;
        this.steps = steps;
        stepDetailFragments = new StepDetailFragment[steps.size()];
    }

    @Override
    public Fragment getItem(int position) {
        if(stepDetailFragments[position] == null)
            stepDetailFragments[position] = StepDetailFragment.newInstance(steps.get(position));
        return stepDetailFragments[position];
    }

    @Override
    public int getCount() {
        return steps.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Step "+position;
    }
}
