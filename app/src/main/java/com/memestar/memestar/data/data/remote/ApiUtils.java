package com.memestar.memestar.data.data.remote;

import com.memestar.memestar.Constants;

public class ApiUtils {

    public static MemeService getMemeService() {
        return RetrofitClient.getClient(Constants.SERVER_BASE_URL).create(MemeService.class);
    }
}
