package com.talkao.green;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.talkao.green.fragments.SongDetailFragment;
import com.talkao.green.lists.SongsAdapter;
import com.talkao.green.models.Search;
import com.talkao.green.models.Session;
import com.talkao.green.models.Song;
import com.talkao.green.services.APIService;
import com.talkao.green.utils.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class gTunesActivity extends AppCompatActivity implements SongDetailFragment.OnFragmentInteractionListener{
    private APIService mAPIService;
    private ListView lvSongs;
    private SearchView svSongs;
    SongsAdapter adapter;
    Session session = Session.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_tunes);

        lvSongs = findViewById(R.id.lvSongs);
        svSongs = findViewById(R.id.svSongs);
        mAPIService = ApiUtils.getAPIService();

        svSongs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                getSearch(s);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });

        lvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (session.getMediaPlayer()==null){
                    session.setMediaPlayer(new MediaPlayer());
                }
                Song song = session.getSongs().get(position);

                Fragment myfragment = null;
                if (song.getArtistId() != null && song.getCollectionId() != null && song.getTrackId() != null  && !song.getArtistId().toString().equalsIgnoreCase("") && !song.getCollectionId().toString().equalsIgnoreCase("") && !song.getTrackId().toString().equalsIgnoreCase("")){
                    myfragment = SongDetailFragment.newInstance(song.getCollectionName(),song.getArtworkUrl100(),song.getArtistName(),song.getTrackName(),song.getPreviewUrl(),song.getArtistId().toString()+song.getCollectionId().toString()+song.getTrackId().toString(),position) ;
                }else{
                    myfragment = SongDetailFragment.newInstance(song.getCollectionName(),song.getArtworkUrl100(),song.getArtistName(),song.getTrackName(),song.getPreviewUrl(),"",position) ;
                }

                goFragment(gTunesActivity.this, myfragment, true, "SongDetailFragment");

            }
        });

        if (session.getSongs() != null){
            if (session.getSongs().size()>0){
                adapter = new SongsAdapter(gTunesActivity.this,R.layout.itemrow_song,session.getSongs());
                lvSongs.setAdapter(adapter);
            }
        }

        SharedPreferences preferences = getBaseContext().getSharedPreferences("gTunesPreferences", Context.MODE_PRIVATE);
        svSongs.setQuery(preferences.getString("search", ""),false);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("search", "");
        editor.apply();
    }


    public void getSearch(final String search) {
        mAPIService.getSearch(search).enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                SharedPreferences.Editor editor = getSharedPreferences("gTunesPreferences", MODE_PRIVATE).edit();
                editor.putString("search", search);
                editor.apply();
                session.setSongs(response.body().getResults());
                adapter = new SongsAdapter(gTunesActivity.this,R.layout.itemrow_song,response.body().getResults());
                lvSongs.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                if(call.isCanceled()) {
                    Log.e("SplashActivity", "request was aborted");
                }else {
                    Log.e("SplashActivity", "Unable to submit post to API.");
                }
            }
        });
    }

    public void goFragment(AppCompatActivity activity, Fragment myfragment, Boolean addBackStack, String tag) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, myfragment,tag);
        if (addBackStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        if (session.getMediaPlayer() != null){
            if (session.getMediaPlayer().isPlaying()){
                session.getMediaPlayer().reset();
            }
        }
        super.onBackPressed();
    }

}
