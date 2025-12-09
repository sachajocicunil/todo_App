package com.libraryapp.todoapp;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

// Définition du préfixe commun pour toutes les ressources REST
@ApplicationPath("/api")
public class TodoApplication extends Application {
    // Pas besoin de méthode ici, JAX-RS scanne automatiquement les @Path
}
