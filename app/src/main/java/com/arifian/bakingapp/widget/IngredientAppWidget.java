package com.arifian.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.arifian.bakingapp.R;
import com.arifian.bakingapp.entities.Ingredient;
import com.arifian.bakingapp.entities.Recipe;
import com.arifian.bakingapp.utils.Preference;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link IngredientAppWidgetConfigureActivity IngredientAppWidgetConfigureActivity}
 */
public class IngredientAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Preference preference = new Preference(context);
        Recipe recipe = preference.getRecipes().get(preference.getWidget(appWidgetId));
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_app_widget);
        views.setTextViewText(R.id.textView_step_ingredient, recipe.getName());
        for(Ingredient ingredient : recipe.getIngredients()){
            RemoteViews ingredientView = new RemoteViews(context.getPackageName(), R.layout.item_ingredient);
            ingredientView.setTextViewText(R.id.textView_ingredient, ingredient.getIngredient()+" ("+ingredient.getQuantity()+" "+ingredient.getMeasure()+")");
            views.addView(R.id.linearLayout_step_ingredients, ingredientView);
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Preference preference = new Preference(context);
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            preference.deleteWidget(appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

