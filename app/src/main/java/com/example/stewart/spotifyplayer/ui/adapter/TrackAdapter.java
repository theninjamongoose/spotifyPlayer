package com.example.stewart.spotifyplayer.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stewart.spotifyplayer.R;
import com.example.stewart.spotifyplayer.model.Track;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sstew5 on 9/5/15.
 */
public class TrackAdapter extends BaseAdapter {

    private Context mContext;
    private List<Track> mTracks;
    private int selectedItemIndex;

    public TrackAdapter(Context context, List<Track> tracks) {
        mContext = context;
        mTracks = tracks;
        selectedItemIndex = 0;
    }

    @Override
    public int getCount() {
        return mTracks.size();
    }

    @Override
    public Track getItem(int position) {
        return mTracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Track track = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.image_name_list_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.trackImageView = (ImageView) convertView.findViewById(R.id.image_list_row);
            viewHolder.trackNameTextView = (TextView) convertView.findViewById(R.id.name_list_row);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == selectedItemIndex) {
            selectTrack(convertView, position);
        } else {
            unSelectTrack(convertView);
        }
        viewHolder.trackNameTextView.setText(track.getName());
        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(mContext).load(track.getAlbum().getDefaultImage().getUrl())
                .error(R.drawable.ic_music_note_black_48dp)
                .placeholder(R.drawable.ic_music_note_black_48dp)
                .into(viewHolder.trackImageView);

        return convertView;
    }

    public void unSelectTrack(View view) {
        view.setBackgroundColor(Color.WHITE);
    }

    public void selectTrack(View view, int itemIndex) {
        setSelectedItemIndex(itemIndex);
        view.setBackgroundColor(Color.GRAY);
    }

    private void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    private static class ViewHolder {
        ImageView trackImageView;
        TextView trackNameTextView;
    }
}
