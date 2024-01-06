package com.webtutsplus.ecommerce.model;

import javax.persistence.*;

@Entity
@Table(name = "military")
public class Military {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

//    @Column(name = "email")
//    private String email;

    private String firstName;

    private String lastName;

    private String rank;

    public Military(User user, String firstName, String lastName, String rank) {
        this.user = user;
//        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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
