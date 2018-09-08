package com.memestar.memestar.data.data.remote;

public class ApiUtils {

    public static final String BASE_URL = "https://firebasestorage.googleapis.com/v0/b/content-experiment-959f4.appspot.com/";

    public static MemeService getMemeService() {
        return RetrofitClient.getClient(BASE_URL).create(MemeService.class);
    }
}
