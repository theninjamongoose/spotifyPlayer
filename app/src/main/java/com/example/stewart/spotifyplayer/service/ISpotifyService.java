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

    /**
     * Search for artists like the search term
     * @param searchTerm search term to be used
     * @param callback callback to return response to
     */
    @GET("/v1/search?type=artist")
    void getArtitsLike(@Query("q") String searchTerm,
                       Callback<ArtistsResultParent> callback);

    /**
     * Gets the artists top tracks
     * @param artistId id of the artist
     * @param countryIso top tracks are country specific
     * @param callback callback to return response to
     */
    @GET("/v1/artists/{id}/top-tracks")
    void getArtistTopHits(@Path("id") String artistId, @Query("country") String countryIso,
                          Callback<TopSongs> callback);

}
