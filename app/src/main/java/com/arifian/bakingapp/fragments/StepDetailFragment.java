package com.arifian.bakingapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.arifian.bakingapp.R;
import com.arifian.bakingapp.R2;
import com.arifian.bakingapp.entities.Step;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class StepDetailFragment extends Fragment {
    public static final String KEY_STEP = "step";
    Step step = new Step();

    @BindView(R2.id.linearLayout_stepDetail_default)
    LinearLayout defaultLinearLayout;
    @BindView(R2.id.scrollView_stepDetail)
    ScrollView detailScrollView;
    @BindView(R2.id.exoPlayer_stepDetail_video)
    SimpleExoPlayerView playerView;
    @BindView(R2.id.textView_stepDetail_description)
    TextView descriptionTextView;

    public StepDetailFragment() {
    }

    public static StepDetailFragment newInstance(Step step) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_STEP, step);
        StepDetailFragment fragment = new StepDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, view);

        if(savedInstanceState != null){
            setStep((Step) savedInstanceState.getParcelable(KEY_STEP));
            defaultLinearLayout.setVisibility(View.GONE);
            detailScrollView.setVisibility(View.VISIBLE);
        }
        if(getArguments() != null){
            setStep((Step) getArguments().getParcelable(KEY_STEP));
        }

        return view;
    }

    public void setStep(Step step){
        this.step = step;

        descriptionTextView.setText(step.getDescription());

        defaultLinearLayout.setVisibility(View.GONE);
        detailScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_STEP, step);
    }
}
