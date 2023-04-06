package model;

import androidx.annotation.NonNull;

import interfaces.GetClassesProperties;

public class UserInfo implements GetClassesProperties {

    protected String firstName;
    protected String lastName;
    protected String email;

    private String userType;


    public UserInfo(String firstNameValue, String lastNameValue, String emailValue, String userType) {
        this.firstName = firstNameValue;
        this.lastName = lastNameValue;
        this.email = emailValue;

        this.userType = userType;
    }

    public UserInfo(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserInfo(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;

    }

    public UserInfo() {
    }


    @NonNull
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    // created getter and setter methods
    // for all variables.
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }


    public String getUserType() {return userType;}
    public void setUserType(String userType) {this.userType = userType;}

    @Override
    public String name() {
        return firstName + " " + lastName;
    }

    @Override
    public String dogName() {
        return null;
    }

    @Override
    public String id() {
        return null;
    }
}

