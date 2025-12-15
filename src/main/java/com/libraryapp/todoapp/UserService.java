package com.libraryapp.todoapp;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UserService {

    @PersistenceContext(unitName = "todoPU")
    private EntityManager em;

    public User create(User user) {
        em.persist(user);
        return user;
    }

    public User find(Long id) {
        return em.find(User.class, id);
    }

    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public User update(User user) {
        return em.merge(user);
    }

    public void delete(Long id) {
        User u = find(id);
        if (u != null) {
            em.remove(u);
        }
    }
}
