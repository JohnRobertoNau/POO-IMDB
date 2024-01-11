package org.example;

public class RequestStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience(int initialExperience) {
        int newExperience = 10;
        return initialExperience + newExperience;
    }
}
