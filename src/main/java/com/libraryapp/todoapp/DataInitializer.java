package com.libraryapp.todoapp;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

@Singleton // Dit au serveur : "Il n'y a qu'une seule instance de cette classe"
@Startup   // Dit au serveur : "Lance cette classe dès que l'app est déployée"
public class DataInitializer {

    @Inject
    private TaskService taskService; // On utilise ton service pour parler à la base

    @PostConstruct // Cette méthode s'exécute toute seule juste après l'injection
    public void init() {
        // Tâche 1
        Task t1 = new Task();
        t1.setTitle("Réussir l'examen SoftArch");
        t1.setDescription("Revoir les diagrammes et le code JSF");
        t1.setDone(false);
        taskService.create(t1);

        // Tâche 2
        Task t2 = new Task();
        t2.setTitle("Faire les courses");
        t2.setDescription("Pain, Lait, Café");
        t2.setDone(true); // Celle-ci sera marquée comme terminée
        taskService.create(t2);

        // Tâche 3
        Task t3 = new Task();
        t3.setTitle("Sport");
        t3.setDescription("Séance de 1h à la salle");
        t3.setDone(false);
        taskService.create(t3);

        System.out.println("--- 🚀 DONNÉES INITIALISÉES AVEC SUCCÈS ---");
    }
}