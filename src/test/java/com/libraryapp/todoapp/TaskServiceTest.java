package com.libraryapp.todoapp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskService service;
    private EntityManager em;

    @BeforeEach
    void setUp() throws Exception {
        service = new TaskService();
        em = mock(EntityManager.class);
        // inject mock EntityManager via reflection (field is private)
        var field = TaskService.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(service, em);
    }

    @Test
    void create_callsPersistAndReturnsTask() {
        Task t = new Task("t", "d");
        Task returned = service.create(t);
        verify(em).persist(t);
        assertSame(t, returned);
    }

    @Test
    void find_delegatesToEntityManager() {
        Task expected = new Task("a","b");
        when(em.find(Task.class, 1L)).thenReturn(expected);

        Task found = service.find(1L);
        assertSame(expected, found);
    }

    @Test
    void findAll_usesTypedQuery() {
        @SuppressWarnings("unchecked")
        TypedQuery<Task> q = mock(TypedQuery.class);
        when(em.createQuery("SELECT t FROM Task t", Task.class)).thenReturn(q);
        when(q.getResultList()).thenReturn(List.of(new Task("a","b")));

        List<Task> all = service.findAll();
        assertEquals(1, all.size());
        verify(q).getResultList();
    }

    @Test
    void update_callsMergeAndReturnsMerged() {
        Task t = new Task("a","b");
        Task merged = new Task("c","d");
        when(em.merge(t)).thenReturn(merged);

        Task res = service.update(t);
        assertSame(merged, res);
        verify(em).merge(t);
    }

    @Test
    void delete_removesWhenFound() {
        Task t = new Task("a","b");
        when(em.find(Task.class, 5L)).thenReturn(t);

        service.delete(5L);
        verify(em).remove(t);
    }

    @Test
    void delete_doesNothingWhenNotFound() {
        when(em.find(Task.class, 99L)).thenReturn(null);
        service.delete(99L);
        verify(em, never()).remove(any());
    }

    @Test
    void toggleTaskStatus_flipsAndMerges() {
        Task t = new Task("a","b");
        t.setDone(false);
        when(em.find(Task.class, 2L)).thenReturn(t);

        service.toggleTaskStatus(2L);

        assertTrue(t.isDone());
        verify(em).merge(t);
    }
}
