package com.cliff.messaging.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Person {
    private final String identifier;
    private final String name;
    private final String gender;

    public Person(@JsonProperty("name") final String name,
                  @JsonProperty("gender") final String gender,
                           @JsonProperty("identifier") final String identifier) {
        this.name = name;
        this.gender = gender;
        this.identifier = identifier;
    }
}
