package com.example.tarunkukreja.http.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tarunkukreja on 24/11/16.
 */
public class MovieModel {

    private String movie ;
    private String director ;
    private float rating ;
    private String tagline ;
    //Serializable name because the actual array name in the JSON is "cast"
    @SerializedName("cast") private List<Cast> castList ; //in <> there can be any class name(custom class)
    private String story ;
    private String duration ;
    private String image ;
    private int year ;

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public List<Cast> getCastList() {
        return castList;
    }

    public void setCastList(List<Cast> castList) {
        this.castList = castList;
    }

    public String getStory() {

        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDirector() {

        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public static class Cast {
        private String name ;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
