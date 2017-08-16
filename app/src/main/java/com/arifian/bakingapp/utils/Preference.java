package com.arifian.bakingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.arifian.bakingapp.entities.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by faqih on 16/08/17.
 */

public class Preference {
    final String PREF_NAME = "baking";
    final String KEY_RECIPE = "baking";
    final String KEY_WIDGET = "widget_";
    Context context;
    int PRIVATE_MODE = 0;
    SharedPreferences pref;

    SharedPreferences.Editor editor;

    public Preference(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    public void saveRecipes(ArrayList<Recipe> recipes){
        Gson gson = new Gson();
        String json = gson.toJson(recipes);
        pref.edit().putString(KEY_RECIPE, json).apply();
    }

    public ArrayList<Recipe> getRecipes(){
        Gson gson = new Gson();
        String json = pref.getString(KEY_RECIPE, "");
        return gson.fromJson(json, new TypeToken<ArrayList<Recipe>>(){}.getType());
    }

    public void saveWidget(int widgetId, int selectedRecipe){
        pref.edit().putInt(KEY_WIDGET+widgetId, selectedRecipe).apply();
    }

    public int getWidget(int widgetId){
        return pref.getInt(KEY_WIDGET+widgetId, 0);
    }

    public void deleteWidget(int widgetId){
        pref.edit().remove(KEY_WIDGET+widgetId).apply();
    }
}
