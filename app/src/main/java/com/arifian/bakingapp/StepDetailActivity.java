package com.arifian.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.arifian.bakingapp.adapter.StepPagerAdapter;
import com.arifian.bakingapp.entities.Recipe;
import com.arifian.bakingapp.fragments.StepListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity {
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

        pagerAdapter = new StepPagerAdapter(getSupportFragmentManager(), this, recipe.getSteps());
        stepViewPager.setAdapter(pagerAdapter);
        stepViewPager.setCurrentItem(selectedPosition);
        stepTabLayout.setupWithViewPager(stepViewPager);
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
        intent.putExtra(StepListFragment.KEY_POSITION, stepViewPager.getCurrentItem());
        setResult(RESULT_OK, intent);
        finish();
    }
}
