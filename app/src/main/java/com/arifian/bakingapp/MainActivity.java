package com.arifian.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.arifian.bakingapp.adapter.RecipeAdapter;
import com.arifian.bakingapp.connection.BakingClient;
import com.arifian.bakingapp.connection.BakingService;
import com.arifian.bakingapp.entities.Recipe;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @BindView(R2.id.toolbar) Toolbar toolbar;

    @BindView(R2.id.recyclerView_recipe)
    RecyclerView recipeRecyclerView;

    RecyclerView.LayoutManager layoutManager;

    List<Recipe> recipes = new ArrayList<>();

    RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.main_title));

        recipeAdapter = new RecipeAdapter(this, recipes);
        layoutManager = new LinearLayoutManager(this);
        recipeRecyclerView.setLayoutManager(layoutManager);
        recipeRecyclerView.setAdapter(recipeAdapter);

        getRecipes();
    }

    private void getRecipes(){
        recipes.clear();
        BakingClient.getClient().create(BakingService.class).getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipes.addAll(response.body());
                recipeAdapter.notifyDataSetChanged();
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
}
