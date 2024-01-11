package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Rating {
    String username;
    Integer rating;
    String comment;

    @JsonCreator
    public Rating(@JsonProperty("username") String username,
                  @JsonProperty("rating") Integer rating,
                  @JsonProperty("comment") String comment) {
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

    public String toString() {
        return "username: " + username + "\n" + "rating: " + rating + "\n" + "comment: " + comment;
    }

}
