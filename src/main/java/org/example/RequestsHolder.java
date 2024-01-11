package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestsHolder {
    private static List<Request> requests = new ArrayList<>();
    public static void addRequest(Request request) {
        requests.add(request);
        String userTo = request.getTo();
        for (User user : IMDB.getInstance().users) {
            if (user.userType.equals(User.AccountType.Admin)) {
                user.update("ADMINs has got a new request from: " +request.getUsername());
            }
        }
    }

    public String toString() {
        return "RequestHolder: " + "\n" + requests;
    }

    public static void displayInfo() {
        System.out.println(requests.toString());
    }

    public static void removeRequest(Request request) {
        requests.remove(request);
    }

    public static void notifyUser(String username, String message) {
        System.out.println("Notificam " + username + ": " + message);
    }

    public static List<Request> getRequests() {
        return requests;
    }
}
