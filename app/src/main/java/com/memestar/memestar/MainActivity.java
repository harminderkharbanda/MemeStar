package com.memestar.memestar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.memestar.memestar.data.data.model.Meme;
import com.memestar.memestar.data.data.remote.ApiUtils;
import com.memestar.memestar.data.data.remote.MemeService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends Activity implements MemesAdapter.MemesClickListener {

    private MemeService memeService;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;
    private RecyclerView mRecyclerView;
    private MemesAdapter memesAdapter;
    private Parcelable mainActivityManagerSavedState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorTextView = findViewById(R.id.tv_error_message);
        mRecyclerView = findViewById(R.id.memes_recyclerview);
        memesAdapter = new MemesAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(memesAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));


        memeService = ApiUtils.getMemeService();
        loadRecipes();
    }

    private void loadRecipes() {
        memeService.getMemes().enqueue(new Callback<List<Meme>>() {
            @Override
            public void onResponse(Call<List<Meme>> call, Response<List<Meme>> response) {
                if (response.isSuccessful()) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mErrorTextView.setVisibility(View.INVISIBLE);
//                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
//                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), RecipeWidgetProvider.class));
//                    mRecipesAdapter.setRecipeData(response.body());
//                    mRemoteViewsFactory.setData(response.body());
//                    RecipeWidgetProvider.setRecipeIngredient(response.body());
//                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);

                    if (mainActivityManagerSavedState != null) {
                        restoreLayoutManagerPosition();
                    }
                    memesAdapter.setMemeData(response.body());
                    memesAdapter.notifyDataSetChanged();
                    Log.d("MainActivity", String.valueOf(response.body().get(0).getImageUrl()));
                } else {
                    int statusCode  = response.code();
                    Log.d("MainActivity", String.valueOf(statusCode) + "  " + String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<Meme>> call, Throwable t) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mErrorTextView.setVisibility(View.VISIBLE);
                Log.d("MainActivity", "error loading from API");
            }
        });
    }

    @Override
    public void onMemeClicked(Meme meme) {
        Toast.makeText(this, "Meme Clicked", Toast.LENGTH_SHORT).show();
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
}
