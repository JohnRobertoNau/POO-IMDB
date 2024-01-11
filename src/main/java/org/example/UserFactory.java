package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserFactory {
    public static User createUser(User.AccountType accountType, String username, int experience, User.Information.InformationBuilder information,
                                  List<String> productionsContributions, List<String> actorsContributions,
                                  List<String> favoriteProductions, List<String> favoriteActors, List<String> notifications) {

        User.Information information1 = information.builder();
        switch (accountType) {
            case Regular:
                return new Regular(username, experience, information1, accountType, productionsContributions, actorsContributions,
                        favoriteProductions, favoriteActors, notifications);

            case Contributor:
                return new Contributor(username, experience, information1, accountType, productionsContributions, actorsContributions,
                        favoriteProductions, favoriteActors, notifications) {
                    @Override
                    public void addProductionSystem(Production p) {

                    }

                    @Override
                    public void addActorSystem(Actor a) {

                    }

                    @Override
                    public void removeProductionSystem(String name) {

                    }

                    @Override
                    public void removeActorSystem(String name) {

                    }

                    @Override
                    public void updateProduction(Production p) {

                    }

                    @Override
                    public void updateActor(Actor a) {

                    }

                    @Override
                    public void manageRequest() {

                    }
                };
        }
        return new Admin(username, Integer.MAX_VALUE, information1, accountType, productionsContributions, actorsContributions,
                favoriteProductions, favoriteActors, notifications) {
            @Override
            public void addProductionSystem(Production p) {

            }

            @Override
            public void addActorSystem(Actor a) {

            }

            @Override
            public void removeProductionSystem(String name) {

            }

            @Override
            public void removeActorSystem(String name) {

            }

            @Override
            public void updateProduction(Production p) {

            }

            @Override
            public void updateActor(Actor a) {

            }

            @Override
            public void manageRequest() {

            }
        };
    }
}

