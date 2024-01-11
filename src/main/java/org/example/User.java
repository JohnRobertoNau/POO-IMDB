package org.example;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDateTime;

public class User extends Subject implements Observer {
     String username;
     int experience;
     Information information;
     AccountType userType;
     List<String> productionsContributions;
     List<String> actorsContributions;
     List<String> favoriteProductions;
     List<String> favoriteActors;
     List<String> notifications;

    @JsonCreator
    public User(@JsonProperty("username") String username,
                @JsonProperty("experience") int experience,
                @JsonProperty("information") Information information,
                @JsonProperty("userType") AccountType userType,
                @JsonProperty("productionsContribution") List<String> productionsContributions,
                @JsonProperty("actorsContribution") List<String> actorsContributions,
                @JsonProperty("favoriteProductions") List<String> favoriteProductions,
                @JsonProperty("favoriteActors") List<String> favoriteActors,
                @JsonProperty("notifications") List<String> notifications) {
        this.username = username;
        this.experience = experience;
        this.information = information;
        this.userType = userType;
        this.productionsContributions = /*(productionsContributions != null) ? */productionsContributions /*: new ArrayList<>()*/;
        this.actorsContributions = actorsContributions;
        this.favoriteProductions = favoriteProductions;
        this.favoriteActors = favoriteActors;
        this.notifications = (notifications != null) ? notifications : new ArrayList<>();
    }

    public static class Information {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private String gender;
        private LocalDate birthDate;
        @JsonCreator
         protected Information(@JsonProperty("credentials") Credentials credentials,
                            @JsonProperty("name") String name,
                            @JsonProperty("country") String country,
                            @JsonProperty("age") int age,
                            @JsonProperty("gender") String gender,
                            @JsonProperty("birthDate") String birthDate) {
            this.credentials = credentials;
            this.name = name;
            this.country = country;
            this.age = age;
            this.gender = gender;
            this.birthDate = LocalDate.parse(birthDate, DateTimeFormatter.ISO_LOCAL_DATE);
        }
        public String getName() {
            return name;
        }
        public String getCountry() {
            return country;
        }
        public int getAge() {
            return age;
        }
        public String getGender() {
            return gender;
        }
        public LocalDate getBirthDate() {
            return birthDate;
        }

        public static class InformationBuilder {
            private Credentials credentials;
            private String name;
            private String country;
            private int age;
            private String gender;
            private String birthDate;

            public InformationBuilder(Credentials credentials, String name, String country, int age,
                                      String gender, String birthDate) {
                this.credentials = credentials;
                this.name = name;
                this.country = country;
                this.age = age;
                this.gender = gender;
                this.birthDate = birthDate;
            }

            public InformationBuilder forCredentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }

            public InformationBuilder forName(String name) {
                this.name = name;
                return this;
            }

            public InformationBuilder forCountry(String country) {
                this.country = country;
                return this;
            }

            public InformationBuilder forAge(int age) {
                this.age = age;
                return this;
            }

            public InformationBuilder forGender(String gender) {
                this.gender =gender;
                return this;
            }

            public InformationBuilder forBirthDate(String birthDate) {
                this.birthDate = birthDate;
                return this;
            }

            public Information builder() {
                return new Information(credentials, name, country, age, gender, birthDate);
            }
        }

        public String toString() {
            return "credentials: " + credentials + "\n" +
                    "name: " + name + "\n" +
                    "country: " + country + "\n" +
                    "age: " + age + "\n" +
                    "gender: " + gender + "\n" +
                    "birthDate: " + birthDate;
        }

        public Credentials getCredentials() {
            return credentials;
        }
    }

    public String toString() {
        if (userType.equals(AccountType.Contributor)) {
            return "username: " + username + "\n" +
                    "experience: " + experience + "\n" +
                    "information: " + information + "\n" +
                    "userType: " + userType + "\n" +
                    "productionsContribution: " + productionsContributions + "\n" +
                    "actorsContributions: " + actorsContributions + "\n" +
                    "favoriteProductions: " + favoriteProductions + "\n" +
                    "favoriteActors: " + favoriteActors + "\n" +
                    "notifications: " + notifications + "\n";
        }

        if (userType.equals(AccountType.Regular)) {
            return "username: " + username + "\n" +
                    "experience: " + experience + "\n" +
                    "information: " + information + "\n" +
                    "userType: " + userType + "\n" +
                    "notifications: " + notifications;
        }

        // If it is an Admin
        return "username: " + username + "\n" +
                "experience: " + experience + "\n" +
                "information: " + information + "\n" +
                "userType: " + userType + "\n" +
                "productionsContribution: " + productionsContributions + "\n" +
                "actorsContributions: " + actorsContributions + "\n" +
                "notifications: " + notifications;
    }

    public Information getInformation() {
        return information;
    }

    public int getExperience() {
        return experience;
    }

    public String getUsername() {
        return username;
    }
    public AccountType getUserType() {
        return userType;
    }
    @Override
    public void update(String notification) {
        notifications.add(notification);
    }
    public enum AccountType {
        Contributor, Regular, Admin
    }

    public void displayNotifications() {
        for (String notification : notifications) {
            System.out.println(notification);
        }
    }

    public boolean isUniqueUsername(String username) {
        for (User user : IMDB.getInstance().users) {
            if (user.username.equals(username)) {
                return false;
            }
        }
        return true;
    }

    public String createUsername(String name) {
        String transformedUsername = name.toLowerCase().replace(" ", "_");
        String copy = transformedUsername;
        Random random = new Random();

        int addition = random.nextInt(100);
        while (!isUniqueUsername(copy)) {
            copy = transformedUsername + addition;
        }
        return copy;
    }

    // Method that returns all the productions contributions that a user has
    public static List<String> userGetProductionsContributions(User user) {
        List<String> myProdCont = new ArrayList<>();
        int isFound = 0;
        if (user.productionsContributions != null) {
            for (String prodCont : user.productionsContributions) {
                myProdCont.add(prodCont);
            }
            isFound = 1;
        }
        if (isFound == 0) {
            return null;
        }
        return myProdCont;
    }

    // analog for actors contributions
    public static List<String> userGetActorsContributions(User user) {
        List<String> myActCont = new ArrayList<>();
        int isFound = 0;
        if (user.actorsContributions != null) {
            for (String actCont : user.actorsContributions) {
                myActCont.add(actCont);
            }
            isFound = 1;
        }
        if (isFound == 0) {
            return null;
        }
        return myActCont;
    }

    public static void removeUser(User userToRemove) {
        // Managing production contributions
        if (userToRemove.userType.equals(AccountType.Contributor)) {
            List<String> myProdCont = userGetProductionsContributions(userToRemove);
            if (myProdCont != null) {
                for (String prodCont : myProdCont) {
                    for (User user : IMDB.getInstance().users) {
                        if (user.userType.equals(AccountType.Admin)) {
                            user.productionsContributions.add(prodCont);
                            break;
                        }
                    }
                }
            }
            // Managing actors contribution
            List<String> myActorCont = userGetActorsContributions(userToRemove);
            if (myActorCont != null) {
                for (String actCont : myActorCont) {
                    for (User user : IMDB.getInstance().users) {
                        if (user.userType.equals(AccountType.Admin)) {
                            user.actorsContributions.add(actCont);
                            break;
                        }
                    }
                }
            }
            for (Production production : IMDB.getInstance().productions) {
                Rating myRating = IMDB.getInstance().findRatingByUsername(userToRemove, production);
                if (myRating != null) {
                    production.ratings.remove(myRating);
                }
            }
        } else {
            for (Production production : IMDB.getInstance().productions) {
                Rating myRating = IMDB.getInstance().findRatingByUsername(userToRemove, production);
                if (myRating != null) {
                    production.ratings.remove(myRating);
                }
            }
        }
        IMDB.getInstance().users.remove(userToRemove);
    }
}
