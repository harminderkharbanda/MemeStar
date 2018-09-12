package com.memestar.memestar.data.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.memestar.memestar.data.data.MemeContract.MemeEntry;

public class MemesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "memes.db";
    private static final int DATABASE_VERSION = 1;

    public MemesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MEMES_TABLE =
                "CREATE TABLE " + MemeEntry.TABLE_NAME + " (" +
                        MemeEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MemeEntry.COLUMN_MEME_IMAGEURL + " TEXT NOT NULL UNIQUE," +
                        MemeEntry.COLUMN_MEME_CATEGORY + " TEXT NOT NULL, " +
                        MemeEntry.COLUMN_MEME_LANGUAGE   + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_MEMES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MemeEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
