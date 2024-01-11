package org.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    public static List<Actor> loadActors() {
        List<Actor> actors = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        try {
            actors = mapper.readValue(new File("src/main/resources/input/actors.json"), mapper.getTypeFactory().constructCollectionType(List.class, Actor.class));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return actors;
    }

    public static List<Production> loadProduction() {
        List<Production> productions = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        try {
            productions = mapper.readValue(new File("src/main/resources/input/production.json"), mapper.getTypeFactory().constructCollectionType(List.class, Production.class));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return productions;
    }

    public static List<Request> loadRequests() {
        List<Request> requests = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        try {
            requests = mapper.readValue(new File("src/main/resources/input/requests.json"), mapper.getTypeFactory().constructCollectionType(List.class, Request.class));
            for (Request request : requests) {
                if (request.getTo().equals("ADMIN")) {
                    RequestsHolder.addRequest(request);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return requests;
    }

    public static List<User> loadUser() {
        List<User> users = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        try {
            List<User> myUsers = mapper.readValue(new File("src/main/resources/input/accounts.json"), mapper.getTypeFactory().constructCollectionType(List.class, User.class));
            for (User myUser : myUsers) {
                User.Information.InformationBuilder informationBuilder = new User.Information.InformationBuilder(myUser.information.getCredentials(),
                        myUser.information.getName(), myUser.information.getCountry(), myUser.information.getAge(), myUser.information.getGender(),
                        myUser.information.getBirthDate().toString());

                User user = UserFactory.createUser(myUser.userType, myUser.username, myUser.experience, informationBuilder,
                        myUser.productionsContributions, myUser.actorsContributions, myUser.favoriteProductions, myUser.favoriteActors,
                        myUser.notifications);
                users.add(user);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return users;
    }
}
