package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Episode {
    String episodeName;
    String duration;

    @JsonCreator
    public Episode(@JsonProperty("episodeName") String episodeName,
                   @JsonProperty("duration") String duration) {
        this.episodeName = episodeName;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "episodeName: " + episodeName + "\n" +
                "duration: " + duration;
    }
}
