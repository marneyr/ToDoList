//TodoItemRepository.java

package com.example.ToDoList.TodoItem; 

import java.util.List; //Import List utiliity
import org.springframework.data.jpa.repository.JpaRepository; //Allows todoitem entities to be stored in the database

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> { //Repository interface for TodoItem entity
    int countAllByCompleted(boolean completed); //Method to count all the completed or incompleted items
    
    List<TodoItem> findAllByCompleted(boolean completed); //Allows for the ability to retrieve completed or incompleted items from the database
}