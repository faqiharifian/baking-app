package com.arifian.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.arifian.bakingapp.adapter.RecipeAdapter;
import com.arifian.bakingapp.connection.BakingClient;
import com.arifian.bakingapp.connection.BakingService;
import com.arifian.bakingapp.entities.Recipe;
import com.arifian.bakingapp.utils.Preference;
import com.arifian.bakingapp.views.SpaceGridItemDecorator;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_RECIPES = "recipes";

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.recyclerView_recipe)
    RecyclerView recipeRecyclerView;

    ArrayList<Recipe> recipes = new ArrayList<>();

    RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.main_title));

        recipeAdapter = new RecipeAdapter(this, recipes, new RecipeAdapter.OnItemClick() {
            @Override
            public void onItemClicked(Recipe recipe) {
                Intent intent = new Intent(MainActivity.this, StepListActivity.class);
                intent.putExtra(StepListActivity.KEY_RECIPE, recipe);
                startActivity(intent);
            }
        });
        recipeRecyclerView.setAdapter(recipeAdapter);
        if(recipeRecyclerView.getLayoutManager() instanceof GridLayoutManager){
            GridLayoutManager layoutManager = (GridLayoutManager) recipeRecyclerView.getLayoutManager();
            recipeRecyclerView.addItemDecoration(new SpaceGridItemDecorator(this, layoutManager.getSpanCount()));
        }else {
            recipeRecyclerView.addItemDecoration(new SpaceGridItemDecorator(this, 1));
        }

        if(savedInstanceState == null){
            getRecipes();
        }
    }

    private void getRecipes(){
        recipes.clear();
        BakingClient.getClient().create(BakingService.class).getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipes.addAll(response.body());
                recipeAdapter.notifyDataSetChanged();
                (new Preference(MainActivity.this)).saveRecipes(recipes);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                t.printStackTrace();
                if(t instanceof SocketTimeoutException)
                    Toast.makeText(MainActivity.this, getString(R.string.error_timeout), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_RECIPES, recipes);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<Recipe> recipes = savedInstanceState.getParcelableArrayList(KEY_RECIPES);
        this.recipes.addAll(recipes);
        recipeAdapter.notifyDataSetChanged();
    }
}
