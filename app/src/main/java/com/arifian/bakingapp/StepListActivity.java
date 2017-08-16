package com.arifian.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.arifian.bakingapp.entities.Recipe;
import com.arifian.bakingapp.entities.Step;
import com.arifian.bakingapp.fragments.StepFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListActivity extends AppCompatActivity {
    public static final String KEY_RECIPE = "recipe";

    Recipe recipe = new Recipe();

    @BindView(R2.id.toolbar) Toolbar toolbar;

    StepFragment stepFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle args = getIntent().getExtras();
        if(args != null){
            recipe = args.getParcelable(KEY_RECIPE);
            getSupportActionBar().setTitle(recipe.getName());
        }

        stepFragment = (StepFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);
        stepFragment.setIngredients(recipe.getIngredients());
        stepFragment.setSteps(recipe.getSteps());
        stepFragment.setOnStepClicked(new StepFragment.OnStepClick(){
            @Override
            public void stepClicked(Step step) {
                super.stepClicked(step);
                Toast.makeText(StepListActivity.this, step.getShortDescription()+" clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
