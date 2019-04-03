package com.choicely.myapplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Job extends RealmObject {

    @PrimaryKey
    private String title;
    private String description;
    private String link;
    private String imageUrl;

    public String getTitle(){
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}

