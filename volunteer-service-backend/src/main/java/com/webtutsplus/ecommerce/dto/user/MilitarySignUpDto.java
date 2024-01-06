package com.webtutsplus.ecommerce.dto.user;

public class MilitarySignUpDto {
    private String firstName;
    private String lastName;
    //private String email;
    private String rank;

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


    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
