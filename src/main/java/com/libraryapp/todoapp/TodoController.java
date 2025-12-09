package com.libraryapp.todoapp;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Named(value = "todoController") // Le nom qu'on utilisera dans le XHTML
@SessionScoped
public class TodoController implements Serializable {

    @Inject
    private TaskService taskService; // On relie notre Service créé à l'étape précédente

    private Task newTask = new Task(); // L'objet vide pour le formulaire
    private List<Task> tasks; // La liste pour l'affichage

    // Méthode pour charger les tâches (souvent appelée par une méthode @PostConstruct)
    public List<Task> getTasks() {
        if (tasks == null) {
            tasks = taskService.findAll();
        }
        return tasks;
    }

    // Méthode appelée par le bouton "Ajouter"
    public String addTask() {
        taskService.create(newTask); // Sauvegarde en base
        newTask = new Task(); // Reset du formulaire
        tasks = taskService.findAll(); // Mise à jour de la liste
        return null; // Reste sur la même page
    }

    // Accessor pour le formulaire (Indispensable selon OQ2 Q2 [cite: 334])
    public Task getNewTask() {
        return newTask;
    }
}