package com.arifian.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arifian.bakingapp.R;
import com.arifian.bakingapp.R2;
import com.arifian.bakingapp.entities.Recipe;
import com.arifian.bakingapp.utils.Preference;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * The configuration screen for the {@link IngredientAppWidget IngredientAppWidget} AppWidget.
 */
public class IngredientAppWidgetConfigureActivity extends AppCompatActivity {

    Preference preference;
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.linearLayout_widgetConfigure)
    LinearLayout linearLayout;
    @BindView(R2.id.button_widgetConfigure_ok)
    Button okButton;
    @BindColor(R2.color.colorPrimary) int primaryColor;
    @BindColor(R2.color.material_typography_secondary_text_color_dark) int textColor;
    int selectedPosition = 0;

    @OnClick({R.id.button_widgetConfigure_ok})
    public void click(View view){
                final Context context = IngredientAppWidgetConfigureActivity.this;

                // When the button is clicked, store the string locally
                preference.saveWidget(mAppWidgetId, selectedPosition);

                // It is the responsibility of the configuration activity to update the app widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                IngredientAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                // Make sure we pass back the original appWidgetId
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
    }

    public IngredientAppWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.ingredient_app_widget_configure);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.widgetConfigure_title));

        preference = new Preference(this);
        ArrayList<Recipe> recipes = preference.getRecipes();
        for(int i = 0; i < recipes.size(); i++){
            final int position = i;
            Recipe recipe = recipes.get(i);
            final TextView textView = (TextView) LayoutInflater.from(linearLayout.getContext())
                    .inflate(R.layout.item_recipe_simple, linearLayout, false);
            textView.setText(recipe.getName());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unselect();
                    selectedPosition = position;
                    select();
                }
            });
            linearLayout.addView(textView);
        }

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            selectedPosition = preference.getWidget(mAppWidgetId);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        if(selectedPosition != -1){
            select();
        }
    }

    private void unselect(){
        TextView view = (TextView) linearLayout.getChildAt(selectedPosition);
        view.setBackgroundColor(Color.WHITE);
        view.setTextColor(textColor);
    }

    private void select(){
        TextView view = (TextView) linearLayout.getChildAt(selectedPosition);
        view.setBackgroundColor(primaryColor);
        view.setTextColor(Color.WHITE);
    }
}

