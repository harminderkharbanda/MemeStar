package com.memestar.memestar.data.data.remote;

import com.memestar.memestar.data.data.model.Meme;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MemeService {
    @GET("o/test%2Fmemes.json?alt=media&token=31fe17e4-3353-40ec-afbd-0f4756943006")
    Call<List<Meme>> getMemes();
}
