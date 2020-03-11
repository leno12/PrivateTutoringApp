package com.example.myapplication;

public class MyDataList {
    public int imageUrl;
    public String course_name;
    public String tutor;
    public String avg_rating;

    public MyDataList(){

    }

    public MyDataList(int imageUrl, String course_name, String tutor, String avg_rating) {
        this.imageUrl = imageUrl;
        this.course_name = course_name;
        this.tutor = tutor;
        this.avg_rating = avg_rating;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getTutor() {
        return tutor;
    }

    public void setTutor(String tutor) { this.tutor = tutor; }

    public String getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(String avg_rating) { this.avg_rating = avg_rating; }
}