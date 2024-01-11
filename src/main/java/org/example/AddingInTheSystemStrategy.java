package org.example;

public class AddingInTheSystemStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience(int initialExperience) {
        int newExperience = 15;
        return newExperience + initialExperience;
    }
}
