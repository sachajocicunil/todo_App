package com.libraryapp.todoapp;

import com.libraryapp.todoapp.Task;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class TaskService {

    @PersistenceContext(unitName = "todoPU") // Connecte à la base de données
    private EntityManager em;

    // CREATE : Équivalent de em.persist() vu dans l'examen [cite: 420]
    public Task create(Task task) {
        if (task == null || task.getUserId() == null) {
            throw new IllegalArgumentException("La tâche doit être associée à un utilisateur (userId non nul).");
        }
        // Validation référentielle applicative: le userId doit exister
        if (em.find(User.class, task.getUserId()) == null) {
            throw new IllegalArgumentException("Utilisateur inexistant pour userId=" + task.getUserId());
        }
        em.persist(task);
        return task;
    }

    // READ
    public Task find(Long id) {
        return em.find(Task.class, id);
    }

    public List<Task> findAll() {
        // JPQL (Java Persistence Query Language) - standard pour récupérer des listes
        return em.createQuery("SELECT t FROM Task t", Task.class).getResultList();
    }

    public List<Task> findByUser(Long userId) {
        TypedQuery<Task> q = em.createQuery("SELECT t FROM Task t WHERE t.userId = :uid", Task.class);
        q.setParameter("uid", userId);
        return q.getResultList();
    }

    // UPDATE : Équivalent de em.merge() vu dans l'examen [cite: 430]
    public Task update(Task task) {
        if (task == null || task.getUserId() == null) {
            throw new IllegalArgumentException("La tâche doit être associée à un utilisateur (userId non nul).");
        }
        // Validation référentielle applicative: le userId doit exister
        if (em.find(User.class, task.getUserId()) == null) {
            throw new IllegalArgumentException("Utilisateur inexistant pour userId=" + task.getUserId());
        }
        return em.merge(task);
    }

    // DELETE : Équivalent de em.remove() vu dans l'examen [cite: 431]
    public void delete(Long id) {
        Task task = find(id);
        if (task != null) {
            em.remove(task);
        }
    }

    public void toggleTaskStatus(Long id) {
        Task task = find(id);
        if (task != null) {
            task.setDone(!task.isDone());
            em.merge(task);
        }
    }
}