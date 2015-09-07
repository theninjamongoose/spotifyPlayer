package com.example.stewart.spotifyplayer.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.stewart.spotifyplayer.R;
import com.example.stewart.spotifyplayer.model.Artist;
import com.example.stewart.spotifyplayer.model.ArtistsResultParent;
import com.example.stewart.spotifyplayer.service.ISpotifyService;
import com.example.stewart.spotifyplayer.util.ServiceTool;
import com.example.stewart.spotifyplayer.ui.adapter.ArtistAdapter;
import com.example.stewart.spotifyplayer.util.Value;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ArtistsSearchActivity extends AppCompatActivity {

    private List<Artist> mArtistList;
    private ArtistAdapter mArtistAdapter;
    private EditText mSearchEditText;

    private static final String TAG = "ArtistsSearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    private void init() {
        mArtistList = new ArrayList<>();
        initSearchEditText();
        initSearchButton();
        initArtistListView();
    }

    private void initArtistListView() {
        ListView artistListView = (ListView) findViewById(R.id.artist_list_view);
        mArtistAdapter = new ArtistAdapter(ArtistsSearchActivity.this, mArtistList);
        artistListView.setAdapter(mArtistAdapter);
        artistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = mArtistList.get(position);
                Intent spotifyPlayerIntent = new Intent(getApplicationContext(), TrackPlayerActivity.class);
                spotifyPlayerIntent.putExtra(Value.ARTIST_ID, artist.getId());
                spotifyPlayerIntent.putExtra(Value.ARTIST_NAME, artist.getName());
                startActivity(spotifyPlayerIntent);
            }
        });
    }

    private void initSearchButton() {
        final Button button = (Button) findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String searchTerm = mSearchEditText.getText().toString();
                if (searchTerm.length() > 0) {
                    searchForArtistsLike(searchTerm);
                    clearViewFocus(mSearchEditText);
                }
            }
        });
    }

    private void clearViewFocus(View view) {
        view.setEnabled(false);
        view.setEnabled(true);
    }

    private void initSearchEditText() {
        mSearchEditText = (EditText) findViewById(R.id.search_edit_text);
        //set filter to ignore user return press and search instead
        mSearchEditText.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        for (int i = start; i < end; i++) {
                            if (source.length() > 0 && source.subSequence(source.length() - 1, source.length()).toString().equalsIgnoreCase("\n")) {
                                if (dest.length() > 0) {
                                    searchForArtistsLike(dest.toString());
                                    clearViewFocus(mSearchEditText);
                                }
                                return "";
                            }
                        }
                        return null;
                    }
                }
        });

    }

    private void searchForArtistsLike(String searchTerm) {
        ISpotifyService spotifyService = ServiceTool.INSTANCE.getService();
        spotifyService.searchArtists(searchTerm, new Callback<ArtistsResultParent>() {
            @Override
            public void success(ArtistsResultParent artistsResultParent, Response response) {
                loadArtists(artistsResultParent.getArtistsDetail().getArtists());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Error: " + error);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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

    private void loadArtists(List<Artist> artists) {
        mArtistList.clear();
        mArtistList.addAll(artists);
        mArtistAdapter.notifyDataSetChanged();
    }
}
