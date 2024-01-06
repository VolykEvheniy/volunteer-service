package com.webtutsplus.ecommerce.model;
import com.webtutsplus.ecommerce.enums.TokenStatus;

import javax.persistence.*;
import java.security.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "tokens")
public class AuthenticationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

//    @Column(name = "created_at")
//    private Date createdAt;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Enumerated(EnumType.STRING)
    private TokenStatus status;


    @Column(name = "token")
    private String token;

    @Column(name = "created_at")
    private Date createdDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public AuthenticationToken(User user) {
        this.user = user;
        this.createdDate = new Date();
        this.token = UUID.randomUUID().toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TokenStatus getStatus() {
        return status;
    }

    public void setStatus(TokenStatus status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public AuthenticationToken(Integer id, String Token, Date createdDate, User user) {
        this.id = id;
        this.token = Token;
        this.createdDate = createdDate;
        this.user = user;
    }

    public AuthenticationToken() {
    }
}
