package com.example.stewart.spotifyplayer.service;

import com.example.stewart.spotifyplayer.model.ArtistsResultParent;
import com.example.stewart.spotifyplayer.model.TopSongs;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by sstew5 on 9/4/15.
 */
public interface ISpotifyService {

    @GET("/v1/search?type=artist")
    void searchArtists(@Query("q") String searchTerm,
                       Callback<ArtistsResultParent> callback);

    @GET("/v1/artists/{id}/top-tracks")
    void searchArtistTopHits(@Path("id") String artistId, @Query("country") String countryIso,
                             Callback<TopSongs> callback);

}
