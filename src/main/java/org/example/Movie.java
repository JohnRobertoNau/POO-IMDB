package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties({"averageRating"})
public class Movie extends Production {
    @JsonCreator
    public Movie(@JsonProperty("title") String title,
                 @JsonProperty("type") String type,
                 @JsonProperty("directors") List<String> directors,
                 @JsonProperty("actors") List<String> actors,
                 @JsonProperty("genres") List<Genre> genres,
                 @JsonProperty("ratings") List<Rating> ratings,
                 @JsonProperty("plot") String plot,
                 @JsonProperty("duration") String duration,
                 @JsonProperty("releaseYear") Integer releaseYear) {
        super(title, type, directors, actors, genres, ratings, plot, duration, releaseYear);
        this.averageRating = calculateAverageRating();
    }

    @Override
    public void displayInfo() {
        System.out.println("Informatii film: ");
        super.displayInfo();
    }
}
