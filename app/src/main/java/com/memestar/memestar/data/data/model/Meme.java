package com.memestar.memestar.data.data.model;

import com.google.gson.annotations.SerializedName;

public class Meme {

    public String getImageUrl() {
        return imageUrl;
    }

    @SerializedName("url")
    public String imageUrl;

    public String getLanguage() {
        return language;
    }

    @SerializedName("language")
    public String language;

    public String getCat() {
        return cat;
    }

    @SerializedName("cat")
    public String cat;

}
