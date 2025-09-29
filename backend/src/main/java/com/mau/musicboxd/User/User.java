package com.mau.musicboxd.User;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "\"User\"", uniqueConstraints = {@UniqueConstraint(name = "unique_email", columnNames = {"email"})})
public class User {

    @Id
    @SequenceGenerator(
        name = "user_sequence",
        sequenceName = "user_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "user_sequence"
    )
    
    private Long id;
    private String name;
    private String email;
    private String password;
    @CreatedDate
    private LocalDate createdAt;

    public User(){

    }
    public User(Long id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public LocalDate getCreatedDate() {return createdAt;}

    @Override
    public String toString(){
        return "name= " + this.name + " id= " + this.id;
    }
}
