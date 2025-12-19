package com.libraryapp.todoapp;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@Singleton // Dit au serveur : "Il n'y a qu'une seule instance de cette classe"
@Startup   // Dit au serveur : "Lance cette classe dès que l'app est déployée"
public class DataInitializer {

    @Inject
    private TaskService taskService; // On utilise ton service pour parler à la base
    @Inject
    private UserService userService;

    @PostConstruct
    public void init() {
        // 1. CRÉER l'utilisateur en premier pour qu'il ait un ID (Primary Key)
        User u1 = new User();
        u1.setEmail("test@gmail.com");
        u1.setPassword("123");
        userService.create(u1); // L'utilisateur est maintenant dans la base et a un ID

        // 2. CRÉER les tâches et ASSIGNER l'utilisateur par son ID (FK simple)

        // Tâche 1
        Task t1 = new Task();
        t1.setTitle("Réussir l'examen SoftArch");
        t1.setDescription("Revoir les diagrammes et le code JSF");
        t1.setDone(false);
        t1.setUserId(u1.getId()); // 👈 Associe la tâche à l'utilisateur par ID
        taskService.create(t1); // Enregistre t1 dans la base avec l'ID de u1 (FK)

        // Tâche 2
        Task t2 = new Task();
        t2.setTitle("Faire les courses");
        t2.setDescription("Pain, Lait, Café");
        t2.setDone(true);
        t2.setUserId(u1.getId()); // 👈 Associe la tâche à l'utilisateur par ID
        taskService.create(t2);

        // Tâche 3
        Task t3 = new Task();
        t3.setTitle("Sport");
        t3.setDescription("Séance de 1h à la salle");
        t3.setDone(false);
        t3.setUserId(u1.getId()); // 👈 Associe la tâche à l'utilisateur par ID
        taskService.create(t3);

        // Plus de synchronisation bidirectionnelle nécessaire

        System.out.println("--- 🚀 DONNÉES INITIALISÉES AVEC SUCCÈS ---");
    }}