package org.example;

public class RatingStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience(int initialExperience) {
        int newExperience = 5;
        return initialExperience + newExperience;
    }
}
