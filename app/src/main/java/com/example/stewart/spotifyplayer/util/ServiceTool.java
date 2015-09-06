package com.example.stewart.spotifyplayer.util;

import com.example.stewart.spotifyplayer.service.ISpotifyService;

import retrofit.RestAdapter;

/**
 * Created by sstew5 on 9/4/15.
 */
public enum ServiceTool {
    INSTANCE;
    private final RestAdapter REST_ADAPTER = new RestAdapter.Builder().setEndpoint(SpotifyConfig.SPOTIFY_API_URL).build();
    private final ISpotifyService SERVICE = REST_ADAPTER.create(ISpotifyService.class);

    public ISpotifyService getService() {
        return SERVICE;
    }
}
