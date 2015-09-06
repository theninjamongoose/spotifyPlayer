package com.example.stewart.spotifyplayer.media;

import android.media.MediaPlayer;

/**
 * Created by sstew5 on 9/6/15.
 */
public class SpotifyMediaPlayer extends MediaPlayer {
    private boolean mPaused = true;


    @Override
    public void pause(){
        super.pause();
        setPaused(true);
    }

    @Override
    public void start() throws IllegalStateException {
        super.start();
        setPaused(false);
    }

    public void setPaused(boolean paused) {
        this.mPaused = paused;
    }
}
