package com.libraryapp.todoapp;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/users")
public class UserResource {

    @Inject
    private UserService userService;

    // LIST
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    // GET ONE
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") Long id) {
        User u = userService.find(id);
        if (u == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(u).build();
    }

    // CREATE
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@jakarta.ws.rs.core.Context jakarta.ws.rs.core.UriInfo uriInfo, User user) {
        if (user == null || isBlank(user.getEmail()) || isBlank(user.getPassword())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("email et password sont obligatoires")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        User created = userService.create(user);
        java.net.URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(created.getId())).build();
        return Response.created(location).entity(created).build();
    }

    // UPDATE
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long id, User user) {
        User existing = userService.find(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (user == null || isBlank(user.getEmail()) || isBlank(user.getPassword())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("email et password sont obligatoires")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        // Mettre à jour les champs autorisés
        existing.setEmail(user.getEmail());
        existing.setPassword(user.getPassword());
        User updated = userService.update(existing);
        return Response.ok(updated).build();
    }

    // DELETE
    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        User existing = userService.find(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        userService.delete(id);
        return Response.noContent().build();
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
