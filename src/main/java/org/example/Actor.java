package org.example;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Actor {
    private String name;
    private List<Performance> performances;
    private String biography;

    @JsonCreator
    public Actor(@JsonProperty("name") String name,
                 @JsonProperty("performances") List<Performance> performances,
                 @JsonProperty("biography") String biography) {
        this.name = name;
        this.performances = performances;
        this.biography = biography;
    }

    public String toString() {
        return "Name: " + name + "\n" + "Performance: " + performances + "\n" + "Biography: " + biography + "\n";
    }

    public static class Performance {
        private String title;
        private String type;

        @JsonCreator
        public Performance(@JsonProperty("title") String title,
                           @JsonProperty("type") String type) {
            this.title = title;
            this.type = type;
        }
        public String toString() {
            return "Title: " + title + "\n" + "Type: " + type;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
