package com.libraryapp.todoapp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    // Un utilisateur peut avoir plusieurs tâches
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    // Helpers pour gérer la relation bidirectionnelle
    public void addTask(Task task) {
        if (task == null) return;
        tasks.add(task);
        task.setUser(this);
    }

    public void removeTask(Task task) {
        if (task == null) return;
        tasks.remove(task);
        if (task.getUser() == this) {
            task.setUser(null);
        }
    }
}
