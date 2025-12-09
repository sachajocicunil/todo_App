package com.libraryapp.todoapp;

import com.libraryapp.todoapp.TaskService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

// 1. @Path définit l'URL de base. Ici : http://localhost:8080/api/tasks
@Path("/tasks")
public class TaskResource {

    @Inject
    private TaskService taskService; // On injecte notre service (comme ApplicationState dans l'exam)

    // --- READ (Lire toutes les tâches) ---
    // Correspond à OQ1 Q5 "Read: getTeacher"
    @GET
    @Produces(MediaType.APPLICATION_JSON) // On renvoie du JSON
    public List<Task> getAllTasks() {
        return taskService.findAll();
    }

    // --- READ (Lire une seule tâche par ID) ---
    @GET
    @Path("/{id}") // URL : /api/tasks/1
    @Produces(MediaType.APPLICATION_JSON)
    public Task getTask(@PathParam("id") Long id) {
        return taskService.find(id);
    }

    // --- CREATE (Ajouter une tâche) ---
    // Correspond à OQ1 Q5 "Create: addTeacher"
    @POST
    @Consumes(MediaType.APPLICATION_JSON) // On reçoit du JSON
    @Produces(MediaType.APPLICATION_JSON) // On renvoie la tâche créée (avec son ID généré)
    public Task createTask(Task task) {
        return taskService.create(task);
    }

    // --- UPDATE (Mettre à jour une tâche) ---
    // Correspond à OQ1 Q5 "Update: setTeacher"
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTask(@PathParam("id") Long id, Task task) {
        // Petite sécurité : on s'assure que l'ID de l'URL correspond à l'objet
        if (taskService.find(id) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        // Note: Dans l'examen, la méthode setTeacher prenait username + teacher
        taskService.update(task);
        return Response.ok().build();
    }

    // --- DELETE (Supprimer une tâche) ---
    // Correspond à OQ1 Q5 "Delete: removeTeacher"
    @DELETE
    @Path("/{id}")
    public void deleteTask(@PathParam("id") Long id) {
        taskService.delete(id);
    }
}