package com.example.stewart.spotifyplayer.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stewart.spotifyplayer.R;
import com.example.stewart.spotifyplayer.model.TopSongs;
import com.example.stewart.spotifyplayer.model.Track;
import com.example.stewart.spotifyplayer.service.ISpotifyService;
import com.example.stewart.spotifyplayer.ui.adapter.TrackAdapter;
import com.example.stewart.spotifyplayer.util.Evaluation;
import com.example.stewart.spotifyplayer.util.ServiceTool;
import com.example.stewart.spotifyplayer.util.SpotifyConfig;
import com.example.stewart.spotifyplayer.util.Value;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.PlayerStateCallback;
import com.spotify.sdk.android.player.Spotify;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TrackPlayerActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {

    private Player mSpotifyPlayer;
    private static final int REQUEST_CODE = 1337;
    private static final String TAG = "TrackPlayerActivity";
    private List<Track> mTrackList;
    private TrackAdapter mTrackAdapter;
    private ImageView mPlayPauseImageView;
    private ListView mTrackListView;
    private ImageView mPreviousTrackImageView;
    private ImageView mNexTracktImageView;
    private boolean endOfPlaylist;
    private static final float AUTO_SCROLL_LENGTH_DP = 100f;
    private static final int AUTO_SCROLL_SPEED = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_player);
        init(getIntent());
    }

    private void init(Intent intent) {
        initSpotifyLogin();
        initArtistNameTextView(intent.getStringExtra(Value.ARTIST_NAME));
        initArtistTopHitsAdapter(intent.getStringExtra(Value.ARTIST_ID));
        initPlayerControls();
    }

    private void initSpotifyLogin() {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(SpotifyConfig.SPOTIFY_CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                SpotifyConfig.SPOTIFY_LOGIN_REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    private void initPlayerControls() {
        initPrevImage();
        initPlayPauseImage();
        initNextImage();
    }

    private void initNextImage() {
        mNexTracktImageView = (ImageView) findViewById(R.id.next_track_image);
        mNexTracktImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpotifyPlayer.getPlayerState(new PlayerStateCallback() {
                    @Override
                    public void onPlayerState(PlayerState playerState) {
                        //do nothing if play hasn't been pressed yet
                        if (!playerState.activeDevice) {
                            return;
                        }
                        //clicked while in paused state
                        else if (!playerState.playing) {
                            mSpotifyPlayer.skipToNext();
                            mSpotifyPlayer.pause();
                        }
                        //clicked while in play
                        else {
                            mSpotifyPlayer.skipToNext();
                        }
                    }
                });

            }
        });
    }

    private int getNextTrackIndex(String trackUri) {
        for (int i = 0; i < mTrackListView.getCount(); i++) {
            if (trackUri.equals(mTrackAdapter.getItem(i).getUri())) {
                return i;
            }
        }
        return -1;
    }

    private void changeSelectedTrack(int change) {
        int selectedIndex = mTrackAdapter.getSelectedItemIndex();
        int actualDestination = selectedIndex + change;
        int firstVisiblePosition = mTrackListView.getFirstVisiblePosition();
        int relativeDestination = actualDestination - firstVisiblePosition;
        int positionToChangeWhite = selectedIndex - mTrackListView.getFirstVisiblePosition();

        //if highlight change is in bounds, then change highlight states
        if (actualDestination >= 0 && actualDestination < mTrackListView.getCount()) {
            mTrackAdapter.unSelectTrack(mTrackListView.getChildAt(positionToChangeWhite));
            mTrackListView.smoothScrollToPositionFromTop(actualDestination,
                    Math.round(Evaluation.INSTANCE.convertDpToPixel(AUTO_SCROLL_LENGTH_DP, this)), AUTO_SCROLL_SPEED);
            mTrackAdapter.selectTrack(mTrackListView.getChildAt(relativeDestination), actualDestination);
        }
    }

    private void initPlayPauseImage() {
        mPlayPauseImageView = (ImageView) findViewById(R.id.play_pause_track_image);
        mPlayPauseImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_arrow_black_48dp));
        mPlayPauseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayPause();
            }
        });
    }

    private void initPrevImage() {
        mPreviousTrackImageView = (ImageView) findViewById(R.id.previous_track_image);
        mPreviousTrackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpotifyPlayer.getPlayerState(new PlayerStateCallback() {
                    @Override
                    public void onPlayerState(PlayerState playerState) {
                        //do nothing if play hasn't been pressed yet
                        if (!playerState.activeDevice) {
                            return;
                        }
                        //if in paused state when previous is clicked
                        else if (!playerState.playing) {
                            mSpotifyPlayer.skipToPrevious();
                            mSpotifyPlayer.pause();
                        }
                        //in play state
                        else {
                            mSpotifyPlayer.skipToPrevious();
                        }
                    }
                });
            }
        });
    }

    private void togglePlayPause() {
        mSpotifyPlayer.getPlayerState(new PlayerStateCallback() {
            @Override
            public void onPlayerState(PlayerState playerState) {
                //handle first play click
                if (!playerState.activeDevice) {
                    mSpotifyPlayer.play(getTopTrackUris());
                    mPlayPauseImageView.setImageDrawable(ContextCompat.getDrawable(TrackPlayerActivity.this, R.drawable.ic_pause_black_48dp));
                }
                //if playing change to pause
                else if (playerState.playing) {
                    mPlayPauseImageView.setImageDrawable(ContextCompat.getDrawable(TrackPlayerActivity.this, R.drawable.ic_play_arrow_black_48dp));
                    mSpotifyPlayer.pause();
                }
                //if paused, play
                else {
                    mPlayPauseImageView.setImageDrawable(ContextCompat.getDrawable(TrackPlayerActivity.this, R.drawable.ic_pause_black_48dp));
                    mSpotifyPlayer.resume();
                }
            }
        });
    }


    private void initArtistTopHitsAdapter(String artistId) {
        mTrackList = new ArrayList<>();
        mTrackListView = (ListView) findViewById(R.id.track_list_view);
        mTrackAdapter = new TrackAdapter(this, mTrackList);
        mTrackListView.setAdapter(mTrackAdapter);
        mTrackListView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryIso = telephonyManager.getNetworkCountryIso();
        ISpotifyService spotifyService = ServiceTool.INSTANCE.getSpotifyService();
        spotifyService.getArtistTopHits(artistId, countryIso, new Callback<TopSongs>() {
            @Override
            public void success(TopSongs topSongs, Response response) {
                loadTracks(topSongs.getTracks());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Error: " + error);
            }
        });

    }

    private void initArtistNameTextView(String stringExtra) {
        TextView artistNameTextView = (TextView) findViewById(R.id.artist_title_text_view);
        artistNameTextView.setText(stringExtra);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spotify_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadTracks(List<Track> tracks) {
        mTrackList.clear();
        mTrackList.addAll(tracks);
        mTrackAdapter.notifyDataSetChanged();
    }

    /**
     * This method handles the response from the user Spotify login attempt
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            //if we get a valid token, via authentication response, initialize the player
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), SpotifyConfig.SPOTIFY_CLIENT_ID);
                mSpotifyPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        mSpotifyPlayer.addConnectionStateCallback(TrackPlayerActivity.this);
                        mSpotifyPlayer.addPlayerNotificationCallback(TrackPlayerActivity.this);
                        mSpotifyPlayer.setRepeat(false);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e(TAG, "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    private List<String> getTopTrackUris() {
        List<String> trackUris = new ArrayList<>();
        int tracksSize = mTrackListView.getCount();
        for (int i = 0; i < tracksSize; i++) {
            trackUris.add(mTrackAdapter.getItem(i).getUri());
        }
        return trackUris;
    }

    @Override
    public void onLoggedIn() {
        Log.d(TAG, "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d(TAG, "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d(TAG, "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d(TAG, "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d(TAG, "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(PlayerNotificationCallback.EventType eventType, PlayerState playerState) {
        //If we are at the end of the playlist, prompt for replay
        if (eventType.equals(EventType.END_OF_CONTEXT)) {
            mSpotifyPlayer.pause();
            promptForReplay();
            endOfPlaylist = true;
        }
        //if track has changed, check to see if highlight needs to move
        if (eventType.equals(EventType.TRACK_CHANGED) && !endOfPlaylist) {
            int nextTrackIndex = getNextTrackIndex(playerState.trackUri);
            int indexChange = nextTrackIndex - mTrackAdapter.getSelectedItemIndex();
            if (indexChange != 0) {
                //move highlight
                changeSelectedTrack(indexChange);
            }
        }
        Log.d(TAG, "Playback event received: " + eventType.name());
    }

    private void promptForReplay() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.play_again_prompt));

        alertDialogBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                recreate();
            }
        });

        alertDialogBuilder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public void onPlaybackError(PlayerNotificationCallback.ErrorType errorType, String errorDetails) {
        Log.d(TAG, "Playback error received: " + errorType.name());
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

}
