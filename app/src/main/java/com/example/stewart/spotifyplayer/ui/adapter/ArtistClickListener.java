package com.example.stewart.spotifyplayer.ui.adapter;

import android.view.View;

import com.example.stewart.spotifyplayer.model.Artist;

/**
 * Created by sstew5 on 9/5/15.
 */
//https://gist.github.com/riyazMuhammad/1c7b1f9fa3065aa5a46f
public interface ArtistClickListener {
    void onItemClick(View v, Artist artist);
}
