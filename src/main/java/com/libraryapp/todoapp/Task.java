package com.libraryapp.todoapp;

import jakarta.persistence.*;
import java.io.Serializable;

// 1. @Entity indique à JPA que cette classe correspond à une table en base de données [cite: 382]
@Entity
// Bonne pratique : on nomme explicitement la table
@Table(name = "tasks")
public class Task implements Serializable {

    // 2. @Id définit la clé primaire [cite: 386]
    @Id
    // @GeneratedValue gère l'auto-incrémentation (comme vu implicitement avec les IDs 1, 2, 3... dans l'examen [cite: 440])
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id") // [cite: 388]
    private Long id;

    private String title;

    private String description;

    private boolean done;

    // Remplacement de la relation par un simple identifiant utilisateur
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 3. Constructeur vide : OBLIGATOIRE pour JPA (le framework en a besoin pour instancier l'objet)
    public Task() {
    }

    // Constructeur utilitaire pour nous (similaire au constructeur Teacher dans OQ1 [cite: 27])
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.done = false; // Par défaut, une tâche n'est pas finie
    }

    // --- Accessors (Getters) & Mutators (Setters) ---
    // Demandés explicitement dans l'examen OQ2 Q2 [cite: 334] et OQ1 [cite: 21]

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}