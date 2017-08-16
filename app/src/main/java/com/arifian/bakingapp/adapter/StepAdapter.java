package com.arifian.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arifian.bakingapp.R;
import com.arifian.bakingapp.R2;
import com.arifian.bakingapp.entities.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by faqih on 16/08/17.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
    ArrayList<Step> steps;
    OnItemClick onItemClick;

    public StepAdapter(ArrayList<Step> steps, OnItemClick onItemClick) {
        this.steps = steps;
        this.onItemClick = onItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Step step = steps.get(position);
        holder.nameTextView.setText(step.getShortDescription());
        if(step.getVideoURL().isEmpty()){
            holder.playImageView.setVisibility(View.GONE);
        }
        holder.bind(step, onItemClick);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public interface OnItemClick{
        void onItemClicked(Step step);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R2.id.textView_step_name)
        TextView nameTextView;
        @BindView(R2.id.imageView_step_play)
        ImageView playImageView;
        View parentView;
        public ViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Step step, final OnItemClick onItemClick){
            parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onItemClicked(step);
                }
            });
        }
    }
}
