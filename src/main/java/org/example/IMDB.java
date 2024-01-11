package org.example;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.swing.*;

public class IMDB {
    List<Actor> actors;
    List<Request> requests;
    List<Production> productions;
    List<User> users;

    private static IMDB instance = null;

    //Constructor
    private IMDB() {

    }

    public static IMDB getInstance() {
        if (instance == null)
            instance = new IMDB();
        return instance;
    }

    public void run() {
        actors = Loader.loadActors();
        productions = Loader.loadProduction();
        users = Loader.loadUser();
        requests = Loader.loadRequests();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Alege modul prin care vrei sa utilizezi aplicatia. Apasa:");
        System.out.println("1: pentru terminal");
        System.out.println("2: pentru interfata grafica");

        int mode = scanningNextInt(scanner);
        if (mode == 1) {
            boolean isAuthenticated = false;

            loggingInLoop:
            while (isAuthenticated == false) {
                System.out.println("Enter email: ");
                String email = scanner.next();

                System.out.println("Enter password: ");
                String password = scanner.next();

                // Verificam daca parola si emailul corespund
                if (verifyPassword(email, password) == false) {
                    System.out.println("Email or password wrong");
                } else {
                    System.out.println("Welcome, " + findUsernameByEmail(email) + " !");
                    isAuthenticated = true;
                    User myUser = getUserByUsername(findUsernameByEmail(email));
                    System.out.println("Tou have a " + myUser.userType + " account.");
                    System.out.println("Experience: " + myUser.experience);

                    switch (myUser.userType) {
                        case Regular:
                            while (true) {
                                System.out.println("Choose action:");
                                System.out.println("    1) View productions details");
                                System.out.println("    2) View actors details");
                                System.out.println("    3) View notifications");
                                System.out.println("    4) Search for actor/movie/series");
                                System.out.println("    5) Add/Delete actor/movie/series to/from favorites");
                                System.out.println("    6) Do/Undo a request");
                                System.out.println("    7) Add/Delete a review for a production");
                                System.out.println("    8) Logout");

                                int action = scanningNextInt(scanner);
                                switch (action) {
                                    case 1:
                                        System.out.println("Enter your genre.");
                                        System.out.println("If you don't want a specific genre, type null");
                                        String genreRead = scanner.next();

                                        System.out.println("Enter your minimum number of ratings.");
                                        System.out.println("If you don't want a minimum number of ratings for your search, type 0");

                                        int nrRatingsRead = scanningNextInt(scanner);
                                        filterProduction(genreRead, nrRatingsRead);
                                        break;

                                    case 2:
                                        System.out.println("Do you want to see the actors sorted by name?");
                                        System.out.println("1) yes");
                                        System.out.println("2) no");
                                        int isSorted = scanningNextInt(scanner);

                                        List<Actor> copyActor = new ArrayList<>(actors);

                                        if (isSorted == 1) {
                                            sortActors();
                                            System.out.println(actors.toString());
                                        } else {
                                            System.out.println(actors.toString());
                                        }
                                        actors = copyActor;
                                        break;

                                    case 3:
                                        myUser.displayNotifications();
                                        break;

                                    case 4:
                                        System.out.println("Enter the name of a movie/series/actor");
                                        String forSearch = scanner.nextLine();
                                        searchingAlgorithm(forSearch);
                                        break;

                                    case 5:
                                        // Implement adding or deleting a production from favorites
                                        System.out.println("How do you want to update your favorite list?");
                                        System.out.println("    1) Add a production to favourites");
                                        System.out.println("    2) Add an actor to favorites");
                                        System.out.println("    3) Remove a production from favorites");
                                        System.out.println("    4) Remove an actor from favorites");
                                        int nrAction = scanningNextInt(scanner);

                                        if (nrAction == 1) {
                                            System.out.println("Type your production to add to favorites");
                                            String toAdd = scanner.nextLine();
                                            Production productionToAdd = findProductionByName(toAdd);
                                            if (productionToAdd == null) {
                                                System.out.println("This production doesn't exist in the system");
                                                break;
                                            } else {
                                                myUser.favoriteProductions.add(productionToAdd.title);
                                                System.out.println("Production " + productionToAdd.title + " has been added");
                                                System.out.println("Here is your new favorite productions list:");
                                                System.out.println(myUser.favoriteProductions.toString());
                                            }
                                        } else if (nrAction == 2) {
                                            System.out.println("Type the name of the actor you want to add to favorites");
                                            String toAdd = scanner.nextLine();
                                            Actor actorToAdd = findActorByName(toAdd);
                                            if (actorToAdd == null) {
                                                System.out.println("This actor doesn't exist");
                                                break;
                                            } else {
                                                myUser.favoriteActors.add(actorToAdd.getName());
                                                System.out.println("The actor " + actorToAdd.getName() + " has been added to favorites");
                                                System.out.println("Here is your new favorite actors list");
                                                System.out.println(myUser.favoriteActors.toString());
                                            }
                                        } else if (nrAction == 3) {
                                            System.out.println("Type the name of the production you want to remove from favorites");
                                            String toRemove = scanner.nextLine();
                                            removeProductionFromFavorites(toRemove, myUser);
                                            System.out.println("Here is your production favorite list:");
                                            System.out.println(myUser.favoriteProductions.toString());
                                        } else if (nrAction == 4) {
                                            System.out.println("Type the name of the actor you want to remove from your favorite list");
                                            String toRemove = scanner.nextLine();
                                            removeActorFromFavorites(toRemove, myUser);
                                            System.out.println("Here is your actor favorite list");
                                            System.out.println(myUser.favoriteActors.toString());
                                        }
                                        break;

                                    case 6:
                                        // Implement requesting
                                        System.out.println("How do you want to manage your requests?");
                                        System.out.println("    1) Create a new request");
                                        System.out.println("    2) Cancel a request");
                                        int nrAction6 = scanningNextInt(scanner);

                                        if (nrAction6 == 1) {
                                            System.out.println("What is the type of the request?");
                                            System.out.println("    1) DELETE_ACCOUNT");
                                            System.out.println("    2) ACTOR_ISSUE");
                                            System.out.println("    3) MOVIE_ISSUE");
                                            System.out.println("    4) OTHERS");
                                            int nrType = scanningNextInt(scanner);

                                            // DELETE_ACCOUNT or OTHERS
                                            if (nrType == 1 || nrType == 4) {
                                                System.out.println("Enter the description of your request");
                                                String descriptionRequest = scanner.nextLine();

                                                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                                String formattedDate = LocalDateTime.now().format(formatterDate);

                                                // Pentru a fi in concordanta cu formatul ISO_LOCAL_DATE_TIME
                                                formattedDate = formattedDate.replace(" ", "T");


                                                Request request = new Request(Request.RequestType.DELETE_ACCOUNT, formattedDate,
                                                        myUser.username, null, null, null, descriptionRequest);

                                                Request.autoRequest(request);
                                                System.out.println("This is your request:");
                                                System.out.println(request.toString() + "\n");
                                                break;

                                                // ACTOR_ISSUE
                                            } else if (nrType == 2) {
                                                System.out.println("Enter the name of the Actor");
                                                String actorNameCase = scanner.nextLine();

                                                System.out.println("Enter the description for your request");
                                                String descriptionRequest2 = scanner.nextLine();

                                                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                                String formattedDate = LocalDateTime.now().format(formatterDate);

                                                // Pentru a fi in concordanta cu formatul ISO_LOCAL_DATE_TIME
                                                formattedDate = formattedDate.replace(" ", "T");


                                                Request myRequest = new Request(Request.RequestType.ACTOR_ISSUE, formattedDate, myUser.username, actorNameCase, null, null, descriptionRequest2);
                                                Request.autoRequest(myRequest);
                                                break;
                                                // MOVIE_ISSUE
                                            } else if (nrType == 3) {
                                                System.out.println("Enter the name of the Movie:");
                                                String movieNameCase = scanner.nextLine();

                                                System.out.println("Enter the description  of your request:");
                                                String myDescription = scanner.nextLine();

                                                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                                String formattedDate = LocalDateTime.now().format(formatterDate);

                                                // Pentru a fi in concordanta cu formatul ISO_LOCAL_DATE_TIME
                                                formattedDate = formattedDate.replace(" ", "T");

                                                Request request = new Request(Request.RequestType.MOVIE_ISSUE, formattedDate, myUser.username,
                                                        null, movieNameCase, null, myDescription);
                                                Request.autoRequest(request);
                                                break;
                                            }

                                            // Remove a request
                                        } else if (nrAction6 == 2) {
                                            System.out.println("Enter your type of request you'd like to remove");
                                            System.out.println("    1) DELETE_ACCOUNT");
                                            System.out.println("    2) ACTOR_ISSUE");
                                            System.out.println("    3) MOVIE_ISSUE");
                                            System.out.println("    4) OTHERS");
                                            int nrType = scanningNextInt(scanner);

                                            // DELETE_ACCOUNT
                                            if (nrType == 1) {
                                                Request myRequest = Request.findRequestByType(myUser, Request.RequestType.DELETE_ACCOUNT);
                                                if (myRequest == null) {
                                                    System.out.println("No request has been found");
                                                    break;
                                                }
                                                RequestsHolder.removeRequest(myRequest);
                                                requests.remove(myRequest);
                                                RequestsHolder.notifyUser(myUser.username, "Request " + myRequest + "has been removed");

                                                // ACTOR_ISSUE
                                            } else if (nrType == 2) {
                                                Request myRequest = Request.findRequestByType(myUser, Request.RequestType.ACTOR_ISSUE);
                                                if (myRequest == null) {
                                                    System.out.println("No request has been found");
                                                    break;
                                                }
                                                RequestsHolder.removeRequest(myRequest);
                                                requests.remove(myRequest);
                                                RequestsHolder.notifyUser(myUser.username, "Request " + myRequest + "has been removed");

                                                // MOVIE_ISSUE
                                            } else if (nrType == 3) {
                                                Request myRequest = Request.findRequestByType(myUser, Request.RequestType.MOVIE_ISSUE);
                                                if (myRequest == null) {
                                                    System.out.println("No request has been found");
                                                    break;
                                                }
                                                RequestsHolder.removeRequest(myRequest);
                                                requests.remove(myRequest);
                                                RequestsHolder.notifyUser(myUser.username, "Request " + myRequest + "has been removed");

                                                // OTHERS
                                            } else if (nrType == 4) {
                                                Request myRequest = Request.findRequestByType(myUser, Request.RequestType.OTHERS);
                                                if (myRequest == null) {
                                                    System.out.println("No request has been found");
                                                    break;
                                                }
                                                RequestsHolder.removeRequest(myRequest);
                                                requests.remove(myRequest);
                                                RequestsHolder.notifyUser(myUser.username, "Request " + myRequest + "has been removed");
                                            }
                                        }
                                        break;

                                    case 7:
                                        System.out.println("How do you want to manage your reviews?");
                                        System.out.println("    1) I want to add a review");
                                        System.out.println("    2) I want to remove a review");
                                        int noCase7 = scanningNextInt(scanner);

                                        // Adding a review
                                        if (noCase7 == 1) {
                                            System.out.println("Enter the name of the production");
                                            String prodName = scanner.nextLine();

                                            Production myProduction = findProductionByName(prodName);
                                            if (myProduction == null) {
                                                System.out.println("There is no production named " + prodName);
                                                break;
                                            }

                                            // Checking if this user already has a rating for this production
                                            if (myProduction.ratings.contains(findRatingByUsername(myUser, myProduction))) {
                                                System.out.println("You already added a rating for this production.");
                                                System.out.println("If you want to add another one, please remove it first.");
                                                break;
                                            }

                                            System.out.println("What is your rating? Please enter an int");
                                            int myMark = scanningNextInt(scanner);
                                            if (myMark < 0 || myMark > 10) {
                                                break;
                                            }

                                            System.out.println("What is your comment?");
                                            String myComment = scanner.nextLine();

                                            Rating myRating = new Rating(myUser.username, myMark, myComment);
                                            myProduction.addReview(myUser, myRating);

                                            // Update experience
                                            RatingStrategy ratingStrategy = new RatingStrategy();
                                            int updatedExperience = ratingStrategy.calculateExperience(myUser.experience);
                                            myUser.experience = updatedExperience;
                                            System.out.println("Your new experience is: " + myUser.experience);

                                            // Removing a review
                                        } else if (noCase7 == 2) {
                                            System.out.println("Enter the name of the production");
                                            String prodName = scanner.nextLine();

                                            Production myProduction = findProductionByName(prodName);
                                            if (myProduction == null) {
                                                System.out.println("There is no production named " + prodName);
                                                break;
                                            }

                                            Rating rating = findRatingByUsername(myUser, myProduction);
                                            if (rating == null) {
                                                System.out.println("You have no review for this production");
                                                break;
                                            }

                                            myProduction.removeReview(myUser, rating);
                                        }
                                        break;

                                    case 8:
                                        isAuthenticated = false;
                                        continue loggingInLoop;

                                    default:
                                        System.out.println("Invalid command, please try again");
                                }
                            }


                        case Contributor:
                            while (true) {

                                System.out.println("Choose action:");
                                System.out.println("    1) View productions details");
                                System.out.println("    2) View actors details");
                                System.out.println("    3) View notifications");
                                System.out.println("    4) Search for actor/movie/series");
                                System.out.println("    5) Add/Delete actor/movie/series to/from favorites");
                                System.out.println("    6) Do/Undo a request");
                                System.out.println("    7) Add/Delete a production/actor from system");
                                System.out.println("    8) View & solve your requests");
                                System.out.println("    9) Update production/actors");
                                System.out.println("    10) Logout");

                                int action2 = scanningNextInt(scanner);
                                switch (action2) {
                                    case 1:
                                        System.out.println("Enter your genre.");
                                        System.out.println("If you don't want a specific genre, type null");
                                        String genreRead = scanner.next();
                                        System.out.println("Enter your minimum number of ratings.");
                                        System.out.println("If you don't want a minimum number of ratings for your search, type 0");
                                        int nrRatingsRead = scanningNextInt(scanner);
                                        filterProduction(genreRead, nrRatingsRead);
                                        break;

                                    case 2:
                                        System.out.println("Do you want to see the actors sorted by name?");
                                        System.out.println("1) yes");
                                        System.out.println("2) no");
                                        int isSorted = scanningNextInt(scanner);

                                        List<Actor> copyActor = new ArrayList<>(actors);

                                        if (isSorted == 1) {
                                            sortActors();
                                            System.out.println(actors.toString());
                                        } else {
                                            System.out.println(actors.toString());
                                        }
                                        actors = copyActor;
                                        break;

                                    case 3:
                                        myUser.displayNotifications();
                                        break;

                                    case 4:
                                        System.out.println("Enter the name of a movie/series/actor");
                                        String forSearch = scanner.nextLine();
                                        searchingAlgorithm(forSearch);
                                        break;

                                    case 5:
                                        System.out.println("How do you want to update your favorite list?");
                                        System.out.println("    1) Add a production to favourites");
                                        System.out.println("    2) Add an actor to favorites");
                                        System.out.println("    3) Remove a production from favorites");
                                        System.out.println("    4) Remove an actor from favorites");
                                        int nrAction = scanningNextInt(scanner);
                                        if (nrAction == 1) {
                                            System.out.println("Type your production to add to favorites");
                                            String toAdd = scanner.nextLine();
                                            Production productionToAdd = findProductionByName(toAdd);
                                            if (productionToAdd == null) {
                                                System.out.println("This production doesn't exist in the system");
                                                break;
                                            } else {
                                                myUser.favoriteProductions.add(productionToAdd.title);
                                                System.out.println("Production " + productionToAdd.title + " has been added");
                                                System.out.println("Here is your new favorite productions list:");
                                                System.out.println(myUser.favoriteProductions.toString());
                                            }
                                        } else if (nrAction == 2) {
                                            System.out.println("Type the name of the actor you want to add to favorites");
                                            String toAdd = scanner.nextLine();
                                            Actor actorToAdd = findActorByName(toAdd);
                                            if (actorToAdd == null) {
                                                System.out.println("This actor doesn't exist");
                                                break;
                                            } else {
                                                myUser.favoriteActors.add(actorToAdd.getName());
                                                System.out.println("The actor " + actorToAdd.getName() + " has been added to favorites");
                                                System.out.println("Here is your new favorite actors list");
                                                System.out.println(myUser.favoriteActors.toString());
                                            }
                                        } else if (nrAction == 3) {
                                            System.out.println("Type the name of the production you want to remove from favorites");
                                            String toRemove = scanner.nextLine();
                                            removeProductionFromFavorites(toRemove, myUser);
                                            System.out.println("Here is your production favorite list:");
                                            System.out.println(myUser.favoriteProductions.toString());
                                        } else if (nrAction == 4) {
                                            System.out.println("Type the name of the actor you want to remove from your favorite list");
                                            String toRemove = scanner.nextLine();
                                            removeActorFromFavorites(toRemove, myUser);
                                            System.out.println("Here is your actor favorite list");
                                            System.out.println(myUser.favoriteActors.toString());
                                        }
                                        break;

                                    case 6:
                                        System.out.println("How do you want to manage your requests?");
                                        System.out.println("    1) Create a new request");
                                        System.out.println("    2) Cancel a request");
                                        int nrAction6 = scanningNextInt(scanner);
                                        if (nrAction6 == 1) {
                                            System.out.println("What is the type of the request?");
                                            System.out.println("    1) DELETE_ACCOUNT");
                                            System.out.println("    2) ACTOR_ISSUE");
                                            System.out.println("    3) MOVIE_ISSUE");
                                            System.out.println("    4) OTHERS");
                                            int nrType = scanningNextInt(scanner);

                                            // DELETE_ACCOUNT
                                            if (nrType == 1) {
                                                System.out.println("Enter the description of your request");
                                                String descriptionRequest = scanner.nextLine();

                                                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                                String formattedDate = LocalDateTime.now().format(formatterDate);

                                                // Pentru a fi in concordanta cu formatul ISO_LOCAL_DATE_TIME
                                                formattedDate = formattedDate.replace(" ", "T");


                                                Request request = new Request(Request.RequestType.DELETE_ACCOUNT, formattedDate,
                                                        myUser.username, null, null, User.AccountType.Admin.toString(), descriptionRequest);

                                                Request.autoRequest(request);

                                                // ACTOR_ISSUE
                                            } else if (nrType == 2) {
                                                System.out.println("Enter the name of the Actor");
                                                String actorNameCase = scanner.nextLine();

                                                System.out.println("Enter the description for your request");
                                                String descriptionRequest2 = scanner.nextLine();

                                                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                                String formattedDate = LocalDateTime.now().format(formatterDate);

                                                // Pentru a fi in concordanta cu formatul ISO_LOCAL_DATE_TIME
                                                formattedDate = formattedDate.replace(" ", "T");

                                                Request request = new Request(Request.RequestType.ACTOR_ISSUE, formattedDate, myUser.username, actorNameCase,
                                                        null, null, descriptionRequest2);

                                                Request.autoRequest(request);

                                                // MOVIE_ISSUE
                                            } else if (nrType == 3) {
                                                System.out.println("Enter the name of the Movie:");
                                                String movieNameCase = scanner.nextLine();

                                                System.out.println("Enter the username you want to send the request");
                                                String toSend = scanner.nextLine();

                                                System.out.println("Enter the description  of your request:");
                                                String myDescription = scanner.nextLine();

                                                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                                String formattedDate = LocalDateTime.now().format(formatterDate);

                                                // Pentru a fi in concordanta cu formatul ISO_LOCAL_DATE_TIME
                                                formattedDate = formattedDate.replace(" ", "T");

                                                Request request = new Request(Request.RequestType.MOVIE_ISSUE, formattedDate, myUser.username,
                                                        null, movieNameCase, toSend, myDescription);

                                                Request.autoRequest(request);

                                                // OTHERS
                                            } else if (nrType == 4) {
                                                System.out.println("Enter the username you want to send the request");
                                                String toSend = scanner.nextLine();

                                                System.out.println("Enter the description  of your request:");
                                                String myDescription = scanner.nextLine();

                                                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                                String formattedDate = LocalDateTime.now().format(formatterDate);

                                                // Pentru a fi in concordanta cu formatul ISO_LOCAL_DATE_TIME
                                                formattedDate = formattedDate.replace(" ", "T");

                                                Request request = new Request(Request.RequestType.OTHERS, formattedDate, myUser.username,
                                                        null, null, toSend, myDescription);

                                                Request.autoRequest(request);
                                            }

                                            // Remove a request
                                        } else if (nrAction6 == 2) {
                                            System.out.println("Enter your type of request you'd like to remove");
                                            System.out.println("    1) DELETE_ACCOUNT");
                                            System.out.println("    2) ACTOR_ISSUE");
                                            System.out.println("    3) MOVIE_ISSUE");
                                            System.out.println("    4) OTHERS");
                                            int nrType = scanningNextInt(scanner);

                                            // DELETE_ACCOUNT
                                            if (nrType == 1) {
                                                Request myRequest = Request.findRequestByType(myUser, Request.RequestType.DELETE_ACCOUNT);
                                                if (myRequest == null) {
                                                    System.out.println("No request has been found");
                                                    break;
                                                }
                                                RequestsHolder.removeRequest(myRequest);
                                                requests.remove(myRequest);
                                                RequestsHolder.notifyUser(myUser.username, "Request " + myRequest + "has been removed");

                                                // ACTOR_ISSUE
                                            } else if (nrType == 2) {
                                                Request myRequest = Request.findRequestByType(myUser, Request.RequestType.ACTOR_ISSUE);
                                                if (myRequest == null) {
                                                    System.out.println("No request has been found");
                                                    break;
                                                }
                                                RequestsHolder.removeRequest(myRequest);
                                                requests.remove(myRequest);
                                                RequestsHolder.notifyUser(myUser.username, "Request " + myRequest + "has been removed");

                                                // MOVIE_ISSUE
                                            } else if (nrType == 3) {
                                                Request myRequest = Request.findRequestByType(myUser, Request.RequestType.MOVIE_ISSUE);
                                                if (myRequest == null) {
                                                    System.out.println("No request has been found");
                                                    break;
                                                }
                                                RequestsHolder.removeRequest(myRequest);
                                                requests.remove(myRequest);
                                                RequestsHolder.notifyUser(myUser.username, "Request " + myRequest + "has been removed");

                                                // OTHERS
                                            } else if (nrType == 4) {
                                                Request myRequest = Request.findRequestByType(myUser, Request.RequestType.OTHERS);
                                                if (myRequest == null) {
                                                    System.out.println("No request has been found");
                                                    break;
                                                }
                                                RequestsHolder.removeRequest(myRequest);
                                                requests.remove(myRequest);
                                                RequestsHolder.notifyUser(myUser.username, "Request " + myRequest + "has been removed");
                                            }
                                        }
                                        break;


                                    case 7:
                                        // Add/Delete a production/actor from system
                                        System.out.println("What do you want to do?");
                                        System.out.println("    1) Add a Production to system");
                                        System.out.println("    2) Remove a Production from system");
                                        System.out.println("    3) Add an Actor to system");
                                        System.out.println("    4) Remove an Actor from system");
                                        int noCase7 = scanningNextInt(scanner);

                                        if (noCase7 == 1) {
                                            System.out.println("Is your production a Movie or a Series?");
                                            System.out.println("    1) Movie");
                                            System.out.println("    2) Series");
                                            int isMovie = scanningNextInt(scanner);

                                            // If is Movie
                                            if (isMovie == 1) {

                                                // Managing title
                                                System.out.println("Enter the title");
                                                String movieTitle = scanner.nextLine();

                                                // Managing directors
                                                System.out.println("How many directors?");
                                                int nrDirectors = scanningNextInt(scanner);
                                                List<String> myDirectors = new ArrayList<>();
                                                for (int i = 1; i <= nrDirectors; i++) {
                                                    System.out.println("Enter the name of director number " + i);
                                                    String nameDirector = scanner.nextLine();
                                                    myDirectors.add(nameDirector);
                                                }

                                                // Managing actors
                                                System.out.println("How many actors?");
                                                int nrActors = scanningNextInt(scanner);
                                                List<String> myActors = new ArrayList<>();
                                                for (int i = 1; i <= nrActors; i++) {
                                                    System.out.println("Enter the name of actor number " + i);
                                                    String nameActor = scanner.nextLine();
                                                    myActors.add(nameActor);
                                                }

                                                // Managing genres
                                                System.out.println("How many genres");
                                                int nrGenres = scanningNextInt(scanner);
                                                List<Production.Genre> myGenres = new ArrayList<>();
                                                for (int i = 1; i <= nrGenres; i++) {
                                                    System.out.println("Enter the Genre number " + i);
                                                    String nameGenre = scanner.nextLine();
                                                    Production.Genre aGenre = new Production.Genre(nameGenre);
                                                    myGenres.add(aGenre);
                                                }

                                                // Managing Ratings
                                                System.out.println("How many ratings");
                                                int nrRatings = scanningNextInt(scanner);
                                                List<Rating> myRatings = new ArrayList<>();
                                                for (int i = 1; i <= nrRatings; i++) {
                                                    System.out.println("Enter username that added the review:");
                                                    String myUsername = scanner.nextLine();

                                                    System.out.println("Enter the mark he gave:");
                                                    Integer myRating = scanningNextInt(scanner);

                                                    System.out.println("Enter the comment he gave:");
                                                    String myComment = scanner.nextLine();

                                                    Rating ratingToAdd = new Rating(myUsername, myRating, myComment);
                                                    myRatings.add(ratingToAdd);
                                                }

                                                // Managing plot
                                                System.out.println("Enter the plot:");
                                                String myPlot = scanner.nextLine();

                                                // Managing duration
                                                System.out.println("Enter the duration:");
                                                String myDuration = scanner.nextLine();

                                                // Managing releaseYear
                                                System.out.println("Enter release year:");
                                                int myReleaseYear = scanningNextInt(scanner);

                                                Movie myMovie = new Movie(movieTitle, "Movie", myDirectors, myActors, myGenres, myRatings,
                                                        myPlot, myDuration, myReleaseYear);

                                                Staff.addProduction(myMovie);
                                                AddingInTheSystemStrategy addingInTheSystemStrategy = new AddingInTheSystemStrategy();
                                                int updatedExperience = addingInTheSystemStrategy.calculateExperience(myUser.experience);
                                                myUser.experience = updatedExperience;
                                                break;

                                                // Managing series case
                                            } else if (isMovie == 2) {

                                                // Managing title
                                                System.out.println("Enter the title");
                                                String seriesTitle = scanner.nextLine();

                                                // Managing directors
                                                System.out.println("How many directors?");
                                                int nrDirectors = scanningNextInt(scanner);
                                                List<String> myDirectors = new ArrayList<>();
                                                for (int i = 1; i <= nrDirectors; i++) {
                                                    System.out.println("Enter the name of director number " + i);
                                                    String nameDirector = scanner.nextLine();
                                                    myDirectors.add(nameDirector);
                                                }

                                                // Managing actors
                                                System.out.println("How many actors?");
                                                int nrActors = scanningNextInt(scanner);
                                                List<String> myActors = new ArrayList<>();
                                                for (int i = 1; i <= nrActors; i++) {
                                                    System.out.println("Enter the name of actor number " + i);
                                                    String nameActor = scanner.nextLine();
                                                    myActors.add(nameActor);
                                                }

                                                // Managing genres
                                                System.out.println("How many genres");
                                                int nrGenres = scanningNextInt(scanner);
                                                List<Production.Genre> myGenres = new ArrayList<>();
                                                for (int i = 1; i <= nrGenres; i++) {
                                                    System.out.println("Enter the Genre number " + i);
                                                    String nameGenre = scanner.nextLine();
                                                    Production.Genre aGenre = new Production.Genre(nameGenre);
                                                    myGenres.add(aGenre);
                                                }

                                                // Managing Ratings
                                                System.out.println("How many ratings");
                                                int nrRatings = scanningNextInt(scanner);
                                                List<Rating> myRatings = new ArrayList<>();
                                                for (int i = 1; i <= nrRatings; i++) {
                                                    System.out.println("Enter username that added the review:");
                                                    String myUsername = scanner.nextLine();

                                                    System.out.println("Enter the mark he gave:");
                                                    Integer myRating = scanningNextInt(scanner);

                                                    System.out.println("Enter the comment he gave:");
                                                    String myComment = scanner.nextLine();

                                                    Rating ratingToAdd = new Rating(myUsername, myRating, myComment);
                                                    myRatings.add(ratingToAdd);
                                                }

                                                // Managing plot
                                                System.out.println("Enter the plot:");
                                                String myPlot = scanner.nextLine();

                                                // Managing releaseYear
                                                System.out.println("Enter release year:");
                                                int myReleaseYear = scanningNextInt(scanner);

                                                // Managing numSeasons
                                                System.out.println("Enter the seasons number");
                                                int myNumSeasons = scanningNextInt(scanner);

                                                // Managing seasons
                                                Map<String, List<Episode>> mySeasons = new HashMap<>();
                                                for (int i = 1; i <= myNumSeasons; i++) {
                                                    System.out.println("Enter the details for Season number " + i);

                                                    List<Episode> myEpisodes = new ArrayList<>();
                                                    System.out.println("How many episodes does seasons " + i + " have?");
                                                    int numEpisodes = scanningNextInt(scanner);

                                                    for (int j = 1; j <= numEpisodes; j++) {
                                                        System.out.println("Enter the episode number " + j + " name:");
                                                        String myEpName = scanner.nextLine();
                                                        System.out.println("Enter the duration for your episode:");
                                                        String myDuration = scanner.nextLine();

                                                        Episode anEpisode = new Episode(myEpName, myDuration);
                                                        myEpisodes.add(anEpisode);
                                                    }
                                                    mySeasons.put("Season " + i, myEpisodes);
                                                }

                                                Series mySeries = new Series(seriesTitle, "Series", myDirectors, myActors, myGenres, myRatings,
                                                        myPlot, null, myReleaseYear, myNumSeasons, mySeasons);

                                                //productions.add(mySeries);
                                                Staff.addProduction(mySeries);
                                                System.out.println("Series " + seriesTitle + " has been added in the system");
                                                AddingInTheSystemStrategy addingInTheSystemStrategy = new AddingInTheSystemStrategy();
                                                int updatedExperience = addingInTheSystemStrategy.calculateExperience(myUser.experience);
                                                myUser.experience = updatedExperience;
                                                break;
                                            }

                                        } else if (noCase7 == 2) {
                                            // Removing a production from system
                                            System.out.println("Enter the name of production you want to remove");
                                            String prodToRemove = scanner.nextLine();

                                            Production production = findProductionByName(prodToRemove);
                                            if (production == null) {
                                                System.out.println("Nu s-a gasit nicio productie numita: " + prodToRemove);
                                                break;
                                            }

                                            Staff.removeProduction(production);
                                            System.out.println("Production named " + prodToRemove + " has been removed");
                                            break;

                                        } else if (noCase7 == 3) {
                                            // Adding an actor to system
                                            System.out.println("Enter the name of the actor you want to add");
                                            String myActorName = scanner.nextLine();

                                            System.out.println("How many performances he has?");
                                            int myNumPerformance = scanningNextInt(scanner);

                                            List<Actor.Performance> myPerformances = new ArrayList<>();

                                            for (int i = 1; i <= myNumPerformance; i++) {
                                                System.out.println("Enter the title of performance number " + i);
                                                String myPerfTitle = scanner.nextLine();

                                                System.out.println("Enter the type of the performance: (Movie/Series)");
                                                String myPerfType = scanner.nextLine();

                                                Actor.Performance aPerformance = new Actor.Performance(myPerfTitle, myPerfType);
                                                myPerformances.add(aPerformance);
                                            }

                                            System.out.println("Enter the biography of the actor:");
                                            String myBiography = scanner.nextLine();

                                            Actor myActor = new Actor(myActorName, myPerformances, myBiography);
                                            Staff.addActor(myActor);
                                            System.out.println("The actor named " + myActorName + " has been added in system");

                                            // Adding the experience for adding the actor in the system
                                            AddingInTheSystemStrategy addingInTheSystemStrategy = new AddingInTheSystemStrategy();
                                            int updatedExperience = addingInTheSystemStrategy.calculateExperience(myUser.experience);
                                            myUser.experience = updatedExperience;
                                            break;

                                        } else if (noCase7 == 4) {
                                            // Removing an actor
                                            System.out.println("Enter the name of the actor you want to delete");
                                            String myActorName = scanner.nextLine();

                                            Actor myActor = findActorByName(myActorName);
                                            if (myActor == null) {
                                                System.out.println("There is no actor named " + myActorName);
                                                break;
                                            }

                                            Staff.removeActor(myActor);
                                            System.out.println("Actor named " + myActorName + " has been removed from system");
                                            break;
                                        }

                                    case 8:
                                        // View & solve your requests
                                        System.out.println("Here are your requests:");
                                        int isFound = 0;
                                        Iterator<Request> iterator = requests.iterator();
                                        while (iterator.hasNext()) {
                                            Request request = iterator.next();
                                            if (request.getTo().equals(myUser.username)) {
                                                isFound = 1;
                                                System.out.println(request.toString());
                                                System.out.println("\nDid you solve this request?");
                                                System.out.println("    1) Yes");
                                                System.out.println("    2) Solve it now, or later");
                                                System.out.println("    3) Reject this request");
                                                int isSolved = scanningNextInt(scanner);

                                                if (isSolved == 1) {
                                                    if (request.getType().equals(Request.RequestType.ACTOR_ISSUE) || request.getType().equals(Request.RequestType.MOVIE_ISSUE)) {
                                                        // Adding the experience to whom that added request
                                                        RequestStrategy requestStrategy = new RequestStrategy();
                                                        int updatedExperience = requestStrategy.calculateExperience(getUserByUsername(request.getUsername()).experience);
                                                        getUserByUsername(request.getUsername()).experience = updatedExperience;
                                                    }

                                                    // If it is solved we delete the request, notifing the user that his request has been solved
                                                    request.resolvedRequest(getUserByUsername(request.getUsername()));
                                                    iterator.remove();
                                                    System.out.println("Sending you to next request");
                                                } else if (isSolved == 2) {
                                                    System.out.println("Sending you to main menu to solve the request ...");
                                                    break;
                                                } else if (isSolved == 3) {
                                                    System.out.println("Rejecting and removing this request ...");
                                                    request.rejectRequest(getUserByUsername(request.getUsername()));
                                                    iterator.remove();
                                                    System.out.println("The request has been removed, user " + request.getUsername() + " has been notified.");
                                                    System.out.println("Sending you to the next request:\n");
                                                }
                                            }
                                        }
                                        if (isFound == 0) {
                                            System.out.println("You have no requests");
                                            break;
                                        }
                                        System.out.println("Now you have no requests");
                                        break;

                                    case 9:
                                        // Update production/actors
                                        System.out.println("What would you like to update?");
                                        System.out.println("    1) Productions");
                                        System.out.println("    2) Actors");
                                        int whatUpdate = scanningNextInt(scanner);

                                        if (whatUpdate == 1) {
                                            System.out.println("Type the title of the production you want to update");
                                            String myTitle = scanner.nextLine();
                                            Production myProduction = findProductionByName(myTitle);
                                            if (myProduction == null) {
                                                System.out.println("There is no production named " + myTitle);
                                                break;
                                            }

                                            if (myProduction.type.equals("Movie")) {
                                                System.out.println("What do you want to change about your movie?");
                                                System.out.println("    1) Title");
                                                System.out.println("    2) Type (Not recommended)");
                                                System.out.println("    3) Directors");
                                                System.out.println("    4) Actors");
                                                System.out.println("    5) Genres");
                                                System.out.println("    6) Ratings");
                                                System.out.println("    7) Plot");
                                                System.out.println("    8) Average rating (Not recommended)");
                                                System.out.println("    9) Duration");
                                                System.out.println("    10) Release year");
                                                int updated = scanningNextInt(scanner);

                                                if (updated == 1) {
                                                    System.out.println("Enter The new Title");
                                                    String newTitle = scanner.nextLine();
                                                    System.out.println("Old title: " + myProduction.title);
                                                    myProduction.title = newTitle;
                                                    System.out.println("New title: " + myProduction.title);
                                                    break;

                                                } else if (updated == 2) {
                                                    System.out.println("Enter the new type. Attention, this could be dangerous!");
                                                    String newType = scanner.nextLine();
                                                    System.out.println("Old Type: " + myProduction.type);
                                                    myProduction.type = newType;
                                                    System.out.println("New Type: " + myProduction.type);
                                                    break;

                                                } else if (updated == 3) {
                                                    System.out.println("How many directors are in the end?");
                                                    int nrDirectors = scanningNextInt(scanner);

                                                    List<String> newDirectors = new ArrayList<>();
                                                    for (int i = 1; i <= nrDirectors; i++) {
                                                        System.out.println("Enter the director number " + i);
                                                        String newDirector = scanner.nextLine();
                                                        newDirectors.add(newDirector);
                                                    }

                                                    System.out.println("Old directors:\n" + myProduction.directors.toString());
                                                    myProduction.directors = newDirectors;
                                                    System.out.println("New directors:\n" + myProduction.directors.toString());
                                                    break;

                                                } else if (updated == 4) {
                                                    System.out.println("How many actors are in the end?");
                                                    int nrActors = scanningNextInt(scanner);

                                                    List<String> newActors = new ArrayList<>();
                                                    for (int i = 1; i <= nrActors; i++) {
                                                        System.out.println("Enter the actor number " + i);
                                                        String newActor = scanner.nextLine();
                                                        newActors.add(newActor);
                                                    }

                                                    System.out.println("Old actors:\n" + myProduction.actors.toString());
                                                    myProduction.actors = newActors;
                                                    System.out.println("New actors:\n" + myProduction.actors.toString());
                                                    break;

                                                } else if (updated == 5) {
                                                    System.out.println("How many genres are in the end?");
                                                    int nrGenres = scanningNextInt(scanner);

                                                    List<Production.Genre> newGenres = new ArrayList<>();
                                                    for (int i = 1; i <= nrGenres; i++) {
                                                        System.out.println("Enter Genre number " + i);
                                                        String newGenre = scanner.nextLine();
                                                        Production.Genre aGenre = new Production.Genre(newGenre);
                                                        newGenres.add(aGenre);
                                                    }

                                                    System.out.println("Old genres:\n" + myProduction.genres);
                                                    myProduction.genres = newGenres;
                                                    System.out.println("New genres:\n" + myProduction.genres);
                                                    break;

                                                } else if (updated == 6) {
                                                    System.out.println("How many ratings are in the end?");
                                                    int nrRatings = scanningNextInt(scanner);

                                                    List<Rating> newRatings = new ArrayList<>();
                                                    for (int i = 1; i <= nrRatings; i++) {
                                                        System.out.println("Rating number " + i);
                                                        System.out.println("Enter username:");
                                                        String newUsername = scanner.nextLine();

                                                        System.out.println("Enter rating: (int)");
                                                        int newRate = scanningNextInt(scanner);

                                                        System.out.println("Enter comment:");
                                                        String newComment = scanner.nextLine();

                                                        Rating newRating = new Rating(newUsername, newRate, newComment);
                                                        newRatings.add(newRating);
                                                    }
                                                    System.out.println("Old rating:\n" + myProduction.ratings.toString());
                                                    myProduction.ratings = newRatings;
                                                    System.out.println("New rating:\n" + myProduction.ratings.toString());
                                                    break;

                                                } else if (updated == 7) {
                                                    System.out.println("Enter new plot:");
                                                    String newPlot = scanner.nextLine();

                                                    System.out.println("Old Plot:\n" + myProduction.plot);
                                                    myProduction.plot = newPlot;
                                                    System.out.println("New plot:\n" + myProduction.plot);
                                                    break;

                                                } else if (updated == 8) {
                                                    System.out.println("Enter new average rating. Attention, this could be dangerous: (double)");
                                                    Double newAverageRating = scanner.nextDouble();
                                                    scanner.nextLine();

                                                    System.out.println("Old average rating:\n" + myProduction.averageRating);
                                                    myProduction.averageRating = newAverageRating;
                                                    System.out.println("New average rating:\n" + myProduction.averageRating);
                                                    break;

                                                } else if (updated == 9) {
                                                    System.out.println("Enter the new duration:");
                                                    String newDuration = scanner.nextLine();

                                                    System.out.println("Old duration: " + myProduction.duration);
                                                    myProduction.duration = newDuration;
                                                    System.out.println("New duration: " + myProduction.duration);
                                                    break;

                                                } else if (updated == 10) {
                                                    System.out.println("Enter the new release year:");
                                                    Integer newReleaseYear = scanningNextInt(scanner);

                                                    System.out.println("Old release year: " + myProduction.releaseYear);
                                                    myProduction.releaseYear = newReleaseYear;
                                                    System.out.println("New release year: " + myProduction.releaseYear);
                                                    break;
                                                }
                                            } else if (myProduction.type.equals("Series") && myProduction instanceof Series){
                                                // This means our production is a series
                                                System.out.println("What do you want to change about this series?");
                                                System.out.println("    1) Title");
                                                System.out.println("    2) Type (Not recommended)");
                                                System.out.println("    3) Directors");
                                                System.out.println("    4) Actors");
                                                System.out.println("    5) Genres");
                                                System.out.println("    6) Ratings");
                                                System.out.println("    7) Plot");
                                                System.out.println("    8) Average rating (Not recommended)");
                                                System.out.println("    9) Release year");
                                                System.out.println("    10) Seasons & seasons number");
                                                int updated = scanningNextInt(scanner);
                                                if (updated == 1) {
                                                    System.out.println("Enter The new Title");
                                                    String newTitle = scanner.nextLine();
                                                    System.out.println("Old title: " + myProduction.title);
                                                    myProduction.title = newTitle;
                                                    System.out.println("New title: " + myProduction.title);
                                                    break;
                                                    
                                                } else if (updated == 2) {
                                                    System.out.println("Enter the new type. Attention, this could be dangerous!");
                                                    String newType = scanner.nextLine();
                                                    System.out.println("Old Type: " + myProduction.type);
                                                    myProduction.type = newType;
                                                    System.out.println("New Type: " + myProduction.type);
                                                    break;
                                                    
                                                } else if (updated == 3) {
                                                    System.out.println("How many directors are in the end?");
                                                    int nrDirectors = scanningNextInt(scanner);
                                                    scanner.nextLine();

                                                    List<String> newDirectors = new ArrayList<>();
                                                    for (int i = 1; i <= nrDirectors; i++) {
                                                        System.out.println("Enter the director number " + i);
                                                        String newDirector = scanner.nextLine();
                                                        newDirectors.add(newDirector);
                                                    }

                                                    System.out.println("Old directors:\n" + myProduction.directors.toString());
                                                    myProduction.directors = newDirectors;
                                                    System.out.println("New directors:\n" + myProduction.directors.toString());
                                                    break;
                                                } else if (updated == 4) {
                                                    System.out.println("How many actors are in the end?");
                                                    int nrActors = scanningNextInt(scanner);
                                                    scanner.nextLine();

                                                    List<String> newActors = new ArrayList<>();
                                                    for (int i = 1; i <= nrActors; i++) {
                                                        System.out.println("Enter the actor number " + i);
                                                        String newActor = scanner.nextLine();
                                                        newActors.add(newActor);
                                                    }

                                                    System.out.println("Old actors:\n" + myProduction.actors.toString());
                                                    myProduction.actors = newActors;
                                                    System.out.println("New actors:\n" + myProduction.actors.toString());
                                                    break;
                                                    
                                                } else if (updated == 5) {
                                                    System.out.println("How many genres are in the end?");
                                                    int nrGenres = scanningNextInt(scanner);
                                                    scanner.nextLine();

                                                    List<Production.Genre> newGenres = new ArrayList<>();
                                                    for (int i = 1; i <= nrGenres; i++) {
                                                        System.out.println("Enter Genre number " + i);
                                                        String newGenre = scanner.nextLine();
                                                        Production.Genre aGenre = new Production.Genre(newGenre);
                                                        newGenres.add(aGenre);
                                                    }

                                                    System.out.println("Old genres:\n" + myProduction.genres);
                                                    myProduction.genres = newGenres;
                                                    System.out.println("New genres:\n" + myProduction.genres);
                                                    break;
                                                    
                                                } else if (updated == 6) {
                                                    System.out.println("How many ratings are in the end?");
                                                    int nrRatings = scanningNextInt(scanner);
                                                    scanner.nextLine();

                                                    List<Rating> newRatings = new ArrayList<>();
                                                    for (int i = 1; i <= nrRatings; i++) {
                                                        System.out.println("Rating number " + i);
                                                        System.out.println("Enter username:");
                                                        String newUsername = scanner.nextLine();

                                                        System.out.println("Enter rating: (int)");
                                                        int newRate = scanningNextInt(scanner);
                                                        scanner.nextLine();

                                                        System.out.println("Enter comment:");
                                                        String newComment = scanner.nextLine();

                                                        Rating newRating = new Rating(newUsername, newRate, newComment);
                                                        newRatings.add(newRating);
                                                    }
                                                    System.out.println("Old rating:\n" + myProduction.ratings.toString());
                                                    myProduction.ratings = newRatings;
                                                    System.out.println("New rating:\n" + myProduction.ratings.toString());
                                                    break;
                                                    
                                                } else if (updated == 7) {
                                                    System.out.println("Enter new plot:");
                                                    String newPlot = scanner.nextLine();

                                                    System.out.println("Old Plot:\n" + myProduction.plot);
                                                    myProduction.plot = newPlot;
                                                    System.out.println("New plot:\n" + myProduction.plot);
                                                    break;

                                                } else if (updated == 8) {
                                                    System.out.println("Enter new average rating. Atention, this could be dangerous: (double)");
                                                    Double newAverageRating = scanner.nextDouble();
                                                    scanner.nextLine();

                                                    System.out.println("Old average rating:\n" + myProduction.averageRating);
                                                    myProduction.averageRating = newAverageRating;
                                                    System.out.println("New average rating:\n" + myProduction.averageRating);
                                                    break;

                                                } else if (updated == 9) {
                                                    System.out.println("Enter the new release year:");
                                                    Integer newReleaseYear = scanningNextInt(scanner);

                                                    System.out.println("Old release year: " + myProduction.releaseYear);
                                                    myProduction.releaseYear = newReleaseYear;
                                                    System.out.println("New release year: " + myProduction.releaseYear);
                                                    break;

                                                } else if (updated == 10) {
                                                    System.out.println("Enter the new number of seasons:");
                                                    int newNumSeasons = scanningNextInt(scanner);

                                                    Map<String, List<Episode>> newSeasons = new HashMap<>();
                                                    for (int i = 1; i <= newNumSeasons; i++) {
                                                        System.out.println("Enter the details for season " + i);

                                                        List<Episode> newEpisodes = new ArrayList<>();
                                                        System.out.println("How many episodes does season " + i + " has?");
                                                        int nrNewEpisodes = scanningNextInt(scanner);

                                                        for (int j = 1; j <= nrNewEpisodes; j++) {
                                                            System.out.println("Enter the episode number " + j + " name");
                                                            String newEpName = scanner.nextLine();

                                                            System.out.println("Enter the duration of the episode number " + j);
                                                            String newDuration = scanner.nextLine();

                                                            Episode newEpisode = new Episode(newEpName, newDuration);
                                                            newEpisodes.add(newEpisode);
                                                        }
                                                        newSeasons.put("Season " + i, newEpisodes);
                                                    }

                                                    System.out.println("Old number of seasons:\n" + ((Series) myProduction).getNumSeasons().toString());
                                                    ((Series) myProduction).setNumSeasons(newNumSeasons);
                                                    System.out.println("New number of seasons:\n" + ((Series) myProduction).getNumSeasons().toString());

                                                    System.out.println("Old seasons:\n" + ((Series) myProduction).getSeasons().toString());
                                                    ((Series) myProduction).setSeasons(newSeasons);
                                                    System.out.println("New seasons:\n" + ((Series) myProduction).getSeasons().toString());
                                                    System.out.println();
                                                    break;
                                                }
                                            }
                                        } else if (whatUpdate == 2) {
                                            System.out.println("Type the name of the Actor:");
                                            String updatedActorName = scanner.nextLine();

                                            Actor newActor = findActorByName(updatedActorName);
                                            if (updatedActorName == null) {
                                                System.out.println("There is no actor named " + updatedActorName);
                                                break;
                                            }

                                            System.out.println("What do you want to change about your actor?");
                                            System.out.println("    1) Name");
                                            System.out.println("    2) Performances");
                                            System.out.println("    3) Biography");
                                            int actorCase = scanningNextInt(scanner);

                                            if (actorCase == 1) {
                                                System.out.println("Enter the new name for your actor");
                                                String newName = scanner.nextLine();

                                                System.out.println("Old name: " + newActor.getName());
                                                newActor.setName(newName);
                                                System.out.println("New name: " + newActor.getName());
                                                break;

                                            } else if (actorCase == 2) {
                                                System.out.println("Enter number of performances");
                                                int newNumPerformances = scanningNextInt(scanner);

                                                List<Actor.Performance> newPerformances = new ArrayList<>();
                                                for (int i = 1; i <= newNumPerformances; i++) {
                                                    System.out.println("Enter title for performance number " + i);
                                                    String newPerfTitle = scanner.nextLine();

                                                    System.out.println("Enter type of performance number " + i + ". (Series/Movie)");
                                                    String newPerfType = scanner.nextLine();

                                                    Actor.Performance aPerformance = new Actor.Performance(newPerfTitle, newPerfType);
                                                    newPerformances.add(aPerformance);
                                                }

                                                System.out.println("Old performances:\n" + newActor.getPerformances().toString());
                                                newActor.setPerformances(newPerformances);
                                                System.out.println("New performances:\n" + newActor.getPerformances().toString());
                                                break;

                                            } else if (actorCase == 3) {
                                                System.out.println("Enter the new biography");
                                                String newBiography = scanner.nextLine();

                                                System.out.println("Old biography: " + newActor.getBiography());
                                                newActor.setBiography(newBiography);
                                                System.out.println("New biography: " + newActor.getBiography());
                                                break;
                                            }
                                        }

                                    case 10:
                                        isAuthenticated = false;
                                        continue loggingInLoop;
                                }

                            }


                        case Admin:
                            while (true) {
                                System.out.println("Choose action:");
                                System.out.println("    1) View productions details");
                                System.out.println("    2) View actors details");
                                System.out.println("    3) View notifications");
                                System.out.println("    4) Search for actor/movie/series");
                                System.out.println("    5) Add/Delete actor/movie/series to/from favorites");
                                System.out.println("    6) Add/Delete a production/actor from system");
                                System.out.println("    7) View & solve your requests");
                                System.out.println("    8) Update production/actors");
                                System.out.println("    9) Delete/Add a user from system");
                                System.out.println("    10) Logout");

                                int action3 = scanningNextInt(scanner);
                                switch (action3) {
                                    case 1:
                                        System.out.println("Enter your genre.");
                                        System.out.println("If you don't want a specific genre, type null");
                                        String genreRead = scanner.next();
                                        System.out.println("Enter your minimum number of ratings.");
                                        System.out.println("If you don't want a minimum number of ratings for your search, type 0");
                                        int nrRatingsRead = scanningNextInt(scanner);
                                        filterProduction(genreRead, nrRatingsRead);
                                        break;

                                    case 2:
                                        System.out.println("Do you want to see the actors sorted by name?");
                                        System.out.println("1) yes");
                                        System.out.println("2) no");
                                        int isSorted = scanningNextInt(scanner);

                                        List<Actor> copyActor = new ArrayList<>(actors);

                                        if (isSorted == 1) {
                                            sortActors();
                                            System.out.println(actors.toString());
                                        } else {
                                            System.out.println(actors.toString());
                                        }
                                        actors = copyActor;
                                        break;

                                    case 3:
                                        myUser.displayNotifications();
                                        break;

                                    case 4:
                                        System.out.println("Enter the name of a movie/series/actor");
                                        String forSearch = scanner.nextLine();
                                        searchingAlgorithm(forSearch);
                                        break;

                                    case 5:
                                        System.out.println("How do you want to update your favorite list?");
                                        System.out.println("    1) Add a production to favourites");
                                        System.out.println("    2) Add an actor to favorites");
                                        System.out.println("    3) Remove a production from favorites");
                                        System.out.println("    4) Remove an actor from favorites");
                                        int nrAction = scanningNextInt(scanner);
                                        if (nrAction == 1) {
                                            System.out.println("Type your production to add to favorites");
                                            String toAdd = scanner.nextLine();
                                            Production productionToAdd = findProductionByName(toAdd);
                                            if (productionToAdd == null) {
                                                System.out.println("This production doesn't exist in the system");
                                                break;
                                            } else {
                                                myUser.favoriteProductions.add(productionToAdd.title);
                                                System.out.println("Production " + productionToAdd.title + " has been added");
                                                System.out.println("Here is your new favorite productions list:");
                                                System.out.println(myUser.favoriteProductions.toString());
                                            }
                                        } else if (nrAction == 2) {
                                            System.out.println("Type the name of the actor you want to add to favorites");
                                            String toAdd = scanner.nextLine();
                                            Actor actorToAdd = findActorByName(toAdd);
                                            if (actorToAdd == null) {
                                                System.out.println("This actor doesn't exist");
                                                break;
                                            } else {
                                                myUser.favoriteActors.add(actorToAdd.getName());
                                                System.out.println("The actor " + actorToAdd.getName() + " has been added to favorites");
                                                System.out.println("Here is your new favorite actors list");
                                                System.out.println(myUser.favoriteActors.toString());
                                            }
                                        } else if (nrAction == 3) {
                                            System.out.println("Type the name of the production you want to remove from favorites");
                                            String toRemove = scanner.nextLine();
                                            removeProductionFromFavorites(toRemove, myUser);
                                            System.out.println("Here is your production favorite list:");
                                            System.out.println(myUser.favoriteProductions.toString());
                                        } else if (nrAction == 4) {
                                            System.out.println("Type the name of the actor you want to remove from your favorite list");
                                            String toRemove = scanner.nextLine();
                                            removeActorFromFavorites(toRemove, myUser);
                                            System.out.println("Here is your actor favorite list");
                                            System.out.println(myUser.favoriteActors.toString());
                                        }
                                        break;

                                    case 6:
                                        // Add/Delete a production/actor from system
                                        System.out.println("What do you want to do?");
                                        System.out.println("    1) Add a Production to system");
                                        System.out.println("    2) Remove a Production from system");
                                        System.out.println("    3) Add an Actor to system");
                                        System.out.println("    4) Remove an Actor from system");
                                        int noCase7 = scanningNextInt(scanner);

                                        if (noCase7 == 1) {
                                            System.out.println("Is your production a Movie or a Series?");
                                            System.out.println("    1) Movie");
                                            System.out.println("    2) Series");
                                            int isMovie = scanningNextInt(scanner);

                                            // If is Movie
                                            if (isMovie == 1) {

                                                // Managing title
                                                System.out.println("Enter the title");
                                                String movieTitle = scanner.nextLine();

                                                // Managing directors
                                                System.out.println("How many directors?");
                                                int nrDirectors = scanningNextInt(scanner);
                                                List<String> myDirectors = new ArrayList<>();
                                                for (int i = 1; i <= nrDirectors; i++) {
                                                    System.out.println("Enter the name of director number " + i);
                                                    String nameDirector = scanner.nextLine();
                                                    myDirectors.add(nameDirector);
                                                }

                                                // Managing actors
                                                System.out.println("How many actors?");
                                                int nrActors = scanningNextInt(scanner);
                                                List<String> myActors = new ArrayList<>();
                                                for (int i = 1; i <= nrActors; i++) {
                                                    System.out.println("Enter the name of actor number " + i);
                                                    String nameActor = scanner.nextLine();
                                                    myActors.add(nameActor);
                                                }

                                                // Managing genres
                                                System.out.println("How many genres");
                                                int nrGenres = scanningNextInt(scanner);
                                                List<Production.Genre> myGenres = new ArrayList<>();
                                                for (int i = 1; i <= nrGenres; i++) {
                                                    System.out.println("Enter the Genre number " + i);
                                                    String nameGenre = scanner.nextLine();
                                                    Production.Genre aGenre = new Production.Genre(nameGenre);
                                                    myGenres.add(aGenre);
                                                }

                                                // Managing Ratings
                                                System.out.println("How many ratings");
                                                int nrRatings = scanningNextInt(scanner);
                                                List<Rating> myRatings = new ArrayList<>();
                                                for (int i = 1; i <= nrRatings; i++) {
                                                    System.out.println("Enter username that added the review:");
                                                    String myUsername = scanner.nextLine();

                                                    System.out.println("Enter the mark he gave:");
                                                    Integer myRating = scanningNextInt(scanner);

                                                    System.out.println("Enter the comment he gave:");
                                                    String myComment = scanner.nextLine();

                                                    Rating ratingToAdd = new Rating(myUsername, myRating, myComment);
                                                    myRatings.add(ratingToAdd);
                                                }

                                                // Managing plot
                                                System.out.println("Enter the plot:");
                                                String myPlot = scanner.nextLine();

                                                // Managing duration
                                                System.out.println("Enter the duration:");
                                                String myDuration = scanner.nextLine();

                                                // Managing releaseYear
                                                System.out.println("Enter release year:");
                                                int myReleaseYear = scanningNextInt(scanner);

                                                Movie myMovie = new Movie(movieTitle, "Movie", myDirectors, myActors, myGenres, myRatings,
                                                        myPlot, myDuration, myReleaseYear);

                                                Staff.addProduction(myMovie);
                                                break;

                                                // Managing series case
                                            } else if (isMovie == 2) {

                                                // Managing title
                                                System.out.println("Enter the title");
                                                String seriesTitle = scanner.nextLine();

                                                // Managing directors
                                                System.out.println("How many directors?");
                                                int nrDirectors = scanningNextInt(scanner);
                                                List<String> myDirectors = new ArrayList<>();
                                                for (int i = 1; i <= nrDirectors; i++) {
                                                    System.out.println("Enter the name of director number " + i);
                                                    String nameDirector = scanner.nextLine();
                                                    myDirectors.add(nameDirector);
                                                }

                                                // Managing actors
                                                System.out.println("How many actors?");
                                                int nrActors = scanningNextInt(scanner);
                                                List<String> myActors = new ArrayList<>();
                                                for (int i = 1; i <= nrActors; i++) {
                                                    System.out.println("Enter the name of actor number " + i);
                                                    String nameActor = scanner.nextLine();
                                                    myActors.add(nameActor);
                                                }

                                                // Managing genres
                                                System.out.println("How many genres");
                                                int nrGenres = scanningNextInt(scanner);
                                                List<Production.Genre> myGenres = new ArrayList<>();
                                                for (int i = 1; i <= nrGenres; i++) {
                                                    System.out.println("Enter the Genre number " + i);
                                                    String nameGenre = scanner.nextLine();
                                                    Production.Genre aGenre = new Production.Genre(nameGenre);
                                                    myGenres.add(aGenre);
                                                }

                                                // Managing Ratings
                                                System.out.println("How many ratings");
                                                int nrRatings = scanningNextInt(scanner);
                                                List<Rating> myRatings = new ArrayList<>();
                                                for (int i = 1; i <= nrRatings; i++) {
                                                    System.out.println("Enter username that added the review:");
                                                    String myUsername = scanner.nextLine();

                                                    System.out.println("Enter the mark he gave:");
                                                    Integer myRating = scanningNextInt(scanner);

                                                    System.out.println("Enter the comment he gave:");
                                                    String myComment = scanner.nextLine();

                                                    Rating ratingToAdd = new Rating(myUsername, myRating, myComment);
                                                    myRatings.add(ratingToAdd);
                                                }

                                                // Managing plot
                                                System.out.println("Enter the plot:");
                                                String myPlot = scanner.nextLine();

                                                // Managing releaseYear
                                                System.out.println("Enter release year:");
                                                int myReleaseYear = scanningNextInt(scanner);

                                                // Managing numSeasons
                                                System.out.println("Enter the seasons number");
                                                int myNumSeasons = scanningNextInt(scanner);

                                                // Managing seasons
                                                Map<String, List<Episode>> mySeasons = new HashMap<>();
                                                for (int i = 1; i <= myNumSeasons; i++) {
                                                    System.out.println("Enter the details for Season number " + i);

                                                    List<Episode> myEpisodes = new ArrayList<>();
                                                    System.out.println("How many episodes does seasons " + i + " have?");
                                                    int numEpisodes = scanningNextInt(scanner);

                                                    for (int j = 1; j <= numEpisodes; j++) {
                                                        System.out.println("Enter the episode number " + j + " name:");
                                                        String myEpName = scanner.nextLine();
                                                        System.out.println("Enter the duration for your episode:");
                                                        String myDuration = scanner.nextLine();

                                                        Episode anEpisode = new Episode(myEpName, myDuration);
                                                        myEpisodes.add(anEpisode);
                                                    }
                                                    mySeasons.put("Season " + i, myEpisodes);
                                                }

                                                Series mySeries = new Series(seriesTitle, "Series", myDirectors, myActors, myGenres, myRatings,
                                                        myPlot, null, myReleaseYear, myNumSeasons, mySeasons);

                                                Staff.addProduction(mySeries);
                                                System.out.println("Series " + seriesTitle + " has been added in the system");
                                                break;
                                            }

                                        } else if (noCase7 == 2) {
                                            // Removing a production from system
                                            System.out.println("Enter the name of production you want to remove");
                                            String prodToRemove = scanner.nextLine();

                                            Production production = findProductionByName(prodToRemove);
                                            if (production == null) {
                                                System.out.println("There is no production named: " + prodToRemove);
                                                break;
                                            }

                                            Staff.removeProduction(production);
                                            System.out.println("Production named " + prodToRemove + " has been removed");
                                            break;

                                        } else if (noCase7 == 3) {
                                            // Adding an actor to system
                                            System.out.println("Enter the name of the actor you want to add");
                                            String myActorName = scanner.nextLine();

                                            System.out.println("How many performances he has?");
                                            int myNumPerformance = scanningNextInt(scanner);

                                            List<Actor.Performance> myPerformances = new ArrayList<>();

                                            for (int i = 1; i <= myNumPerformance; i++) {
                                                System.out.println("Enter the title of performance number " + i);
                                                String myPerfTitle = scanner.nextLine();

                                                System.out.println("Enter the type of the performance: (Movie/Series)");
                                                String myPerfType = scanner.nextLine();

                                                Actor.Performance aPerformance = new Actor.Performance(myPerfTitle, myPerfType);
                                                myPerformances.add(aPerformance);
                                            }

                                            System.out.println("Enter the biography of the actor:");
                                            String myBiography = scanner.nextLine();

                                            Actor myActor = new Actor(myActorName, myPerformances, myBiography);
                                            Staff.addActor(myActor);
                                            System.out.println("The actor named " + myActorName + " has been added in system");
                                            break;

                                        } else if (noCase7 == 4) {
                                            // Removing an actor
                                            System.out.println("Enter the name of the actor you want to delete");
                                            String myActorName = scanner.nextLine();

                                            Actor myActor = findActorByName(myActorName);
                                            if (myActor == null) {
                                                System.out.println("There is no actor named " + myActorName);
                                                break;
                                            }

                                            Staff.removeActor(myActor);
                                            System.out.println("Actor named " + myActorName + " has been removed from system");
                                            break;
                                        }

                                    case 7:
                                        System.out.println("What do you want to manage?");
                                        System.out.println("    1) Personal requests");
                                        System.out.println("    2) RequestHolder");
                                        int isReqHold = scanningNextInt(scanner);
                                        if (isReqHold == 1) {
                                            System.out.println("Here are your requests:");
                                            int isFound = 0;
                                            Iterator<Request> iterator = requests.iterator();
                                            while (iterator.hasNext()) {
                                                Request request = iterator.next();
                                                if (request.getTo().equals(myUser.username)) {
                                                    isFound = 1;
                                                    System.out.println(request.toString());
                                                    System.out.println("\nDid you solve this request?");
                                                    System.out.println("    1) Yes");
                                                    System.out.println("    2) Solve it now, or later");
                                                    System.out.println("    3) Reject this request");
                                                    int isSolved = scanningNextInt(scanner);

                                                    if (isSolved == 1) {
                                                        if (request.getType().equals("ACTOR_ISSUE") || request.getType().equals("MOVIE_ISSUE")) {
                                                            // Adding the experience to whom that added request
                                                            RequestStrategy requestStrategy = new RequestStrategy();
                                                            int updatedExperience = requestStrategy.calculateExperience(getUserByUsername(request.getUsername()).experience);
                                                            getUserByUsername(request.getUsername()).experience = updatedExperience;
                                                        }

                                                        // Removing the request because we solved it
                                                        request.resolvedRequest(getUserByUsername(request.getUsername()));
                                                        iterator.remove();
                                                        System.out.println("Sending you to next request:\n");
                                                    } else if (isSolved == 2) {
                                                        System.out.println("Sending you to main menu to solve the request ...");
                                                        break;
                                                    } else if (isSolved == 3) {
                                                        System.out.println("Rejecting and removing this request ...");
                                                        request.rejectRequest(getUserByUsername(request.getUsername()));
                                                        iterator.remove();
                                                        System.out.println("The request has been removed, user " + request.getUsername() + " has been notified.");
                                                        System.out.println("Sending you to next request:\n");
                                                    }
                                                }
                                            }
                                            if (isFound == 0) {
                                                System.out.println("You have no requests");
                                                break;
                                            }
                                            System.out.println("Now you have no requests");
                                            break;
                                        } else if (isReqHold == 2) {
                                            List<Request> requestsFromHolder = RequestsHolder.getRequests();
                                            Iterator<Request> iterator = requestsFromHolder.iterator();

                                            while (iterator.hasNext()) {
                                                Request request = iterator.next();
                                                System.out.println(request.toString());
                                                System.out.println("\nDid you solve this request?");
                                                System.out.println("    1) Yes");
                                                System.out.println("    2) Solve it now, or later");
                                                System.out.println("    3) Reject this request");
                                                int isSolved = scanningNextInt(scanner);

                                                if (isSolved == 1) {
                                                    // Deleting the request from the RequestHolder and notify the user
                                                    request.resolvedRequest(getUserByUsername(request.getUsername()));
                                                    iterator.remove();
                                                    System.out.println("Sending you to next request:\n");
                                                } else if (isSolved == 2) {
                                                    System.out.println("Sending you to main menu to solve the request ...");
                                                    break;
                                                } else if (isSolved == 3) {
                                                    // Rejecting and removing from the request holder
                                                    System.out.println("Rejecting and removing this request ...");
                                                    request.rejectRequest(getUserByUsername(request.getUsername()));
                                                    iterator.remove();
                                                    System.out.println("The request has been removed, user " + request.getUsername() + " has been notified.");
                                                    System.out.println("Sending you to next request:\n");
                                                }
                                            }
                                            System.out.println("Now the RequestHolder is empty.");
                                            break;
                                        }


                                    case 8:
                                        System.out.println("What would you like to update?");
                                        System.out.println("    1) Productions");
                                        System.out.println("    2) Actors");
                                        int whatUpdate = scanningNextInt(scanner);

                                        if (whatUpdate == 1) {
                                            System.out.println("Type the title of the production you want to update");
                                            String myTitle = scanner.nextLine();
                                            Production myProduction = findProductionByName(myTitle);
                                            if (myProduction == null) {
                                                System.out.println("There is no production named " + myTitle);
                                                break;
                                            }

                                            if (myProduction.type.equals("Movie")) {
                                                System.out.println("What do you want to change about your movie?");
                                                System.out.println("    1) Title");
                                                System.out.println("    2) Type (Not recommended)");
                                                System.out.println("    3) Directors");
                                                System.out.println("    4) Actors");
                                                System.out.println("    5) Genres");
                                                System.out.println("    6) Ratings");
                                                System.out.println("    7) Plot");
                                                System.out.println("    8) Average rating (Not recommended)");
                                                System.out.println("    9) Duration");
                                                System.out.println("    10) Release year");
                                                int updated = scanningNextInt(scanner);

                                                if (updated == 1) {
                                                    System.out.println("Enter The new Title");
                                                    String newTitle = scanner.nextLine();
                                                    System.out.println("Old title: " + myProduction.title);
                                                    myProduction.title = newTitle;
                                                    System.out.println("New title: " + myProduction.title);
                                                    break;

                                                } else if (updated == 2) {
                                                    System.out.println("Enter the new type. Attention, this could be dangerous!");
                                                    String newType = scanner.nextLine();
                                                    System.out.println("Old Type: " + myProduction.type);
                                                    myProduction.type = newType;
                                                    System.out.println("New Type: " + myProduction.type);
                                                    break;

                                                } else if (updated == 3) {
                                                    System.out.println("How many directors are in the end?");
                                                    int nrDirectors = scanningNextInt(scanner);

                                                    List<String> newDirectors = new ArrayList<>();
                                                    for (int i = 1; i <= nrDirectors; i++) {
                                                        System.out.println("Enter the director number " + i);
                                                        String newDirector = scanner.nextLine();
                                                        newDirectors.add(newDirector);
                                                    }

                                                    System.out.println("Old directors:\n" + myProduction.directors.toString());
                                                    myProduction.directors = newDirectors;
                                                    System.out.println("New directors:\n" + myProduction.directors.toString());
                                                    break;

                                                } else if (updated == 4) {
                                                    System.out.println("How many actors are in the end?");
                                                    int nrActors = scanningNextInt(scanner);

                                                    List<String> newActors = new ArrayList<>();
                                                    for (int i = 1; i <= nrActors; i++) {
                                                        System.out.println("Enter the actor number " + i);
                                                        String newActor = scanner.nextLine();
                                                        newActors.add(newActor);
                                                    }

                                                    System.out.println("Old actors:\n" + myProduction.actors.toString());
                                                    myProduction.actors = newActors;
                                                    System.out.println("New actors:\n" + myProduction.actors.toString());
                                                    break;

                                                } else if (updated == 5) {
                                                    System.out.println("How many genres are in the end?");
                                                    int nrGenres = scanningNextInt(scanner);

                                                    List<Production.Genre> newGenres = new ArrayList<>();
                                                    for (int i = 1; i <= nrGenres; i++) {
                                                        System.out.println("Enter Genre number " + i);
                                                        String newGenre = scanner.nextLine();
                                                        Production.Genre aGenre = new Production.Genre(newGenre);
                                                        newGenres.add(aGenre);
                                                    }

                                                    System.out.println("Old genres:\n" + myProduction.genres);
                                                    myProduction.genres = newGenres;
                                                    System.out.println("New genres:\n" + myProduction.genres);
                                                    break;

                                                } else if (updated == 6) {
                                                    System.out.println("How many ratings are in the end?");
                                                    int nrRatings = scanningNextInt(scanner);

                                                    List<Rating> newRatings = new ArrayList<>();
                                                    for (int i = 1; i <= nrRatings; i++) {
                                                        System.out.println("Rating number " + i);
                                                        System.out.println("Enter username:");
                                                        String newUsername = scanner.nextLine();

                                                        System.out.println("Enter rating: (int)");
                                                        int newRate = scanningNextInt(scanner);

                                                        System.out.println("Enter comment:");
                                                        String newComment = scanner.nextLine();

                                                        Rating newRating = new Rating(newUsername, newRate, newComment);
                                                        newRatings.add(newRating);
                                                    }
                                                    System.out.println("Old rating:\n" + myProduction.ratings.toString());
                                                    myProduction.ratings = newRatings;
                                                    System.out.println("New rating:\n" + myProduction.ratings.toString());
                                                    break;

                                                } else if (updated == 7) {
                                                    System.out.println("Enter new plot:");
                                                    String newPlot = scanner.nextLine();

                                                    System.out.println("Old Plot:\n" + myProduction.plot);
                                                    myProduction.plot = newPlot;
                                                    System.out.println("New plot:\n" + myProduction.plot);
                                                    break;

                                                } else if (updated == 8) {
                                                    System.out.println("Enter new average rating. Atention, this could be dangerous: (double)");
                                                    Double newAverageRating = scanner.nextDouble();
                                                    scanner.nextLine();

                                                    System.out.println("Old average rating:\n" + myProduction.averageRating);
                                                    myProduction.averageRating = newAverageRating;
                                                    System.out.println("New average rating:\n" + myProduction.averageRating);
                                                    break;

                                                } else if (updated == 9) {
                                                    System.out.println("Enter the new duration:");
                                                    String newDuration = scanner.nextLine();

                                                    System.out.println("Old duration: " + myProduction.duration);
                                                    myProduction.duration = newDuration;
                                                    System.out.println("New duration: " + myProduction.duration);
                                                    break;

                                                } else if (updated == 10) {
                                                    System.out.println("Enter the new release year:");
                                                    Integer newReleaseYear = scanningNextInt(scanner);

                                                    System.out.println("Old release year: " + myProduction.releaseYear);
                                                    myProduction.releaseYear = newReleaseYear;
                                                    System.out.println("New release year: " + myProduction.releaseYear);
                                                    break;
                                                }
                                            } else if (myProduction.type.equals("Series") && myProduction instanceof Series){
                                                // This means our production is a series
                                                System.out.println("What do you want to change about this series?");
                                                System.out.println("    1) Title");
                                                System.out.println("    2) Type (Not recommended)");
                                                System.out.println("    3) Directors");
                                                System.out.println("    4) Actors");
                                                System.out.println("    5) Genres");
                                                System.out.println("    6) Ratings");
                                                System.out.println("    7) Plot");
                                                System.out.println("    8) Average rating (Not recommended)");
                                                System.out.println("    9) Release year");
                                                System.out.println("    10) Seasons & seasons number");
                                                int updated = scanningNextInt(scanner);
                                                if (updated == 1) {
                                                    System.out.println("Enter The new Title");
                                                    String newTitle = scanner.nextLine();
                                                    System.out.println("Old title: " + myProduction.title);
                                                    myProduction.title = newTitle;
                                                    System.out.println("New title: " + myProduction.title);
                                                    break;

                                                } else if (updated == 2) {
                                                    System.out.println("Enter the new type. Attention, this could be dangerous!");
                                                    String newType = scanner.nextLine();
                                                    System.out.println("Old Type: " + myProduction.type);
                                                    myProduction.type = newType;
                                                    System.out.println("New Type: " + myProduction.type);
                                                    break;

                                                } else if (updated == 3) {
                                                    System.out.println("How many directors are in the end?");
                                                    int nrDirectors = scanningNextInt(scanner);

                                                    List<String> newDirectors = new ArrayList<>();
                                                    for (int i = 1; i <= nrDirectors; i++) {
                                                        System.out.println("Enter the director number " + i);
                                                        String newDirector = scanner.nextLine();
                                                        newDirectors.add(newDirector);
                                                    }

                                                    System.out.println("Old directors:\n" + myProduction.directors.toString());
                                                    myProduction.directors = newDirectors;
                                                    System.out.println("New directors:\n" + myProduction.directors.toString());
                                                    break;
                                                } else if (updated == 4) {
                                                    System.out.println("How many actors are in the end?");
                                                    int nrActors = scanningNextInt(scanner);

                                                    List<String> newActors = new ArrayList<>();
                                                    for (int i = 1; i <= nrActors; i++) {
                                                        System.out.println("Enter the actor number " + i);
                                                        String newActor = scanner.nextLine();
                                                        newActors.add(newActor);
                                                    }

                                                    System.out.println("Old actors:\n" + myProduction.actors.toString());
                                                    myProduction.actors = newActors;
                                                    System.out.println("New actors:\n" + myProduction.actors.toString());
                                                    break;

                                                } else if (updated == 5) {
                                                    System.out.println("How many genres are in the end?");
                                                    int nrGenres = scanningNextInt(scanner);

                                                    List<Production.Genre> newGenres = new ArrayList<>();
                                                    for (int i = 1; i <= nrGenres; i++) {
                                                        System.out.println("Enter Genre number " + i);
                                                        String newGenre = scanner.nextLine();
                                                        Production.Genre aGenre = new Production.Genre(newGenre);
                                                        newGenres.add(aGenre);
                                                    }

                                                    System.out.println("Old genres:\n" + myProduction.genres);
                                                    myProduction.genres = newGenres;
                                                    System.out.println("New genres:\n" + myProduction.genres);
                                                    break;

                                                } else if (updated == 6) {
                                                    System.out.println("How many ratings are in the end?");
                                                    int nrRatings = scanningNextInt(scanner);

                                                    List<Rating> newRatings = new ArrayList<>();
                                                    for (int i = 1; i <= nrRatings; i++) {
                                                        System.out.println("Rating number " + i);
                                                        System.out.println("Enter username:");
                                                        String newUsername = scanner.nextLine();

                                                        System.out.println("Enter rating: (int)");
                                                        int newRate = scanningNextInt(scanner);

                                                        System.out.println("Enter comment:");
                                                        String newComment = scanner.nextLine();

                                                        Rating newRating = new Rating(newUsername, newRate, newComment);
                                                        newRatings.add(newRating);
                                                    }
                                                    System.out.println("Old rating:\n" + myProduction.ratings.toString());
                                                    myProduction.ratings = newRatings;
                                                    System.out.println("New rating:\n" + myProduction.ratings.toString());
                                                    break;

                                                } else if (updated == 7) {
                                                    System.out.println("Enter new plot:");
                                                    String newPlot = scanner.nextLine();

                                                    System.out.println("Old Plot:\n" + myProduction.plot);
                                                    myProduction.plot = newPlot;
                                                    System.out.println("New plot:\n" + myProduction.plot);
                                                    break;

                                                } else if (updated == 8) {
                                                    System.out.println("Enter new average rating. Atention, this could be dangerous: (double)");
                                                    Double newAverageRating = scanner.nextDouble();
                                                    scanner.nextLine();

                                                    System.out.println("Old average rating:\n" + myProduction.averageRating);
                                                    myProduction.averageRating = newAverageRating;
                                                    System.out.println("New average rating:\n" + myProduction.averageRating);
                                                    break;

                                                } else if (updated == 9) {
                                                    System.out.println("Enter the new release year:");
                                                    Integer newReleaseYear = scanningNextInt(scanner);

                                                    System.out.println("Old release year: " + myProduction.releaseYear);
                                                    myProduction.releaseYear = newReleaseYear;
                                                    System.out.println("New release year: " + myProduction.releaseYear);
                                                    break;

                                                } else if (updated == 10) {
                                                    System.out.println("Enter the new number of seasons:");
                                                    int newNumSeasons = scanningNextInt(scanner);

                                                    Map<String, List<Episode>> newSeasons = new HashMap<>();
                                                    for (int i = 1; i <= newNumSeasons; i++) {
                                                        System.out.println("Enter the details for season " + i);

                                                        List<Episode> newEpisodes = new ArrayList<>();
                                                        System.out.println("How many episodes does season " + i + " has?");
                                                        int nrNewEpisodes = scanningNextInt(scanner);

                                                        for (int j = 1; j <= nrNewEpisodes; j++) {
                                                            System.out.println("Enter the episode number " + j + " name");
                                                            String newEpName = scanner.nextLine();

                                                            System.out.println("Enter the duration of the episode number " + j);
                                                            String newDuration = scanner.nextLine();

                                                            Episode newEpisode = new Episode(newEpName, newDuration);
                                                            newEpisodes.add(newEpisode);
                                                        }
                                                        newSeasons.put("Season " + i, newEpisodes);
                                                    }

                                                    System.out.println("Old number of seasons:\n" + ((Series) myProduction).getNumSeasons().toString());
                                                    ((Series) myProduction).setNumSeasons(newNumSeasons);
                                                    System.out.println("New number of seasons:\n" + ((Series) myProduction).getNumSeasons().toString());

                                                    System.out.println("Old seasons:\n" + ((Series) myProduction).getSeasons().toString());
                                                    ((Series) myProduction).setSeasons(newSeasons);
                                                    System.out.println("New seasons:\n" + ((Series) myProduction).getSeasons().toString());
                                                    System.out.println();
                                                    break;
                                                }
                                            }
                                        } else if (whatUpdate == 2) {
                                            System.out.println("Type the name of the Actor:");
                                            String updatedActorName = scanner.nextLine();

                                            Actor newActor = findActorByName(updatedActorName);
                                            if (updatedActorName == null) {
                                                System.out.println("There is no actor named " + updatedActorName);
                                                break;
                                            }

                                            System.out.println("What do you want to change about your actor?");
                                            System.out.println("    1) Name");
                                            System.out.println("    2) Performances");
                                            System.out.println("    3) Biography");
                                            int actorCase = scanningNextInt(scanner);

                                            if (actorCase == 1) {
                                                System.out.println("Enter the new name for your actor");
                                                String newName = scanner.nextLine();

                                                System.out.println("Old name: " + newActor.getName());
                                                newActor.setName(newName);
                                                System.out.println("New name: " + newActor.getName());
                                                break;

                                            } else if (actorCase == 2) {
                                                System.out.println("Enter number of performances");
                                                int newNumPerformances = scanningNextInt(scanner);

                                                List<Actor.Performance> newPerformances = new ArrayList<>();
                                                for (int i = 1; i <= newNumPerformances; i++) {
                                                    System.out.println("Enter title for performance number " + i);
                                                    String newPerfTitle = scanner.nextLine();

                                                    System.out.println("Enter type of performance number " + i + ". (Series/Movie)");
                                                    String newPerfType = scanner.nextLine();

                                                    Actor.Performance aPerformance = new Actor.Performance(newPerfTitle, newPerfType);
                                                    newPerformances.add(aPerformance);
                                                }

                                                System.out.println("Old performances:\n" + newActor.getPerformances().toString());
                                                newActor.setPerformances(newPerformances);
                                                System.out.println("New performances:\n" + newActor.getPerformances().toString());
                                                break;

                                            } else if (actorCase == 3) {
                                                System.out.println("Enter the new biography");
                                                String newBiography = scanner.nextLine();

                                                System.out.println("Old biography: " + newActor.getBiography());
                                                newActor.setBiography(newBiography);
                                                System.out.println("New biography: " + newActor.getBiography());
                                                break;
                                            }
                                        }

                                    case 9:
                                        System.out.println("What do you want to do?");
                                        System.out.println("    1) Add a User in system");
                                        System.out.println("    2) Remove a User from system");
                                        int userAction = scanningNextInt(scanner);

                                        if (userAction == 1) {
                                            System.out.println("Enter the full name:");
                                            String myName = scanner.nextLine();

                                            if (myName.length() < 3) {
                                                System.out.println("Please introduce a complete name");
                                                break;
                                            }

                                            // creating a proper username
                                            String myUsername = myUser.createUsername(myName);
                                            System.out.println("Your username is: " + myUsername);

                                            // reading experience
                                            System.out.println("Enter experience: (int)");
                                            int myExperience = scanningNextInt(scanner);

                                            // reading information
                                            System.out.println("Now you'll have to enter information:");
                                            System.out.println("First, enter the email");
                                            String myEmail = scanner.nextLine();

                                            if (myEmail.length() < 3) {
                                                System.out.println("Your email is too short");
                                                break;
                                            } else if (!myEmail.contains("@")) {
                                                System.out.println("Please respect this format: yyyy@zzz");
                                                break;
                                            }

                                            System.out.println("Enter the password:");
                                            String myPassword = scanner.nextLine();

                                            if (myPassword.length() < 3) {
                                                System.out.println("Your password is too short");
                                                break;
                                            }

                                            Credentials myCredentials = new Credentials(myEmail, myPassword);

                                            System.out.println("Enter the country");
                                            String myCountry = scanner.nextLine();

                                            System.out.println("Enter the age: (int)");
                                            int myAge = scanningNextInt(scanner);

                                            System.out.println("Enter the gender");
                                            String myGender = scanner.nextLine();

                                            System.out.println("Enter the birth date. Format: yyyy-mm-dd");
                                            String myBirthDate = scanner.nextLine();

                                            User.Information.InformationBuilder myInformationBuilder = new User.Information.InformationBuilder(myCredentials, myName, myCountry,
                                                    myAge, myGender, myBirthDate);


                                            // Now reading userType
                                            System.out.println("Which is the account type?");
                                            System.out.println("    1) Regular");
                                            System.out.println("    2) Contributor");
                                            System.out.println("    3) Admin");
                                            int isType = scanningNextInt(scanner);
                                            User.AccountType myUserType = null;
                                            if (isType == 1) {
                                                myUserType = User.AccountType.Regular;
                                            } else if (isType == 2) {
                                                myUserType = User.AccountType.Contributor;
                                            } else if (isType == 3) {
                                                myUserType = User.AccountType.Admin;
                                            }

                                            // Reading Production Contribution
                                            System.out.println("Now we are reading production contributions");
                                            System.out.println("How many productions contributions does this user have?");
                                            int nrProdContr = scanningNextInt(scanner);

                                            List<String> myProductionContributions = new ArrayList<>();
                                            for (int i = 1; i <= nrProdContr; i++) {
                                                System.out.println("Enter the name of the production contribution number " + i);
                                                String myProdContr = scanner.nextLine();
                                                myProductionContributions.add(myProdContr);
                                            }

                                            // Reading Actors contributions
                                            System.out.println("Now we are reading actors contributions");
                                            System.out.println("How many actors contributions does this user have?");
                                            int nrActContr = scanningNextInt(scanner);

                                            List<String> myActorsContributions = new ArrayList<>();
                                            for (int i = 1; i <= nrActContr; i++) {
                                                System.out.println("Enter the actor contribution number " + i);
                                                String myActContr = scanner.nextLine();
                                                myActorsContributions.add(myActContr);
                                            }

                                            // Reading favorite Productions
                                            System.out.println("Now we are reading favorite Productions");
                                            System.out.println("How many favorite producitons does this user have?");
                                            int nrFavProd = scanningNextInt(scanner);

                                            List<String> myFavoriteProduction = new ArrayList<>();
                                            for (int i = 1; i <= nrFavProd; i++) {
                                                System.out.println("Enter favorite Production number " + i);
                                                String myFavProd = scanner.nextLine();
                                                myFavoriteProduction.add(myFavProd);
                                            }

                                            // Reading favorite actors
                                            System.out.println("Now we are reading favorite actors");
                                            System.out.println("How many favorite actors does this user have?");
                                            int nrFavAct = scanningNextInt(scanner);

                                            List<String> myFavoriteActors = new ArrayList<>();
                                            for (int i = 1; i <= nrFavAct; i++) {
                                                System.out.println("Enter the favorite Actor number " + i);
                                                String myFavAct = scanner.nextLine();
                                                myFavoriteActors.add(myFavAct);
                                            }

                                            // Now reading the notifications
                                            System.out.println("Now we are reading notifications");
                                            System.out.println("How many notifications does this user have?");
                                            int nrNotif = scanningNextInt(scanner);

                                            List<String> myNotifications = new ArrayList<>();
                                            for (int i = 1; i <= nrNotif; i++) {
                                                System.out.println("Enter the notification number " + i);
                                                String myNotif = scanner.nextLine();
                                                myNotifications.add(myNotif);
                                            }

                                            // Adding a new user using UserFactory
                                            User newUser = UserFactory.createUser(myUserType, myUsername, myExperience, myInformationBuilder,
                                                    myProductionContributions, myActorsContributions, myFavoriteProduction,
                                                    myFavoriteActors, myNotifications);

                                            Admin.addUser(newUser);
                                            System.out.println("User " + myUsername + " has been added in the system");
                                            break;
                                        } else if (userAction == 2) {
                                            System.out.println("Enter the username of the account that you want to remove");
                                            String toRemove = scanner.nextLine();
                                            User userToRemove = getUserByUsername(toRemove);
                                            if (userToRemove == null) {
                                                System.out.println("There is no user with username " + toRemove + " in the system");
                                                break;
                                            }
                                            User.removeUser(userToRemove);
                                            System.out.println("User " + toRemove + " has been removed from system");
                                            break;
                                        }

                                    case 10:
                                        isAuthenticated = false;
                                        continue loggingInLoop;
                                }
                            }
                    }
                }
            }
        } else if (mode == 2) {
            // Opening the graphic interface with swing
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new LoginGUI();
                }
            });
        } else {
            try {
                throw new InvalidCommandException("Invalid command, please try again.");
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean verifyPassword(String email, String password) {
        for (User user : users) {
            User.Information myInfo = user.getInformation();
            Credentials credentials = myInfo.getCredentials();
            if (credentials.email.equals(email) && credentials.password.equals(password)) {
                return true;
            }
        }
        return false;
    }

    public String findUsernameByEmail(String email) {
        for (User user : users) {
            User.Information myInfo =  user.getInformation();
            if (myInfo.getCredentials().email.equals(email)) {
                return user.username;
            }
        }
        return null;
    }

    public User findUserByEmail(String email) {
        for (User user : users) {
            User.Information myInfo = user.getInformation();
            if (myInfo.getCredentials().email.equals(email)) {
                return user;
            }
        }
        return null;
    }

    public static User getUserByUsername(String username) {
        for (User user : IMDB.instance.users) {
            if (user.username.equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void searchingAlgorithm(String forSearch) {
        int isFound = 0;
        for (Actor actor : actors) {
            if (actor.getName().equals(forSearch)) {
                System.out.println(actor.toString());
                isFound = 1;
                break;
            }
        }

        for (Production production : productions) {
            if (production.getTitle().equals(forSearch)) {
                System.out.println(production.toString());
                isFound = 1;
                break;
            }
        }
        if (isFound == 0) {
            System.out.println("Acest camp nu este in baza de date");
        }
    }

    public void filterProduction(String genre, int nrRating) {
        // If there is no specific genre and any number of ratings
        if (genre.equals("null")) {
            int isFound = 0;
            for (Production production : productions) {
                if (production.ratings.size() >= nrRating) {
                    System.out.println(production.toString());
                    isFound = 1;
                }
            }
            if (isFound == 0) {
                System.out.println("Nu exista nicio productie cu agest gen / cu un nr asa mare de ratings");
            }
        } else {
            int isFound = 0;
            // If it is a specific genre and any number of ratings
            for (Production production : productions) {
                for (Production.Genre myGenre : production.genres) {
                    if (myGenre.getName().contains(genre) && production.ratings.size() >= nrRating) {
                        System.out.println(production.toString());
                        isFound = 1;
                    }
                }
            }
            if (isFound == 0) {
                System.out.println("Nu exista nicio productie cu agest gen / cu un rating asa de mare");
            }
        }
    }

    public void sortActors() {
        Collections.sort(actors, new Comparator<Actor>() {
            @Override
            public int compare(Actor o1, Actor o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    public Production findProductionByName(String name) {
        for (Production production : productions) {
            if (production.title.equals(name)) {
                return production;
            }
        }
        return null;
    }

    public Actor findActorByName(String name) {
        for (Actor actor : actors) {
            if (actor.getName().equals(name)) {
                return actor;
            }
        }
        return null;
    }

    public void removeProductionFromFavorites(String name, User user) {
        int isFound = 0;
        for (String title : user.favoriteProductions) {
            if (title.equals(name)) {
                user.favoriteProductions.remove(title);
                isFound = 1;
            }
        }
        if (isFound == 0) {
            System.out.println("There is no production named " + name + " in your favorite list");
        } else {
            System.out.println("Production named " + name + " has been removed from your list");
        }
    }

    public void removeActorFromFavorites(String name, User user) {
        int isFound = 0;
        for (String actorName : user.favoriteActors) {
            if (actorName.equals(name)) {
                user.favoriteActors.remove(actorName);
                isFound = 1;
            }
        }
        if (isFound == 0) {
            System.out.println("There is no Actor named " + name + " in your favorite list");
        } else {
            System.out.println("Actor named " + name + " has been removed from your favorite list");
        }
    }

    public Rating findRatingByUsername(User user, Production production) {
        for (Rating rating : production.ratings) {
            if (rating.username.equals(user.username)) {
                return rating;
            }
        }
        return null;
    }

    public int scanningNextInt(Scanner scanner) {
        int myInt = 0;
        boolean validInput = false;
        while (validInput == false) {
            System.out.println("Enter an int: ");
            try {
                myInt = scanner.nextInt();
                scanner.nextLine();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please try again.");
                scanner.nextLine();
            }
        }
        return myInt;
    }

    public static void main(String[] args) {
        IMDB.getInstance().run();
    }
}