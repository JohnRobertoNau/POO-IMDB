package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public abstract class Admin extends Staff {
    @JsonCreator
    public Admin(@JsonProperty("username") String username,
                 @JsonProperty("experience") int experience,
                 @JsonProperty("information") Information information,
                 @JsonProperty("userType") AccountType userType,
                 @JsonProperty("productionsContribution") List<String> productionsContributions,
                 @JsonProperty("actorsContributions") List<String> actorsContributions,
                 @JsonProperty("favoriteProductions") List<String> favoriteProductions,
                 @JsonProperty("favoriteActors") List<String> favoriteActors,
                 @JsonProperty("notifications") List<String> notifications) {
        super(username, experience, information, userType, productionsContributions,
                actorsContributions, favoriteProductions, favoriteActors, notifications);
    }

    static void deleteUser(String usernameToDelete) {
        int ok = 0;
        for (User user : IMDB.getInstance().users) {
            if (user.username.equals(usernameToDelete)) {
                IMDB.getInstance().users.remove(user);
                ok = 1;
            }
        }
        if (ok == 0) {
            throw new IllegalArgumentException("Userul nu s-a sters");
        }
    }

    public static void addUser(User userToAdd) {
        if (userToAdd == null) {
            throw new IllegalArgumentException("Userul este null");
        }
        IMDB.getInstance().users.add(userToAdd);
    }
}


