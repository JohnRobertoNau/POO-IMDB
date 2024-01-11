package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;

public class Regular extends User /*implements RequestsManager*/ {
    @JsonCreator
    public Regular(@JsonProperty("username") String username,
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

    public void createRequest(Request request) {
        RequestsHolder.addRequest(request);

        // This reprezinta utilizatorul curent care creaza cererea
        request.attach(this);
    }

    public void removeRequest(Request request) {
        RequestsHolder.removeRequest(request);
        request.detach(this);
    }

    public Production findProduction(String productionTitle) {
        for (Production production : IMDB.getInstance().productions) {
            if (production.title.equals(productionTitle)) {
                return production;
            }
        }
        // if we don't find we will return null
        return null;
    }

    public void addReview(String productionTitle, int rating, String comment) {
        Production productionOptional = findProduction(productionTitle);
        if (productionOptional != null) {
            Rating newRating = new Rating(this.username, rating, comment);
            productionOptional.ratings.add(newRating);
            productionOptional.averageRating = productionOptional.calculateAverageRating();
        } else {
            throw new IllegalArgumentException("Productia nu s-a gasit");
        }
    }
}
