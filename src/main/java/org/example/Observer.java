package org.example;


import java.util.ArrayList;
import java.util.List;

abstract class Subject {
    private List<Observer> observers = new ArrayList<>();
    public void attach(Observer o) {
        observers.add(o);
    }

    public void detach(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers(String notification) {
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }
}
public interface Observer {
    void update(String notification);
}
