package com.memestar.memestar.data.data.remote;

import com.memestar.memestar.data.data.model.Meme;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MemeService {
    @GET("sooper/memes.json")
    Call<List<Meme>> getMemes();
}

