package com.talkao.green.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.talkao.green.R;
import com.talkao.green.helpers.AsyncLoadImage;
import com.talkao.green.models.Session;
import com.talkao.green.models.Song;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SongDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SongDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongDetailFragment extends Fragment implements MediaPlayer.OnPreparedListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ALBUM_NAME = "album_name";
    private static final String ARG_ALBUM_URL = "album_url";
    private static final String ARG_ARTIST_NAME = "artist_name";
    private static final String ARG_SONG_NAME = "song_name";
    private static final String ARG_SONG_URL = "song_url";
    private static final String ARG_CACHE_NAME = "cache_name";
    private static final String ARG_POSITION = "search_position";

    MediaPlayer mediaPlayer;
    private ProgressBar activity;
    private String mAlbumName;
    private String mAlbumURL;
    private String mArtistName;
    private String mSongName;
    private String mSongURL;
    private String mCacheName;
    private int mCurrentPosition;
    private Session session = Session.getInstance();

    private OnFragmentInteractionListener mListener;

    public SongDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param albumName Album Name.
     * @param albumURL Album Image URL.
     * @param artistName Artist Name.
     * @param songName Song Name.
     * @param songURL Song File URL.
     * @param cacheName Image Name in Storage.
     * @param position Position in array of songs.
     * @return A new instance of fragment SongDetailFragment.
     */
    public static SongDetailFragment newInstance(String albumName, String albumURL, String artistName, String songName, String songURL, String cacheName, int position) {
        SongDetailFragment fragment = new SongDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ALBUM_NAME, albumName);
        args.putString(ARG_ALBUM_URL, albumURL);
        args.putString(ARG_ARTIST_NAME, artistName);
        args.putString(ARG_SONG_NAME, songName);
        args.putString(ARG_SONG_URL, songURL);
        args.putString(ARG_CACHE_NAME, cacheName);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAlbumName = getArguments().getString(ARG_ALBUM_NAME);
            mAlbumURL = getArguments().getString(ARG_ALBUM_URL);
            mArtistName = getArguments().getString(ARG_ARTIST_NAME);
            mSongName = getArguments().getString(ARG_SONG_NAME);
            mSongURL = getArguments().getString(ARG_SONG_URL);
            mCacheName = getArguments().getString(ARG_CACHE_NAME);
            mCurrentPosition = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_detail, container, false);

        final TextView tvAlbumName = view.findViewById(R.id.tvAlbumName);
        final TextView tvArtistName = view.findViewById(R.id.tvArtistName);
        final TextView tvSongName = view.findViewById(R.id.tvSongName);

        ImageButton ibPlay = view.findViewById(R.id.ibPlay);
        ImageButton ibPause = view.findViewById(R.id.ibPause);
        ImageButton ibNext = view.findViewById(R.id.ibNext);
        ImageButton ibPrev = view.findViewById(R.id.ibPrev);
        final ImageView ivAlbum = view.findViewById(R.id.ivAlbum);
        activity = view.findViewById(R.id.progressBar);

        mediaPlayer = session.getMediaPlayer();

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (session.getSongs().size() > mCurrentPosition){
                    mCurrentPosition += 1;
                    Song song = session.getSongs().get(mCurrentPosition);

                    mAlbumName = song.getCollectionName();
                    mArtistName = song.getArtistName();
                    mSongName = song.getTrackName();
                    mSongURL = song.getPreviewUrl();
                    mAlbumURL = song.getArtworkUrl100();
                    tvAlbumName.setText(mAlbumName);
                    tvArtistName.setText(mArtistName);
                    tvSongName.setText(mSongName);
                    mediaPlayer.reset();
                    playSong(song.getPreviewUrl());
                    if (mAlbumURL != null && !mAlbumURL.equalsIgnoreCase("")){
                        if (song.getArtistId() != null && song.getCollectionId() != null && song.getTrackId() != null  && !song.getArtistId().toString().equalsIgnoreCase("") && !song.getCollectionId().toString().equalsIgnoreCase("") && !song.getTrackId().toString().equalsIgnoreCase("")){
                            new AsyncLoadImage(ivAlbum, song.getArtistId().toString()+song.getCollectionId().toString()+song.getTrackId().toString(), getContext()).execute(mAlbumURL);
                        }else{
                            new AsyncLoadImage(ivAlbum, "", getContext()).execute(mAlbumURL);
                        }

                    }
                }

            }
        });

        ibPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentPosition > 0){
                    mCurrentPosition -= 1;
                    Song song = session.getSongs().get(mCurrentPosition);

                    mAlbumName = song.getCollectionName();
                    mArtistName = song.getArtistName();
                    mSongName = song.getTrackName();
                    mSongURL = song.getPreviewUrl();
                    mAlbumURL = song.getArtworkUrl100();
                    tvAlbumName.setText(mAlbumName);
                    tvArtistName.setText(mArtistName);
                    tvSongName.setText(mSongName);
                    mediaPlayer.reset();
                    playSong(song.getPreviewUrl());
                    if (mAlbumURL != null && !mAlbumURL.equalsIgnoreCase("")){
                        if (song.getArtistId() != null && song.getCollectionId() != null && song.getTrackId() != null  && !song.getArtistId().toString().equalsIgnoreCase("") && !song.getCollectionId().toString().equalsIgnoreCase("") && !song.getTrackId().toString().equalsIgnoreCase("")){
                            new AsyncLoadImage(ivAlbum, song.getArtistId().toString()+song.getCollectionId().toString()+song.getTrackId().toString(), getContext()).execute(mAlbumURL);
                        }else{
                            new AsyncLoadImage(ivAlbum, "", getContext()).execute(mAlbumURL);
                        }
                    }
                }

            }
        });

        ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mediaPlayer.isPlaying()){
                    mediaPlayer.reset();
                    playSong(mSongURL);
                }
            }
        });

        ibPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }else{
                    mediaPlayer.start();
                }
            }
        });

        tvAlbumName.setText(mAlbumName);
        tvArtistName.setText(mArtistName);
        tvSongName.setText(mSongName);
        if (mAlbumURL != null && !mAlbumURL.equalsIgnoreCase("")){
            new AsyncLoadImage(ivAlbum, mCacheName, getContext()).execute(mAlbumURL);
        }

        return view;
    }

    private void playSong(String song){
        try {
            mediaPlayer.setDataSource(song);
            mediaPlayer.setOnPreparedListener(SongDetailFragment.this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        activity.setVisibility(View.VISIBLE);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
        activity.setVisibility(View.GONE);
        mediaPlayer.start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
