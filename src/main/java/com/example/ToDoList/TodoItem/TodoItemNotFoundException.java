//TodoItemNotFoundException.java
//This file defines a custom exception class, that extends the RuntimeException class and provides a message indicating the ID of the Todo item that was not found.

package com.example.ToDoList.TodoItem; 

public class TodoItemNotFoundException extends RuntimeException { // Custom exception class for handling Todo items not found errors
    public TodoItemNotFoundException(Long id) { // Constructor that takes the ID of the Todo item that was not found
        super("Todo item with ID " + id + " not found."); // Error message indicating that no to do item with that ID was found
    }
}