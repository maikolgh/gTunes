package com.talkao.green.models;

import android.media.MediaPlayer;

import java.util.List;

public class Session {
    private static Session _session = null;
    List<Song> _songs;
    MediaPlayer _mediaPlayer;


    public static Session getInstance()
    {
        if (_session == null)
            _session = new Session();

        return _session;
    }

    public void setSongs(List<Song> songs) { _songs = songs; }

    public List<Song> getSongs(){
        return _songs;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) { _mediaPlayer = mediaPlayer; }

    public MediaPlayer getMediaPlayer(){
        return _mediaPlayer;
    }

}
