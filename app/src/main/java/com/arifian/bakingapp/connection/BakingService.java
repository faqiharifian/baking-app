package com.arifian.bakingapp.connection;

import com.arifian.bakingapp.entities.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by faqih on 15/08/17.
 */

public interface BakingService {
    @Headers({
            "Accept: application/json"
    })
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
    @Headers({
            "Accept: application/json"
    })
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<String> getRecipes2();
}
