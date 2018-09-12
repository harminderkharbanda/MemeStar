package com.memestar.memestar.ui;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.memestar.memestar.Constants;
import com.memestar.memestar.MemesAdapter;
import com.memestar.memestar.R;
import com.memestar.memestar.data.data.MemeContract;
import com.memestar.memestar.data.data.model.Meme;
import com.memestar.memestar.data.data.remote.ApiUtils;
import com.memestar.memestar.data.data.remote.MemeService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements MemesAdapter.MemesClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";
    private MemeService memeService;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;
    private RecyclerView mRecyclerView;
    private MemesAdapter memesAdapter;
    private Parcelable mainActivityManagerSavedState;
    private Button buttonAll;
    private Button buttonBollywood;
    private Button buttonSports;
    private static final int TASK_LOADER_ID = 1;
    List<Meme> memeArrayData;
    private String buttonType = Constants.BUTTONTYPE_ALL;
    public static int memeIndex = 0;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String imagePath;
    private boolean isWhatsAppOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mErrorTextView = findViewById(R.id.tv_error_message);
        buttonAll = findViewById(R.id.btn_all);
        buttonBollywood = findViewById(R.id.btn_bolly);
        buttonSports = findViewById(R.id.btn_sports);
        mRecyclerView = findViewById(R.id.memes_recyclerview);

        mProgressBar.setVisibility(View.VISIBLE);

        memesAdapter = new MemesAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(memesAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        MobileAds.initialize(this, Constants.MOBILE_ADS_APP_ID);
        AdView adView_fragment = findViewById(R.id.adView_free);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView_fragment.loadAd(adRequest);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        memeService = ApiUtils.getMemeService();
        loadRecipes();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
            AppBarLayout appBarLayout = findViewById(R.id.appBar);
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                boolean isShow = true;
                int scrollRange = -1;

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }
                    if (scrollRange + verticalOffset == 0) {
                        collapsingToolbarLayout.setTitle(Constants.MEMESTAR_TITLE);
                        isShow = true;
                    } else if (isShow) {
                        collapsingToolbarLayout.setTitle(" ");
                        isShow = false;
                    }
                }
            });
        }

        buttonBollywood.setBackgroundResource(android.R.drawable.btn_default);
        buttonSports.setBackgroundResource(android.R.drawable.btn_default);

        buttonBollywood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonAll.setBackgroundResource(android.R.drawable.btn_default);
                buttonBollywood.setBackgroundResource(R.drawable.button_selector);
                buttonSports.setBackgroundResource(android.R.drawable.btn_default);
                buttonType = Constants.BUTTONTYPE_BOLLYWOOD;
                getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, MainActivity.this).forceLoad();
            }
        });

        buttonSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonAll.setBackgroundResource(android.R.drawable.btn_default);
                buttonBollywood.setBackgroundResource(android.R.drawable.btn_default);
                buttonSports.setBackgroundResource(R.drawable.button_selector);
                buttonType = Constants.BUTTONTYPE_SPORTS;
                getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, MainActivity.this).forceLoad();
            }
        });

        buttonAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonBollywood.setBackgroundResource(android.R.drawable.btn_default);
                buttonAll.setBackgroundResource(R.drawable.button_selector);
                buttonSports.setBackgroundResource(android.R.drawable.btn_default);
                buttonType = Constants.BUTTONTYPE_ALL;
                getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, MainActivity.this).forceLoad();
            }
        });
    }

    private void loadRecipes() {
        memeService.getMemes().enqueue(new Callback<List<Meme>>() {
            @Override
            public void onResponse(Call<List<Meme>> call, Response<List<Meme>> response) {
                if (response.isSuccessful()) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mErrorTextView.setVisibility(View.INVISIBLE);

                        for (int i = 0; i < response.body().size(); i++) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(MemeContract.MemeEntry.COLUMN_MEME_IMAGEURL, response.body().get(i).getImageUrl());
                            contentValues.put(MemeContract.MemeEntry.COLUMN_MEME_LANGUAGE, response.body().get(i).getLanguage());
                            contentValues.put(MemeContract.MemeEntry.COLUMN_MEME_CATEGORY, response.body().get(i).getCat());

                            try {
                                getContentResolver().insert(MemeContract.MemeEntry.CONTENT_URI, contentValues);
                            } catch (Exception e) {
                                Log.d(TAG, "Possibly duplicate entry");
                            }
                        }

                    getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, MainActivity.this);

                    Log.d(TAG, String.valueOf(response.body()));
                } else {
                    int statusCode  = response.code();
                    Log.d(TAG, String.valueOf(statusCode) + "  " + String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<Meme>> call, Throwable t) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mErrorTextView.setVisibility(View.VISIBLE);
                Log.d(TAG, "error loading from API");
            }
        });
    }

    @Override
    public void onMemeClicked(Meme meme) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Constants.ANALYTICS_ITEM_ID);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, Constants.ANALYTICS_ITEM_NAME);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Constants.ANALYTICS_CONTENT_TYPE);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        Intent intent = new Intent(this, MemeFullViewActivity.class);
        intent.putExtra(Constants.IntentConstant.IMAGE_URL, meme.getImageUrl());
        startActivity(intent);
    }

    @Override
    public void onWAClicked(Meme meme) {
        isWhatsAppOnly = true;
        processDownload(meme.imageUrl);
    }

    @Override
    public void onShareClicked(Meme meme) {
        isWhatsAppOnly = false;
        processDownload(meme.imageUrl);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (mRecyclerView.getLayoutManager() != null) {
            bundle.putParcelable(Constants.SAVED_POSITION_MAIN_ACTIVITY, mRecyclerView.getLayoutManager().onSaveInstanceState());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mainActivityManagerSavedState = savedInstanceState.getParcelable(Constants.SAVED_POSITION_MAIN_ACTIVITY);
        }
    }

    private void restoreLayoutManagerPosition() {
        if (mainActivityManagerSavedState != null &&  mRecyclerView.getLayoutManager() != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mainActivityManagerSavedState);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mCursor = null;

            @Override
            protected void onStartLoading() {
                if (mCursor != null) {
                    deliverResult(mCursor);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {

                try {
                    if (buttonType.equals(Constants.BUTTONTYPE_ALL)) {
                        return getContentResolver().query(MemeContract.MemeEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                null);
                    } else if (buttonType.equals(Constants.BUTTONTYPE_BOLLYWOOD)) {
                        String[] args = {Constants.BUTTONTYPE_BOLLYWOOD};
                        return getContentResolver().query(MemeContract.MemeEntry.CONTENT_URI,
                                null,
                                "category=?",
                                args,
                                null);
                    } else {
                        String[] args = {Constants.BUTTONTYPE_SPORTS};
                        return getContentResolver().query(MemeContract.MemeEntry.CONTENT_URI,
                                null,
                                "category=?",
                                args,
                                null);
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mCursor = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null) {
            cursor.moveToFirst();
            memeArrayData = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                Meme currentMeme = new Meme();
                currentMeme.imageUrl = cursor.getString(cursor.getColumnIndex(MemeContract.MemeEntry.COLUMN_MEME_IMAGEURL));
                currentMeme.language = cursor.getString(cursor.getColumnIndex(MemeContract.MemeEntry.COLUMN_MEME_LANGUAGE));
                currentMeme.cat = cursor.getString(cursor.getColumnIndex(MemeContract.MemeEntry.COLUMN_MEME_CATEGORY));
                memeArrayData.add(currentMeme);
                cursor.moveToNext();
            }
        }
        memesAdapter.setMemeData(memeArrayData);
        if (mainActivityManagerSavedState != null) {
            restoreLayoutManagerPosition();
        }
        memesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void processDownload(final String url) {
        mProgressBar.setVisibility(View.VISIBLE);
        String name = url.substring(12);
        if(TextUtils.isEmpty(url) || TextUtils.isEmpty(name))
        {
            return;
        }
        File externalFilesDir = getApplicationContext().getExternalFilesDir(null);
        if(externalFilesDir == null)
        {
            externalFilesDir = getApplication().getCacheDir();
        }
        final String downloadPath = externalFilesDir.getAbsolutePath() + "/" + name;

        imagePath = downloadPath;
        if(new File(downloadPath).exists())
        {
            shareImage(url,isWhatsAppOnly);
            return;

        }
        Uri image_uri = Uri.parse(url);

        DownloadManager.Request request = buildRequest(name, image_uri);
        final DownloadManager downloadManager = (DownloadManager) getApplicationContext().getSystemService(DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mProgressBar.setVisibility(View.GONE);
                    getApplicationContext().unregisterReceiver(this);
                    shareImage(url, isWhatsAppOnly);


            }
        };

        getApplicationContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private DownloadManager.Request buildRequest(String name, Uri image_uri) {
        DownloadManager.Request request = new DownloadManager.Request(image_uri);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI|DownloadManager.Request.NETWORK_MOBILE);
        request.allowScanningByMediaScanner();
        request.setTitle(name);
        request.setDestinationInExternalFilesDir(getApplicationContext(),null,name);
        request.setVisibleInDownloadsUi(false);
        request.setAllowedOverMetered(true);
        request.setAllowedOverRoaming(true);
        return request;
    }

    private void shareImage(String url, boolean whatsappOnly) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        if (whatsappOnly) {
            intent.setPackage(Constants.WHATSAPP_PACKAGE_NAME);
        }
        intent.putExtra(Intent.EXTRA_TEXT,!TextUtils.isEmpty(Constants.MEME_SHARE_TEXT_BODY) ? Constants.MEME_SHARE_TEXT_BODY : "");

        if (!TextUtils.isEmpty(url)) {
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imagePath));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
            startActivity(intent);
    }
}
