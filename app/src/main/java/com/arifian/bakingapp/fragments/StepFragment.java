package com.arifian.bakingapp.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arifian.bakingapp.R;
import com.arifian.bakingapp.R2;
import com.arifian.bakingapp.adapter.StepAdapter;
import com.arifian.bakingapp.entities.Ingredient;
import com.arifian.bakingapp.entities.Step;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment {
    public static final String KEY_INGREDIENTS = "ingredients";
    public static final String KEY_STEPS = "steps";
    public static final String KEY_POSITION = "position";

    int selectedPosition = -1;

    ArrayList<Ingredient> ingredients = new ArrayList<>();
    ArrayList<Step> steps = new ArrayList<>();

    StepAdapter stepAdapter;

    @BindView(R2.id.linearLayout_step_ingredients)
    LinearLayout ingredientLinearLayout;
    @BindView(R2.id.recyclerView_step)
    RecyclerView stepRecyclerView;

    OnStepClick stepClick;

    @BindColor(R2.color.colorPrimaryDark) int primaryDarkColor;
    @BindColor(R2.color.colorPrimary) int primaryColor;
    @BindColor(R2.color.material_typography_secondary_text_color_dark) int textColor;

    public StepFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, view);

        stepAdapter = new StepAdapter(steps, new StepAdapter.OnItemClick() {
            @Override
            public void onItemClicked(Step step) {
                if(selectedPosition != -1){
                    unselect();
                }
                selectedPosition = steps.indexOf(step);
                select();
                stepClick.stepClicked(step);
            }
        });
        stepRecyclerView.setAdapter(stepAdapter);
        stepRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        if(savedInstanceState != null){
            ArrayList<Ingredient> ingredients = savedInstanceState.getParcelableArrayList(KEY_INGREDIENTS);
            setIngredients(ingredients);
            selectedPosition = savedInstanceState.getInt(KEY_POSITION);
            ArrayList<Step> steps = savedInstanceState.getParcelableArrayList(KEY_STEPS);
            setSteps(steps);
        }

        return view;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients){
        this.ingredients = ingredients;
        for(Ingredient ingredient : this.ingredients){
            View view = LayoutInflater.from(ingredientLinearLayout.getContext()).inflate(R.layout.item_ingredient, ingredientLinearLayout, false);
            ((TextView) view.findViewById(R.id.textView_ingredient)).setText(ingredient.getIngredient()+" ("+ingredient.getQuantity()+" "+ingredient.getMeasure()+")");
            ingredientLinearLayout.addView(view);
        }
    }

    public void setSteps(ArrayList<Step> steps){
        this.steps.clear();
        this.steps.addAll(steps);
        stepAdapter.notifyDataSetChanged();
        if(selectedPosition != -1){
            select();
        }
    }

    public void setOnStepClicked(OnStepClick onStepClick){
        this.stepClick = onStepClick;
    }

    public static class OnStepClick{
        public void stepClicked(Step step){
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_INGREDIENTS, ingredients);
        outState.putParcelableArrayList(KEY_STEPS, steps);
        outState.putInt(KEY_POSITION, selectedPosition);
    }

    private void unselect(){
        View view = stepRecyclerView.getChildAt(selectedPosition);
        view.setBackgroundColor(Color.WHITE);
        ((TextView) view.findViewById(R.id.textView_step_name)).setTextColor(textColor);
        ((ImageView) view.findViewById(R.id.imageView_step_play)).setColorFilter(primaryDarkColor);
    }

    private void select(){
        View view = stepRecyclerView.getChildAt(selectedPosition);
        view.setBackgroundColor(primaryColor);
        ((TextView) view.findViewById(R.id.textView_step_name)).setTextColor(Color.WHITE);
        ((ImageView) view.findViewById(R.id.imageView_step_play)).setColorFilter(Color.WHITE);
    }
}
