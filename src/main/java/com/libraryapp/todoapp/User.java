package com.libraryapp.todoapp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.json.bind.annotation.JsonbTransient;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    // Par demande: mot de passe NON haché (à ne pas faire en prod)
    @Column(nullable = false)
    private String password;

    // La relation forte avec Task est supprimée: on garde un modèle simple côté User

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Plus de getter/setter de liste de tâches ici pour éviter la dépendance forte
}
