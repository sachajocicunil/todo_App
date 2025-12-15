package com.libraryapp.todoapp;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskResourceTest {

    private TaskResource resource;
    private TaskService service;

    @BeforeEach
    void setUp() throws Exception {
        resource = new TaskResource();
        service = mock(TaskService.class);
        var field = TaskResource.class.getDeclaredField("taskService");
        field.setAccessible(true);
        field.set(resource, service);
    }

    @Test
    void getAllTasks_returnsListFromService() {
        when(service.findAll()).thenReturn(List.of(new Task("a","b")));
        var list = resource.getAllTasks();
        assertEquals(1, list.size());
        verify(service).findAll();
    }

    @Test
    void getTask_returnsFromService() {
        Task t = new Task("x","y");
        when(service.find(42L)).thenReturn(t);
        assertSame(t, resource.getTask(42L));
    }

    @Test
    void createTask_persistsAndReturnsEntity() {
        Task incoming = new Task("a","b");
        when(service.create(incoming)).thenReturn(incoming);
        Task created = resource.createTask(incoming);
        assertSame(incoming, created);
        verify(service).create(incoming);
    }

    @Test
    void updateTask_returns404WhenMissing() {
        when(service.find(1L)).thenReturn(null);
        Response r = resource.updateTask(1L, new Task("a","b"));
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), r.getStatus());
        verify(service, never()).update(any());
    }

    @Test
    void updateTask_returnsOkWhenExists() {
        Task t = new Task("a","b");
        when(service.find(2L)).thenReturn(t);
        Response r = resource.updateTask(2L, t);
        assertEquals(Response.Status.OK.getStatusCode(), r.getStatus());
        verify(service).update(t);
    }

    @Test
    void deleteTask_callsService() {
        resource.deleteTask(9L);
        verify(service).delete(9L);
    }
}
