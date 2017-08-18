package com.arifian.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.arifian.bakingapp.adapter.StepPagerAdapter;
import com.arifian.bakingapp.entities.Recipe;
import com.arifian.bakingapp.entities.Step;
import com.arifian.bakingapp.fragments.StepDetailFragment;
import com.arifian.bakingapp.fragments.StepListFragment;
import com.google.android.exoplayer2.C;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    public static final String KEY_RECIPE = "recipe";
    public static final String KEY_POSITION = "position";

    Recipe recipe = new Recipe();
    int selectedPosition = -1;

    @BindView(R2.id.toolbar) Toolbar toolbar;
    @BindView(R2.id.tabLayout_stepDetail)
    TabLayout stepTabLayout;
    @BindView(R2.id.viewPager_stepDetail)
    ViewPager stepViewPager;
    StepPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle args = getIntent().getExtras();
        if(args != null){
            selectedPosition = args.getInt(KEY_POSITION);
            recipe = args.getParcelable(KEY_RECIPE);
            getSupportActionBar().setTitle(recipe.getName());
        }

        if(savedInstanceState != null){
            selectedPosition = savedInstanceState.getInt(KEY_POSITION);
            recipe = savedInstanceState.getParcelable(KEY_RECIPE);
        }

        pagerAdapter = new StepPagerAdapter(getSupportFragmentManager(), this, recipe.getSteps());
        stepViewPager.setAdapter(pagerAdapter);
        stepViewPager.setCurrentItem(selectedPosition);
        stepTabLayout.setupWithViewPager(stepViewPager);
        stepViewPager.addOnPageChangeListener(this);
        getFragment(selectedPosition).startPlaying();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(StepListFragment.KEY_POSITION, selectedPosition);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE &&
                ((getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) ||
                ((getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL)){
            int position = stepViewPager.getCurrentItem();
            Step step = recipe.getSteps().get(position);
            StepDetailFragment fragment = getFragment(position);
            if(!step.getVideoURL().isEmpty()) {
                fragment.releasePlayer();
                Intent intent = new Intent(this, FullscreenStepDetailActivity.class);
                intent.putExtra(FullscreenStepDetailActivity.KEY_VIDEO, step.getVideoURL());
                intent.putExtra(FullscreenStepDetailActivity.KEY_POSITION, fragment.getPlayerPosition());
                startActivityForResult(intent, 102);
                overridePendingTransition(0, 0);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        release(selectedPosition);
        selectedPosition = position;
        getFragment(selectedPosition).startPlaying();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void release(int position){
        StepDetailFragment fragment = (StepDetailFragment) pagerAdapter.getItem(position);
        fragment.releasePlayer();
    }

    private StepDetailFragment getFragment(int position){
        return (StepDetailFragment) pagerAdapter.getItem(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(resultCode == RESULT_OK && requestCode == 102){
            Log.e("selected pos", selectedPosition+"");
            Log.e("play pos", data.getLongExtra(StepDetailFragment.KEY_PLAYER_POSITION, C.TIME_UNSET)+"");
            stepViewPager.post(new Runnable() {
                @Override
                public void run() {

            getFragment(selectedPosition).startPlaying(data.getLongExtra(StepDetailFragment.KEY_PLAYER_POSITION, C.TIME_UNSET));
                }
            });
        }else
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_POSITION, selectedPosition);
        outState.putParcelable(KEY_RECIPE, recipe);
    }
}
