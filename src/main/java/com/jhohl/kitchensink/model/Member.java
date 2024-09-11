package com.jhohl.kitchensink.model;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "members")
public class Member implements Serializable {

    public static final String SEQUENCE_NAME = "member_sequence";
    @Id
    private Long id;

    @NotNull
    @Size(min = 1, max = 25, message= " Size must be between 1 and 25")
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String name;

    @NotNull
    @NotEmpty(message = "Must not be empty")
    @Email(message = "Must be a well-formed email address")
    private String email;

    @NotNull
    @Size(min = 10, max = 12, message = "Must be between 10-12 characters")
    //@Digits(fraction = 0, integer = 12)

    private String phoneNumber;


    public Member(){

    }
    public Member(String name, String email, String number) {
        this.name = name;
        this.email = email;
        this.phoneNumber = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
