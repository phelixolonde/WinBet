package com.hansen.winbet;

/**
 * Created by Hansen on 03-Aug-17.
 */

public class NewsModel {
    String title,description,image,time;

    public NewsModel(String title, String description, String image, String time) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.time = time;
    }

    public String getTitle() {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
