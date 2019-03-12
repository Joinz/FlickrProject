package com.joinz.flickerproject.imagebig;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.joinz.flickerproject.R;

public class ImageActivity extends AppCompatActivity {

    private ImageView ivFullscreen;
    private Button btnShare;
    private Button btnBack;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        initViews();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("url");
            Glide.with(ivFullscreen).load(url).into(ivFullscreen);
        }
    }

    private void initViews() {
        ivFullscreen = findViewById(R.id.ivFullscreen);
        btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, url);
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent, "Choose the program"));
            }
        });
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageActivity.this.finish();
            }
        });
    }
}
