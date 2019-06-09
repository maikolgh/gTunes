package com.talkao.green.lists;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.talkao.green.R;
import com.talkao.green.helpers.AsyncLoadImage;
import com.talkao.green.models.Song;

import java.util.List;


public class SongsAdapter extends ArrayAdapter<Song > {
    Activity activity;
    int layoutResourceId;
    List<Song> data = null;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();
        Song item = data.get(position);

        SongsAdapter.DataHolder holder = null;

        if(convertView!=null && convertView.getClass().equals(SongsAdapter.DataHolder.class)){
            holder = (SongsAdapter.DataHolder) convertView.getTag();
        }else {
            convertView = inflater.inflate(layoutResourceId,parent,false);

            holder = new SongsAdapter.DataHolder();
            holder.tvSongName = convertView.findViewById(R.id.tvSongName);
            holder.tvArtistName = convertView.findViewById(R.id.tvArtistName);
            holder.ivThumbnail = convertView.findViewById(R.id.ivThumbnail);

            convertView.setTag(holder);
        }

        holder.tvSongName.setText(item.getTrackName());
        holder.tvArtistName.setText(item.getArtistName());
        if (item.getArtworkUrl30() != null && !item.getArtworkUrl30().equalsIgnoreCase("")){
            if (item.getArtistId() != null && item.getCollectionId() != null && item.getTrackId() != null  && !item.getArtistId().toString().equalsIgnoreCase("") && !item.getCollectionId().toString().equalsIgnoreCase("") && !item.getTrackId().toString().equalsIgnoreCase("")){
                new AsyncLoadImage(holder.ivThumbnail, item.getArtistId().toString() + item.getCollectionId().toString() + item.getTrackId().toString() + "_thumb", getContext()).execute(item.getArtworkUrl30());
            }else{
                new AsyncLoadImage(holder.ivThumbnail, "", getContext()).execute(item.getArtworkUrl30());
            }
        }else if (item.getArtworkUrl60() != null && !item.getArtworkUrl60().equalsIgnoreCase("")){
            if (item.getArtistId() != null && item.getCollectionId() != null && item.getTrackId() != null && !item.getArtistId().toString().equalsIgnoreCase("") && !item.getCollectionId().toString().equalsIgnoreCase("") && !item.getTrackId().toString().equalsIgnoreCase("")){
                new AsyncLoadImage(holder.ivThumbnail, item.getArtistId().toString() + item.getCollectionId().toString() + item.getTrackId().toString() + "_thumb", getContext()).execute(item.getArtworkUrl60());
            }else{
                new AsyncLoadImage(holder.ivThumbnail, "", getContext()).execute(item.getArtworkUrl60());
            }
        }


        return convertView;
    }


    static class DataHolder{
        TextView tvSongName;
        TextView tvArtistName;
        ImageView ivThumbnail;
    }

    public SongsAdapter(@NonNull Activity activity, int resource, @NonNull List<Song> objects) {
        super(activity, resource, objects);

        this.layoutResourceId = resource;
        this.activity = activity;
        this.data = objects;
    }

}
