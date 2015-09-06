package com.example.stewart.spotifyplayer.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stewart.spotifyplayer.R;
import com.example.stewart.spotifyplayer.model.TopSongs;
import com.example.stewart.spotifyplayer.model.Track;
import com.example.stewart.spotifyplayer.service.ISpotifyService;
import com.example.stewart.spotifyplayer.ui.adapter.TrackAdapter;
import com.example.stewart.spotifyplayer.util.ServiceTool;
import com.example.stewart.spotifyplayer.util.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TrackPlayerActivity extends AppCompatActivity {

    private static final String TAG = "TrackPlayerActivity";
    private List<Track> mTrackList;
    private TrackAdapter mTrackAdapter;
    private MediaPlayer mMediaPlayer;
    private ImageView mPlayPauseImageView;
    private ListView mTrackListView;
    private ImageView mPreviousTrackImageView;
    private ImageView mNexTracktImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_player);
        init(getIntent());
    }

    private void init(Intent intent) {
        //grab artist name
        initArtistNameTextView(intent.getStringExtra(Value.ARTIST_NAME));
        initArtistTopHitsAdapter(intent.getStringExtra(Value.ARTIST_ID));
        initMediaPlayer();
        initPlayerControls();

        //grab artist id
        //display listview
    }


    private void initPlayerControls() {
        initPrevImage();
        initPlayPauseImage();
        initNextImage();
    }

    private void initNextImage() {
        mNexTracktImageView = (ImageView)findViewById(R.id.next_track_image);
        mNexTracktImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelectedTrack(1);
            }
        });
    }

    private void changeSelectedTrack(int change) {
        int selectedIndex = mTrackAdapter.getSelectedItemIndex();
        int actualDestination = selectedIndex + change;
        int firstVisibilePosition = mTrackListView.getFirstVisiblePosition();
        int lastVisiblePosition = mTrackListView.getLastVisiblePosition();
        int relativeDestination = actualDestination - firstVisibilePosition;
        if(relativeDestination < 0 || relativeDestination > lastVisiblePosition){
            //scroll up?
            mTrackListView.smoothScrollToPosition(-2);
//            mTrackListView.smoothScrollBy(-300, 2);
        }
//        else if(relativeDestination > lastVisiblePosition){
//            //scroll down?
//            mTrackListView.setSelectionFromTop(firstVisibilePosition + relativeDestination, 100);
//        }

        int positionToChangeWhite = selectedIndex - mTrackListView.getFirstVisiblePosition();
        if(actualDestination >= 0 && actualDestination < mTrackListView.getCount()){
            mTrackAdapter.unSelectTrack(mTrackListView.getChildAt(positionToChangeWhite));
            mTrackListView.smoothScrollToPositionFromTop(actualDestination, 100);
            mTrackAdapter.selectTrack(mTrackListView.getChildAt(relativeDestination), actualDestination);
        }
    }

//    private void scrollMyListViewToBottom(final int destination) {
//        mTrackListView.post(new Runnable() {
//            @Override
//            public void run() {
//                // Select the last row so it will scroll into view...
//                mTrackListView.smoothScrollToPosition(destination);
//                // Just add something to scroll to the top ;-)
//            }
//        });
//    }

    private void initPlayPauseImage() {
        mPlayPauseImageView = (ImageView)findViewById(R.id.play_pause_track_image);
        mPlayPauseImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_play_arrow_black_48dp));
        mPlayPauseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMediaPlayer.isPlaying()){
                    mMediaPlayer.pause();
                    mPlayPauseImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_arrow_black_48dp));
                } else {
                    if()
                }
                togglePlayPause();
            }
        });
    }

    private void initPrevImage() {
        mPreviousTrackImageView = (ImageView)findViewById(R.id.previous_track_image);
        mPreviousTrackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelectedTrack(-1);
            }
        });
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                togglePlayPause();
            }
        });
    }

    private void togglePlayPause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPlayPauseImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_arrow_black_48dp));
        } else {
            //resume playing the selected item.

            try {
                Track track = mTrackAdapter.getItem(mTrackAdapter.getSelectedItemIndex());
                mMediaPlayer.setDataSource(track.getPreviewUrl());
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.start();
            mPlayPauseImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_black_48dp));
        }
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

        mTrackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = mTrackList.get(position);
                //todo
            }
        });
        TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryIso = telephonyManager.getNetworkCountryIso();
        ISpotifyService spotifyService = ServiceTool.INSTANCE.getService();
        spotifyService.searchArtistTopHits(artistId, countryIso, new Callback<TopSongs>() {
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
        TextView artistNameTextView = (TextView)findViewById(R.id.artist_title_text_view);
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

    private void loadTracks(List<Track> tracks){
        mTrackList.clear();
        mTrackList.addAll(tracks);
        mTrackAdapter.notifyDataSetChanged();
    }


//    View view;
//    ArrayList<String> mannschaftsnamen = new ArrayList<>();
//    for (int i = 0; i < mTrackListView.getCount(); i++) {
//        view = mTrackListView.getChildAt(i);
//        et = (EditText) v.findViewById(R.id.editText1);
//        mannschaftsnamen.add(et.getText().toString());
//    }
}
