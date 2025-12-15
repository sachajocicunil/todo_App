package com.libraryapp.todoapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoControllerTest {

    private TodoController controller;
    private TaskService service;

    @BeforeEach
    void setUp() throws Exception {
        controller = new TodoController();
        service = mock(TaskService.class);
        Field f = TodoController.class.getDeclaredField("taskService");
        f.setAccessible(true);
        f.set(controller, service);
    }

    @Test
    void getTasks_lazilyLoadsFromService() {
        List<Task> list = new ArrayList<>();
        list.add(new Task("a","b"));
        when(service.findAll()).thenReturn(list);

        List<Task> first = controller.getTasks();
        List<Task> second = controller.getTasks();

        assertSame(first, second, "Should cache within the same instance");
        assertEquals(1, first.size());
        verify(service, times(1)).findAll();
    }

    @Test
    void addTask_persistsAndRefreshesListAndResetsForm() {
        // Arrange cached tasks to ensure refresh occurs
        when(service.findAll()).thenReturn(List.of(new Task("a","b")), List.of());
        controller.getTasks(); // cache

        Task formTask = controller.getNewTask();
        formTask.setTitle("New");
        formTask.setDescription("Desc");

        // Act
        String nav = controller.addTask();

        // Assert
        verify(service).create(formTask);
        verify(service, times(2)).findAll(); // initial + refresh
        assertNull(nav);
        assertNotSame(formTask, controller.getNewTask(), "Form should be reset to a new instance");
    }

    @Test
    void edit_setsTaskToEditAndStaysOnPage() {
        Task t = new Task("x","y");
        // emulate id set (like persisted entity)
        setId(t, 10L);
        when(service.find(10L)).thenReturn(t);

        String nav = controller.edit(10L);
        assertNull(nav);
        assertTrue(controller.isEditing(10L));
        assertSame(t, controller.getTaskToEdit());
    }

    @Test
    void saveTask_updatesRefreshesAndExitsEditMode() {
        Task t = new Task("x","y");
        setId(t, 7L);
        // prepare edit state
        controller.setTaskToEdit(t);

        String nav = controller.saveTask();
        assertNull(nav);
        verify(service).update(t);
        verify(service).findAll();
        assertNull(controller.getTaskToEdit());
        assertFalse(controller.isEditing(7L));
    }

    @Test
    void cancelEdit_exitsEditMode() {
        Task t = new Task("x","y");
        setId(t, 3L);
        controller.setTaskToEdit(t);

        controller.cancelEdit();
        assertNull(controller.getTaskToEdit());
        assertFalse(controller.isEditing(3L));
    }

    @Test
    void deleteTask_callsServiceAndRefreshes() {
        when(service.findAll()).thenReturn(List.of());
        String nav = controller.deleteTask(5L);
        assertNull(nav);
        verify(service).delete(5L);
        verify(service).findAll();
    }

    @Test
    void toggleTaskStatus_callsServiceAndRefreshes() {
        when(service.findAll()).thenReturn(List.of());
        String nav = controller.toggleTaskStatus(2L);
        assertNull(nav);
        verify(service).toggleTaskStatus(2L);
        verify(service).findAll();
    }

    // Helper to set private id for testing purposes
    private static void setId(Task task, Long id) {
        try {
            var f = Task.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(task, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
