package com.example.stewart.spotifyplayer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stewart.spotifyplayer.R;
import com.example.stewart.spotifyplayer.model.Artist;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sstew5 on 9/4/15.
 */
//http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
public class ArtistAdapter extends BaseAdapter {


    private Context mContext;
    private List<Artist> mArtists;

    public ArtistAdapter(Context context, List<Artist> artists) {
        mContext = context;
        mArtists = artists;
    }




    @Override
    public int getCount() {
        return mArtists.size();
    }

    @Override
    public Artist getItem(int position) {
        return mArtists.get(position);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Artist artist = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.image_name_list_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.artistImageView = (ImageView) convertView.findViewById(R.id.image_list_row);
            viewHolder.artistNameTextView = (TextView) convertView.findViewById(R.id.name_list_row);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.artistNameTextView.setText(artist.getName());

        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(mContext).load(artist.getDefaultImage().getUrl())
                .error(R.drawable.ic_music_note_black_48dp)
                .placeholder(R.drawable.ic_music_note_black_48dp)
                .into(viewHolder.artistImageView);

        return convertView;
    }

    private static class ViewHolder {
        ImageView artistImageView;
        TextView artistNameTextView;
    }

}
