//TodoItemFormData.java
//This class is used to represent the form data for a Todo item in the application. It contains the fields that are submitted from the HTML form and includes validation annotations to ensure that the data is valid before it is processed by the application.

package com.example.ToDoList.TodoItem;
import jakarta.validation.constraints.NotBlank; //Importing the NotBlank annotation for validation

public class TodoItemFormData { //Form data object that matches the HTML form and its inputs for Todo items
    @NotBlank //Validation annotation to ensure the title of the to do item is not blank
    private String title; //Each todo item must have a title

    public String getTitle() { //Return the title of the task
        return title;
    }

    public void setTitle(String title) { //Set the title of the task
        this.title = title;
    }
}