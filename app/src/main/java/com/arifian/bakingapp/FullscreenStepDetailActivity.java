package com.arifian.bakingapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.arifian.bakingapp.fragments.StepDetailFragment;
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
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenStepDetailActivity extends AppCompatActivity {
    public static final String KEY_VIDEO = "video";
    public static final String KEY_POSITION = "position";

    @BindView(R.id.exoPlayer_stepDetail_video)
    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;
    long playerPosition = C.TIME_UNSET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen_step_detail);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        Bundle args = getIntent().getExtras();
        if(args != null){
            playerPosition = args.getLong(KEY_POSITION);
            initializePlayer(Uri.parse(args.getString(KEY_VIDEO)));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("config change", String.valueOf(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT));
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        releasePlayer();
        Intent intent = new Intent();
        intent.putExtra(StepDetailFragment.KEY_PLAYER_POSITION, playerPosition);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void initializePlayer(Uri mediaUri) {
        if (player == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            playerView.setPlayer(player);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this, "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            player.prepare(mediaSource);
            if (playerPosition != C.TIME_UNSET) player.seekTo(playerPosition);
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
        if(player != null) {
            playerPosition = player.getCurrentPosition();
            releasePlayer();
        }
        super.onPause();
    }
}
