package com.libraryapp.todoapp;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("userController")
@ViewScoped
public class UserController implements Serializable {

    @Inject
    private transient UserService userService;

    private User newUser = new User();
    private List<User> users;

    public List<User> getUsers() {
        if (users == null) {
            users = userService.findAll();
        }
        return users;
    }

    public String addUser() {
        userService.create(newUser);
        newUser = new User();
        users = userService.findAll();
        return null;
    }

    public String deleteUser(Long id) {
        userService.delete(id);
        users = userService.findAll();
        return null;
    }

    public User getNewUser() {
        return newUser;
    }
}
