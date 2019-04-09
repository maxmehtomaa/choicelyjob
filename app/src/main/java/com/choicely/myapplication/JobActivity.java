package com.choicely.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import io.realm.Realm;

public class JobActivity extends AppCompatActivity {

    private static final String TAG = "JobActivity";
    private Realm realm;
    private TextView titleView;
    private TextView descView;
    private TextView linkView;
    private ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        Log.d(TAG, "onCreate: Activity started");
        realm = Realm.getDefaultInstance();

        titleView = findViewById(R.id.activity_job_row_title);
        descView = findViewById(R.id.activity_job_row_description);
        image = findViewById(R.id.activity_job_row_image);
        linkView = findViewById(R.id.activity_job_row_link);

        ViewCompat.setTransitionName(image, "image");

        getIncomingIntent();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: Checking for incoming intents");
        if (getIntent().hasExtra("title")) {
            Log.d(TAG, "getIncomingIntent: Found intent extras");

            String title = getIntent().getStringExtra("title");
            Job job = getJobData(title);

            setTitle(title);
            setDescription(job.getDescription());
            setImage(job.getImageUrl());
            setLink(job.getLink());
        }
    }

    private Job getJobData(String title) {

        Job job = realm.where(Job.class)
                .equalTo("title", title)
                .findFirst();

        return job;
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setDescription(String description) {
        descView.setText(description);
    }

    public void setImage(String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .into(image);
    }

    public void setLink(String link) {
        linkView.setText(link);
    }
}
