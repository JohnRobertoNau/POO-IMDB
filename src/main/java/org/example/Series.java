package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import kotlin.NotImplementedError;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties({"averageRating"})
public class Series extends Production {
    private Map<String, List<Episode>> seasons;
    private Integer numSeasons;

    @JsonCreator
    public Series(@JsonProperty("title") String title,
                  @JsonProperty("type") String type,
                  @JsonProperty("directors") List<String> directors,
                  @JsonProperty("actors") List<String> actors,
                  @JsonProperty("genres") List<Genre> genres,
                  @JsonProperty("ratings") List<Rating> ratings,
                  @JsonProperty("plot") String plot,
                  @JsonProperty("duration") String duration,
                  @JsonProperty("releaseYear") Integer releaseYear,
                  @JsonProperty("numSeasons") Integer numSeasons,
                  @JsonProperty("seasons") Map<String, List<Episode>> seasons) {
        super(title, type, directors, actors, genres, ratings, plot, null, releaseYear);
        this.numSeasons = numSeasons;
        this.seasons = seasons;
    }

    @Override
    public String toString() {
        return "title: " + title + "\n" + "type: " + type + "\n" +
                "directors: " + directors + "\n" + "actors " + actors + "\n"
                + genres + "\n" + "ratings: " + ratings + "\n"  +
                "plot: " + plot + "\n" + "averageRating: " + averageRating + "\n" + "duration: " + duration + "\n" +
                "releaseYear: " + releaseYear + "\n" + "numSeasons: " + numSeasons + "\n" +
                "seasons: " + seasons + "\n";
    }

    public Map<String, List<Episode>> getSeasons() {
        return seasons;
    }

    @Override
    public void displayInfo() {
        System.out.println("Informatii serial: ");
    }

    public void setNumSeasons(Integer numSeasons) {
        this.numSeasons = numSeasons;
    }

    public void setSeasons(Map<String, List<Episode>> seasons) {
        this.seasons = seasons;
    }

    public Integer getNumSeasons() {
        return numSeasons;
    }
}


