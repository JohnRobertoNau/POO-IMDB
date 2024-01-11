package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import kotlin.NotImplementedError;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Movie.class, name = "Movie"),
        @JsonSubTypes.Type(value = Series.class, name = "Series")
})
public abstract class Production extends Subject implements Comparable<Production> {
    String title;
    String type;
    List<String> directors;
    List<String> actors;
    List<Genre> genres;
    List<Rating> ratings;
    String plot;
    Double averageRating;
    String duration;
    Integer releaseYear;

    @JsonCreator
    public Production(@JsonProperty("title") String title,
                      @JsonProperty("type") String type,
                      @JsonProperty("directors") List<String> directors,
                      @JsonProperty("actors") List<String> actors,
                      @JsonProperty("genres") List<Genre> genres,
                      @JsonProperty("ratings") List<Rating> ratings,
                      @JsonProperty("plot") String plot,
                      @JsonProperty("duration") String duration,
                      @JsonProperty("releaseYear") Integer releaseYear) {
        this.title = title;
        this.type = type;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.plot = plot;
        this.averageRating = calculateAverageRating();
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    // am adaugat aici this:
     protected Double calculateAverageRating() {
        if (this.ratings.isEmpty()) {
            return 0.0;
        }
        double s = 0.0;
        for (Rating rating : this.ratings) {
            s = s + rating.rating;
        }
        return s / this.ratings.size();
    }

    public String toString() {
        return "title: " + title + "\n" + "type: " + type + "\n" + "directors: " + directors + "\n" +
                "actors: " + actors + "\n" + "genres: " + genres + "\n" + "ratings: " + ratings + "\n"  +
                "plot: " + plot + "\n" + "averageRating: " + averageRating + "\n" + "duration: " + duration + "\n" +
                "releaseYear: " + releaseYear + "\n";
    }

    public static class Genre {
        String name;

        @JsonCreator
        public Genre(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "genres: " + name;
        }

        public String getName() {
            return name;
        }
    }

    public void displayInfo() {
        System.out.println(this.toString());
    }

    @Override
    public int compareTo(Production o) {
        return this.title.compareTo(o.title);
    }

    public String getTitle() {
        return title;
    }

    public void addReview(User user, Rating rating) {
        this.ratings.add(rating);
        this.attach(user); // utilizatorul user este acum observator
        this.notifyObservers("The user " + user.username + " added a new review for " + this.title);
        this.averageRating = calculateAverageRating();
    }

    public void removeReview(User user, Rating rating) {
        this.ratings.remove(rating);
        this.attach(user);
        this.notifyObservers("A fost stearsa recenzia de la: " + rating.username + "pentru productia " + this.title);
        this.averageRating = calculateAverageRating();
    }
}


