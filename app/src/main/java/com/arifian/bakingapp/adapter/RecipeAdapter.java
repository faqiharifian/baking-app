package com.arifian.bakingapp.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arifian.bakingapp.R;
import com.arifian.bakingapp.R2;
import com.arifian.bakingapp.entities.Recipe;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by faqih on 15/08/17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{
    Activity activity;
    List<Recipe> recipes;

    OnItemClick listener;

    public RecipeAdapter(Activity activity, List<Recipe> recipes, OnItemClick listener) {
        this.activity = activity;
        this.recipes = recipes;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.nameTextView.setText(recipe.getName());
        holder.servingTextView.setText(activity.getString(R.string.recipe_servings, String.valueOf(recipe.getServings())));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.food);
        requestOptions.error(R.drawable.food);
        Glide.with(activity)
                .setDefaultRequestOptions(requestOptions)
                .load(recipe.getImage())
                .into(holder.recipeImageView);

        holder.bind(recipe, listener);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public interface OnItemClick{
        public void onItemClicked(Recipe recipe);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R2.id.textView_recipeName)
        TextView nameTextView;
        @BindView(R2.id.textView_recipeServing)
        TextView servingTextView;
        @BindView(R2.id.imageView_recipeImage)
        ImageView recipeImageView;

        View parentView;
        public ViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Recipe recipe, final OnItemClick listener){
            parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(recipe);
                }
            });
        }
    }
}
