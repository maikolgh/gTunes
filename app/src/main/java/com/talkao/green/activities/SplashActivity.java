package com.talkao.green.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.talkao.green.R;
import com.talkao.green.models.Search;
import com.talkao.green.models.Session;
import com.talkao.green.services.APIService;
import com.talkao.green.utils.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    Session session = Session.getInstance();
    private APIService mAPIService;
    ProgressBar activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAPIService = ApiUtils.getAPIService();

        SharedPreferences preferences = getBaseContext().getSharedPreferences("gTunesPreferences", Context.MODE_PRIVATE);
        String lastSearch = preferences.getString("search", "");

        activity = findViewById(R.id.progressBarSplash);
        activity.setVisibility(View.VISIBLE);
        if (lastSearch.equalsIgnoreCase("")){
            activity.setVisibility(View.GONE);
            Intent intent = new Intent(this, gTunesActivity.class);
            startActivity(intent);
            ActivityCompat.finishAffinity(this);
        }else{
            getSearch(lastSearch);
        }
    }

    public void getSearch(final String search) {
        mAPIService.getSearch(search).enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                session.setSongs(response.body().getResults());
                activity.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), gTunesActivity.class);
                intent.putExtra("search", search);
                startActivity(intent);
                ActivityCompat.finishAffinity(SplashActivity.this);
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                activity.setVisibility(View.GONE);
                if(call.isCanceled()) {
                    Log.e("SplashActivity", "request was aborted");
                }else {
                    Log.e("SplashActivity", "Unable to submit post to API.");
                }
            }
        });
    }
}
