package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.example.RequestsHolder.addRequest;

public class Request extends Subject {
    private RequestType type;
    private LocalDateTime createdDate;
    private String variableInfo;
    private String description;
    private String username;
    private String to;

    @JsonCreator
    public Request(@JsonProperty("type") RequestType type,
                   @JsonProperty("createdDate") String createdDate,
                   @JsonProperty("username") String username,
                   @JsonProperty("actorName") String actorName,
                   @JsonProperty("movieTitle") String movieTitle,
                   @JsonProperty("to") String to,
                   @JsonProperty("description") String description) {
        this.type = type;
        this.createdDate = LocalDateTime.parse(createdDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.description = description;
        this.username = username;
        this.to = to;
        if (type.equals(RequestType.ACTOR_ISSUE)) {
            this.variableInfo = actorName;
        } else if (type.equals(RequestType.MOVIE_ISSUE)) {
            this.variableInfo = movieTitle;
        }
    }

    public String toString() {
        if (type.equals(RequestType.DELETE_ACCOUNT)) {
            return "type: " + type + "\n" +
                    "createdDate: " + createdDate + "\n" +
                    "username: " + username + "\n" +
                    "to: " + to + "\n" +
                    "description: " + description + "\n";
        }
        if (type.equals(RequestType.ACTOR_ISSUE)) {
            return "type: " + type + "\n" +
                    "createdDate: " + createdDate + "\n" +
                    "username: " + username + "\n" +
                    "actorName: " + variableInfo + "\n" +
                    "to: " + to + "\n" +
                    "description: " + description + "\n";
        }
        if (type.equals(RequestType.MOVIE_ISSUE)) {
            return "type: " + type + "\n" +
                    "createdDate: " + createdDate + "\n" +
                    "username: " + username + "\n" +
                    "movieTitle: " + variableInfo + "\n" +
                    "to: " + to + "\n" +
                    "description: " + description + "\n";
        }

        // If we get here it means the type is OTHERS
        return "type: " + type + "\n" +
                "createdDate: " + createdDate + "\n" +
                "username: " + username + "\n" +
                "to: " + to + "\n" +
                "description: " + description + "\n";
    }

    public enum RequestType {
        DELETE_ACCOUNT, ACTOR_ISSUE, MOVIE_ISSUE, OTHERS
    }

    public RequestType getType() {
        return type;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getVariableInfo() {
        return variableInfo;
    }

    public String getUsername() {
        return username;
    }
    public String getTo() {
        return to;
    }

    // Searching to find the user that added actorName
    public static String findUserThatAddedActor(String actorName) {

        for (User user1 : IMDB.getInstance().users) {
            if (user1.actorsContributions != null && user1.actorsContributions.contains(actorName)) {
                return user1.username;
            }
        }
        return "UNKNOWN";
    }

    public static String findUserThatAddedMovie(String movieTitle) {

        for (User user1 : IMDB.getInstance().users) {
            if (user1.productionsContributions != null && user1.productionsContributions.contains(movieTitle)) {
                return user1.username;
            }
        }
        return "UNKNOWN";
    }

    // Method that sets who should receive the request
    public static void autoRequest(Request request) {
        switch (request.getType()) {
            case DELETE_ACCOUNT, OTHERS:
                request.setTo("ADMIN");
                RequestsHolder.addRequest(request);
                break;
            case ACTOR_ISSUE:
                String actorResponsible = findUserThatAddedActor(request.getVariableInfo());
                if (actorResponsible.equals("UNKNOWN")) {
                    RequestsHolder.addRequest(request);
                    break;
                }
                request.setTo(actorResponsible);
                addRequestSimple(request);
                break;
            case MOVIE_ISSUE:
                String movieResponsible = findUserThatAddedMovie(request.getVariableInfo());
                if (movieResponsible.equals("UNKNOWN")) {
                    RequestsHolder.addRequest(request);
                    break;
                }
                request.setTo(movieResponsible);
                addRequestSimple(request);
                break;
        }
    }

    public static Request findRequestByType(User myUser, RequestType type) {
        for (Request request : IMDB.getInstance().requests) {
            if (request.username.equals(myUser.username) && request.getType().equals(type)) {
                return request;
            }
        }
        return null;
    }

    public void resolvedRequest(User user) {
        attach(user);
        notifyObservers("Your  " + this.type + " request about " + this.variableInfo + " was solved");
    }

    public void rejectRequest(User user) {
        attach(user);
        notifyObservers("Your " + this.type + " request about " + this.variableInfo + " was rejected.");
    }

    // add request, not for holder
    public static void addRequestSimple(Request request) {
        IMDB.getInstance().requests.add(request);
        String userTo = request.getTo();

        if (userTo != null) {
            // cautam prin utilizatori pe acela cu numele request.getTo pentru a-l notifica
            User toNotify = IMDB.getUserByUsername(userTo);
            if (toNotify != null) {
                request.attach(toNotify);
                toNotify.update("You recieved "+ request.getType() + " request");
            }
        }
    }
}


