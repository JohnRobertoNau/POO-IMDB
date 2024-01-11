package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class Staff extends User implements StaffInterface {
    private List<Request> staffRequests;
    private SortedSet<String> addItems;

    @JsonCreator
    public Staff(@JsonProperty("username") String username,
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
        this.staffRequests = new ArrayList<>();
        this.addItems = new TreeSet<>();
    }

    public static void addProduction(Production newProduction) {
        if (newProduction != null && !IMDB.getInstance().productions.contains(newProduction)) {
            IMDB.getInstance().productions.add(newProduction);
        } else {
            System.out.println("This production couldn't be added in system");
        }
    }
    public static void addActor(Actor newActor) {
        if (newActor != null && !IMDB.getInstance().actors.contains(newActor)) {
            IMDB.getInstance().actors.add(newActor);
        } else {
            System.out.println("The actor couldn't be added in system");
        }
    }
    public static void removeProduction(Production myProduction) {
        if (myProduction != null && IMDB.getInstance().productions.contains(myProduction)) {
            IMDB.getInstance().productions.remove(myProduction);
        } else {
            System.out.println("Couldn't remove this production from system");
        }
    }
    public static void removeActor(Actor myActor) {
        if (myActor != null && IMDB.getInstance().actors.contains(myActor)) {
            IMDB.getInstance().actors.remove(myActor);
        } else {
            System.out.println("Couldn't remove this actor from system");
        }
    }
}


