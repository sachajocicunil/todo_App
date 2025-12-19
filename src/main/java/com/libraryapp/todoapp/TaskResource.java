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
    public Response getTask(@PathParam("id") Long id) {
        Task t = taskService.find(id);
        if (t == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(t).build();
    }

    // --- CREATE (Ajouter une tâche) ---
    // Correspond à OQ1 Q5 "Create: addTeacher"
    @POST
    @Consumes(MediaType.APPLICATION_JSON) // On reçoit du JSON
    @Produces(MediaType.APPLICATION_JSON) // On renvoie la tâche créée (avec son ID généré)
    public Response createTask(@jakarta.ws.rs.core.Context jakarta.ws.rs.core.UriInfo uriInfo, Task task) {
        if (task == null || task.getUserId() == null) {
            throw new BadRequestException("La tâche doit être associée à un utilisateur (userId obligatoire).");
        }
        try {
            Task created = taskService.create(task);
            java.net.URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(created.getId())).build();
            return Response.created(location).entity(created).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    // --- UPDATE (Mettre à jour une tâche) ---
    // Correspond à OQ1 Q5 "Update: setTeacher"
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTask(@PathParam("id") Long id, Task task) {
        Task existing = taskService.find(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (task == null || task.getUserId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("La tâche doit être associée à un utilisateur (userId obligatoire).")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        try {
            // Met à jour les champs autorisés
            existing.setTitle(task.getTitle());
            existing.setDescription(task.getDescription());
            existing.setDone(task.isDone());
            existing.setUserId(task.getUserId());
            Task updated = taskService.update(existing);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    // --- DELETE (Supprimer une tâche) ---
    // Correspond à OQ1 Q5 "Delete: removeTeacher"
    @DELETE
    @Path("/{id}")
    public Response deleteTask(@PathParam("id") Long id) {
        Task existing = taskService.find(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        taskService.delete(id);
        return Response.noContent().build();
    }
}