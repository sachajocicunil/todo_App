package com.libraryapp.todoapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void defaultConstructor_setsDoneFalse() {
        Task t = new Task();
        assertFalse(t.isDone(), "New task should be not done by default");
    }

    @Test
    void gettersAndSetters_work() {
        Task t = new Task();
        t.setTitle("Title");
        t.setDescription("Desc");
        t.setDone(true);

        assertEquals("Title", t.getTitle());
        assertEquals("Desc", t.getDescription());
        assertTrue(t.isDone());
    }

    @Test
    void convenienceConstructor_initializesFields() {
        Task t = new Task("Buy milk", "2 bottles");
        assertEquals("Buy milk", t.getTitle());
        assertEquals("2 bottles", t.getDescription());
        assertFalse(t.isDone());
    }
}
