package com.arifian.bakingapp.fragments;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.arifian.bakingapp.R;
import com.arifian.bakingapp.R2;
import com.arifian.bakingapp.entities.Step;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.exoplayer2.Player.STATE_ENDED;

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
    @BindView(R2.id.imageView_stepDetail_image)
    ImageView imageView;
    @BindView(R2.id.textView_stepDetail_description)
    TextView descriptionTextView;

    SimpleExoPlayer player;

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

        playerView.requestFocus();
        return view;
    }

    public void setStep(Step step){
        defaultLinearLayout.setVisibility(View.GONE);
        detailScrollView.setVisibility(View.VISIBLE);
        this.step = step;

        descriptionTextView.setText(step.getDescription());

        if(!step.getVideoURL().isEmpty()){
            playerView.setVisibility(View.VISIBLE);

            if(!step.getThumbnailURL().isEmpty()) {
                Glide.with(getActivity())
                        .asBitmap()
                        .load(step.getThumbnailURL()).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        playerView.setDefaultArtwork(resource);
                    }
                });
            }

            initializePlayer(Uri.parse(step.getVideoURL()));
        }else{
            playerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_STEP, step);
    }

    private void initializePlayer(Uri mediaUri) {
        if(player != null)
            releasePlayer();
        if (player == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            playerView.setPlayer(player);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
            player.addListener(new Player.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if(playbackState == STATE_ENDED){
                        player.seekTo(0);
                        player.setPlayWhenReady(false);
                    }
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity() {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }
            });
        }
    }

    private void releasePlayer() {
        if(player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onPause() {
        if (player != null)
            player.setPlayWhenReady(false);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }
}
