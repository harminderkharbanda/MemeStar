package com.memestar.memestar.data.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MemeContract {

    public static final String CONTENT_AUTHORITY = "com.memestar.memestar";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MEMES = "memes";

    public static final class MemeEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MEMES)
                .build();

        public static final String TABLE_NAME = "memes";

        public static final String COLUMN_MEME_IMAGEURL = "image_url";
        public static final String COLUMN_MEME_LANGUAGE = "language";
        public static final String COLUMN_MEME_CATEGORY = "category";
    }
}
