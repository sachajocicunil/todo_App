package com.libraryapp.todoapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void defaultConstructor_shouldInitializeWithNullIdAndDoneFalse() {
        Task task = new Task();
        assertNull(task.getId(), "Default id should be null before persistence");
        assertFalse(task.isDone(), "Default done should be false");
        assertNull(task.getTitle(), "Title should be null by default");
        assertNull(task.getDescription(), "Description should be null by default");
    }

    @Test
    void parameterizedConstructor_shouldSetTitleDescriptionAndDefaultDoneFalse() {
        Task task = new Task("Read book", "Read Clean Code");
        assertNull(task.getId());
        assertEquals("Read book", task.getTitle());
        assertEquals("Read Clean Code", task.getDescription());
        assertFalse(task.isDone());
    }

    @Test
    void settersAndGetters_shouldUpdateValues() {
        Task task = new Task();

        task.setTitle("Write tests");
        task.setDescription("Add JUnit tests for Task model");
        task.setDone(true);

        assertEquals("Write tests", task.getTitle());
        assertEquals("Add JUnit tests for Task model", task.getDescription());
        assertTrue(task.isDone());
    }
}
