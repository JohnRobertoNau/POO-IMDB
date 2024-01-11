package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Credentials {
    String email;
    String password;

    @JsonCreator
    public Credentials(@JsonProperty("email") String email,
                       @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }

    public String toString() {
        return "email: " + email + "\n" +
                "password: " + password;
    }

    public String getEmail() {
        return email;
    }
}
