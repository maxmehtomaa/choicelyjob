package com.choicely.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";
    private OkHttpClient client = new OkHttpClient();
    private RecyclerView recyclerView;
    private JobAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String url = "http://qvik.fi/mobilesystems/jobs2017.json";

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new JobAdapter(this);
        recyclerView.setAdapter(adapter);

        fetchJsonAndPutToTextView(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void updateAdapter() {
        List<Job> jobs = getJobsFromRealm();
        for (Job job : jobs) {
            adapter.add(job);
        }
        adapter.notifyDataSetChanged();
    }


    private List<Job> getJobsFromRealm() {
        return realm.where(Job.class).findAll();
    }


    private void fetchJsonAndPutToTextView(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();

                    try {
                        //String to JSONObject
                        JSONObject json = new JSONObject(myResponse);
                        saveAllJobsToRealm(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, "response: " + myResponse);
                }
            }
        });

        Log.d(TAG, "wooop");
    }

    private void saveAllJobsToRealm(JSONObject json) {
        runOnUiThread(() -> {
            JSONArray jobs = null;
            try {
                jobs = json.getJSONArray("jobs");
                for (int index = 0; index < jobs.length(); index++) {
                    JSONObject job = jobs.getJSONObject(index);
                    saveJobToRealm(job);
                }
                updateAdapter();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void saveJobToRealm(JSONObject json) {
       runOnUiThread(() -> {
           String titleKey = null;
           String descKey = null;
           String imageKey = null;
           String linkKey = null;
           try {
               titleKey = json.getString("title");
               descKey = json.getString("description");
               imageKey = json.getString("image");
               linkKey = json.getString("link");
           } catch (JSONException e) {
               e.printStackTrace();
           }

           realm.beginTransaction();

           Job jobData = new Job();

           jobData.setTitle(titleKey);
           jobData.setDescription(descKey);
           jobData.setImageUrl(imageKey);
           jobData.setLink(linkKey);

           realm.copyToRealmOrUpdate(jobData);
           realm.commitTransaction();
       });
    }

    private boolean jobExistsInRealm(String key) {
        return realm.where(Job.class)
                .equalTo("title", "React Native developer")
                .findFirst() != null;
    }
}


