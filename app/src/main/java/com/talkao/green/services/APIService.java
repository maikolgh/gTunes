package com.talkao.green.services;

import com.talkao.green.models.Search;
import com.talkao.green.models.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("search")
    Call<Search> getSearch(@Query("term") String search);
}
