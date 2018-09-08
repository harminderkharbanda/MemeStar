package com.memestar.memestar.data.data.model;

import com.google.gson.annotations.SerializedName;

public class Meme {

    public String getImageUrl() {
        return imageUrl;
    }

    @SerializedName("url")
    private String imageUrl;

}
