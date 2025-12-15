package com.libraryapp.todoapp;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Named(value = "todoController") // Le nom qu'on utilisera dans le XHTML
@ViewScoped
public class TodoController implements Serializable {

    @Inject
    private transient TaskService taskService; // On relie notre Service créé à l'étape précédente

    @Inject
    private transient UserService userService; // Pour lier une tâche à un utilisateur

    private Task newTask = new Task(); // L'objet vide pour le formulaire
    private List<Task> tasks; // La liste pour l'affichage
    private Task taskToEdit;
    private Long selectedUserId; // ID de l'utilisateur sélectionné pour la nouvelle tâche

    // Méthode pour charger les tâches (souvent appelée par une méthode @PostConstruct)
    public List<Task> getTasks() {
        if (tasks == null) {
            tasks = taskService.findAll();
        }
        return tasks;
    }

    // Méthode appelée par le bouton "Ajouter"
    public String addTask() {
        if (selectedUserId != null) {
            User u = userService.find(selectedUserId);
            if (u != null) {
                newTask.setUser(u);
            }
        }
        taskService.create(newTask); // Sauvegarde en base
        newTask = new Task(); // Reset du formulaire
        selectedUserId = null; // reset du select
        tasks = taskService.findAll(); // Mise à jour de la liste
        return null; // Reste sur la même page
    }

    public String deleteTask(Long taskId) {
        taskService.delete(taskId);
        tasks = taskService.findAll(); // Mise à jour de la liste
        return null;
    }

    public String toggleTaskStatus(Long taskId) {
        taskService.toggleTaskStatus(taskId);
        tasks = taskService.findAll(); // Mise à jour de la liste
        return null;
    }

    public String edit(Long taskId) {
        this.taskToEdit = taskService.find(taskId);
        // Rester sur la même page pour l'édition en ligne
        return null;
    }

    public String saveTask() {
        taskService.update(taskToEdit);
        tasks = taskService.findAll();
        // Quitter le mode édition en ligne et rester sur la page
        this.taskToEdit = null;
        return null;
    }

    public String cancelEdit() {
        // Annule l'édition et réinitialise l'état
        this.taskToEdit = null;
        return null;
    }

    public boolean isEditing(Long taskId) {
        return taskToEdit != null
                && taskToEdit.getId() != null
                && taskToEdit.getId().equals(taskId);
    }

    // Accessor pour le formulaire (Indispensable selon OQ2 Q2 [cite: 334])
    public Task getNewTask() {
        return newTask;
    }

    public Task getTaskToEdit() {
        return taskToEdit;
    }

    public void setTaskToEdit(Task taskToEdit) {
        this.taskToEdit = taskToEdit;
    }

    public Long getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(Long selectedUserId) {
        this.selectedUserId = selectedUserId;
    }
}