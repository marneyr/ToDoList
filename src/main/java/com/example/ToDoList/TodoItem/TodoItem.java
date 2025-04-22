//TodoItem.java
//Todo Item entity class


package com.example.ToDoList.TodoItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank; //Import the NotBlank annotation from the Jakarta Validation API

@Entity //TodoItem entity class
public class TodoItem { //Create a class for the TodoItem
    @Id
    @GeneratedValue
    private Long id; //Each todo item is assigned an Id

    @NotBlank
    private String title; //Title of the task (which cannot be blank)

    private boolean completed; //Completion status of the task (which is false by default)

    //Constructors
    public TodoItem() {} //Default constructor

    public TodoItem(String title, boolean completed) { //Convenience constructor
        setTitle(title);
        setCompleted(completed);
    }
    
    // Getters and setters
    public Long getId() { //Return the Id of the task
        return id;
    }
    
    public void setId(Long id) { //Set the Id of the task
        if (id < 0) { //Checks if the ID is less than 0
            throw new IllegalArgumentException("ID must be greater than 0"); //Id must be greater than 0
        }
        this.id = id;
    }
    public String getTitle() { //Return the title of the task
        return title;
    }

    public void setTitle(String title) { //Set the title of the task
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty"); //Error is thrown if the title is null or empty, as every task has to have a title
        }
        this.title = title;
    }
    
    
    public boolean isCompleted() { //Return the completion status of the task
        return completed;
    }
    
    public void setCompleted(boolean completed) { //Set the completion status of the task
        this.completed = completed; //Set the completion status of the task
    }

    

}