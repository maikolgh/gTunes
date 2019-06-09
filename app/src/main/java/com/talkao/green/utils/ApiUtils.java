package com.talkao.green.utils;

import com.talkao.green.services.APIService;
import com.talkao.green.services.RetrofitClient;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "https://itunes.apple.com/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
