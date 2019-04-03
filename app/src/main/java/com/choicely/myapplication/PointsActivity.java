package com.choicely.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class PointsActivity extends AppCompatActivity {

    private Button increaseButton;
    private String stringValue;
    private int value = 0;
    private Realm realm;
    private Button listButton;
    private TextView textView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        textView = findViewById(R.id.points_view);
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

        increaseButton = findViewById(R.id.increase_button);
        increaseButton.setText("+");

        listButton = findViewById(R.id.list_button);
        listButton.setText("Job List");

        increaseButton.setOnClickListener(v -> {
            value++;
            stringValue = Integer.toString(value);
            textView.setText("Points: " + stringValue);
        });

        listButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
            startActivity(intent);
        });

        createObject();
    }

    private Point getPointsFromRealm() {
        Point point = realm.where(Point.class)
                .findFirst();
        return point;
    }

    public void createObject() {
        realm.beginTransaction();


        Point point = realm.where(Point.class)
                .findFirst();

        if (point == null) {
            point = new Point();
            point.setPoints(0);
            realm.copyToRealmOrUpdate(point);
        }

        realm.commitTransaction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
