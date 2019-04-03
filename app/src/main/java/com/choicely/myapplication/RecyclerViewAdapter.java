package com.choicely.myapplication;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private final List<Job> jobs = new ArrayList<>();
    private final WeakReference<Activity> weakActivity;

    public RecyclerViewAdapter(Activity act) {
        weakActivity = new WeakReference<>(act);
    }

    public void add(Job job) {
        jobs.add(job);
    }

    public void remove(Job job) {
        jobs.remove(job);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_list_row, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Job job = jobs.get(position);
        Log.d(TAG, "Element " + position + " set.");

        Picasso.get()
                .load(job.getImageUrl())
                .into(holder.image);

        holder.title.setText(job.getTitle());
        holder.description.setText(job.getDescription());

        holder.parentLayout.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, JobActivity.class);
            intent.putExtra("title", job.getTitle());

            Activity act = weakActivity.get();
            if(act != null) {
                String transitionString = "image";
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(act, holder.image, transitionString);
                ActivityCompat.startActivity(act, intent, options.toBundle());
            } else {
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private TextView textView;
        private TextView link;
        private LinearLayout parentLayout;
        private ImageView image;


        public ViewHolder(@NonNull View v) {
            super(v);

            title = v.findViewById(R.id.job_list_row_title);
            description = v.findViewById(R.id.job_list_row_description);
            image = v.findViewById(R.id.job_list_row_image_view);
            ViewCompat.setTransitionName(image, "image");
            link = v.findViewById(R.id.activity_job_row_link);
            parentLayout = v.findViewById(R.id.parent_layout);

            v.setOnClickListener(v1 -> {
                Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
            });

            textView = v.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }

}
