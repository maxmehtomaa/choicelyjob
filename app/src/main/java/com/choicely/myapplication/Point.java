package com.choicely.myapplication;

import io.realm.RealmObject;

public class Point extends RealmObject {
    private int points;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }



}
