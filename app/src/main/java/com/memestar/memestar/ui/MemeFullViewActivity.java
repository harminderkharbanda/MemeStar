package com.memestar.memestar.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.memestar.memestar.Constants;
import com.memestar.memestar.R;
import com.squareup.picasso.Picasso;

public class MemeFullViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        setContentView(R.layout.activity_meme_full_view);

        ImageView fullImage = findViewById(R.id.meme_full_iv);

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.IntentConstant.IMAGE_URL)) {
            Picasso.get().load(intent.getStringExtra(Constants.IntentConstant.IMAGE_URL)).into(fullImage);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return true;
    }

}
