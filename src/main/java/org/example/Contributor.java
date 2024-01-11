package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public abstract class Contributor extends Staff implements RequestsManager {
    @JsonCreator
    public Contributor(@JsonProperty("username") String username,
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

    @Override
    public void createRequest(Request request) {
        RequestsHolder.addRequest(request);
        request.attach(this);
    }

    @Override
    public void removeRequest(Request request) {
        RequestsHolder.removeRequest(request);
        request.detach(this);
    }
}