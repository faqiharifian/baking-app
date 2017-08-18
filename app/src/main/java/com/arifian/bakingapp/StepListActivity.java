package com.arifian.bakingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.arifian.bakingapp.entities.Recipe;
import com.arifian.bakingapp.entities.Step;
import com.arifian.bakingapp.fragments.StepDetailFragment;
import com.arifian.bakingapp.fragments.StepListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListActivity extends AppCompatActivity {
    public static final String KEY_RECIPE = "recipe";
    public static final String KEY_FRAGMENT_LIST = "fragment_list";
    public static final String KEY_FRAGMENT_DETAIL = "fragment_detail";

    Recipe recipe = new Recipe();

    @BindView(R2.id.toolbar) Toolbar toolbar;

    StepListFragment stepListFragment;
    StepDetailFragment stepDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle args = getIntent().getExtras();
        if(args != null){
            recipe = args.getParcelable(KEY_RECIPE);
            getSupportActionBar().setTitle(recipe.getName());
        }

        if(savedInstanceState == null) {
            stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_step_detail);
            stepListFragment = (StepListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_step_list);
        }else{
            stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_FRAGMENT_LIST);
            stepListFragment = (StepListFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_FRAGMENT_DETAIL);
        }
        stepListFragment.setIngredients(recipe.getIngredients());
        stepListFragment.setSteps(recipe.getSteps());
        stepListFragment.setOnStepClicked(new StepListFragment.OnStepClick(){
            @Override
            public void stepClicked(Step step) {
                if(stepDetailFragment != null) {
                    stepDetailFragment.setStep(step);
                }else{
                    Intent intent = new Intent(StepListActivity.this, StepDetailActivity.class);
                    intent.putExtra(StepDetailActivity.KEY_POSITION, recipe.getSteps().indexOf(step));
                    intent.putExtra(StepDetailActivity.KEY_RECIPE, recipe);
                    startActivityForResult(intent, 101);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case 101:
                    stepListFragment.setPosition(data.getIntExtra(StepListFragment.KEY_POSITION, -1));
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, KEY_FRAGMENT_LIST, stepListFragment);
        getSupportFragmentManager().putFragment(outState, KEY_FRAGMENT_DETAIL, stepDetailFragment);
    }
}
