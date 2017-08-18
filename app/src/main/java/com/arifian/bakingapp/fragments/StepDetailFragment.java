package com.arifian.bakingapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.google.android.exoplayer2.C;
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
    public static final String KEY_PLAYER_POSITION = "position";
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
    long playerPosition = C.TIME_UNSET;

    boolean canPlay = false;

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
            if(playerPosition <= 0)
                playerPosition = getArguments().getLong(KEY_PLAYER_POSITION, C.TIME_UNSET);
            defaultLinearLayout.setVisibility(View.GONE);
            detailScrollView.setVisibility(View.VISIBLE);
            Step step = savedInstanceState.getParcelable(KEY_STEP);
            setStep(step);
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

            initializePlayer();
        }else{
            playerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_STEP, step);
        outState.putLong(KEY_PLAYER_POSITION, playerPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    public long getPlayerPosition(){
        return playerPosition;
    }

    public void startPlaying(long playerPosition){
        this.playerPosition = playerPosition;

        if(player != null)
            if (playerPosition != C.TIME_UNSET) player.seekTo(playerPosition);
        startPlaying();
    }

    public void startPlaying(){
        canPlay = true;
        if(player != null)
            player.setPlayWhenReady(canPlay);
    }

    public void pausePlaying(){
        canPlay = false;
        if(player != null)
            player.setPlayWhenReady(false);
    }

    public void initializePlayer() {
        if(player != null)
            releasePlayer();
        if (player == null) {
            Uri mediaUri = Uri.parse(step.getVideoURL());
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
            if (playerPosition != C.TIME_UNSET) player.seekTo(playerPosition);
            player.setPlayWhenReady(canPlay);
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

    public void releasePlayer() {
        canPlay = false;
        if(player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        if(player != null) {
            playerPosition = player.getCurrentPosition();
            releasePlayer();
        }
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == 102){
            playerPosition = data.getLongExtra(KEY_PLAYER_POSITION, C.TIME_UNSET);
            if(!step.getVideoURL().isEmpty()) {
//                initializePlayer(Uri.parse(step.getVideoURL()));
            }
        }
    }


}
