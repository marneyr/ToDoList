//TodoItemController.java
//This is the controller class for the TodoItem resource. It handles all the requests related to the TodoItem entity, such as adding, deleting, and updating items.

package com.example.ToDoList.TodoItem;

import jakarta.validation.Valid; //Import the Valid annotation for validation
import java.util.List; //Import List utility

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller; 
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*; //import the required annotation packages, such as DeleteMapping, GetMapping, PostMapping, ModelAttributes, etc.



@Controller //Controller to serve the TodoItem resource
@RequestMapping("/")
public class TodoItemController {

    private final TodoItemRepository repository; //Repository to access the TodoItem entity

    private int getNumberOfActiveItems() { //Count the number of active items in the database
        return repository.countAllByCompleted(false); //Count all the items that are not completed
    }

    public TodoItemController(TodoItemRepository repository) { //Constructor to insert the TodoItem repository into the controller
        this.repository = repository; 
    }
    

    @PostMapping
    public String addNewTodoItem(@Valid @ModelAttribute("item") TodoItemFormData formData) {  //Take the item attribute from the model via @ModelAttribute
        repository.save(new TodoItem(formData.getTitle(), false)); //Convert the TodoItemFormData to a TodoItem and save it via the repository.

        return "redirect:/"; //Redirect to the root page again, to allow the user to input the next todo item
    }

    @PutMapping("/{id}/toggle") //Annotated with PutMapping to indicate that this method will react to a HTTP PUT request
    public String toggleSelection(@PathVariable("id") Long id) { //Extract the id from the path and assign it to the id variable
        TodoItem todoItem = repository.findById(id) //Find the item in the database by using the repository
                                      .orElseThrow(() -> new TodoItemNotFoundException(id)); //If item is not found, throw an error

        todoItem.setCompleted(!todoItem.isCompleted());  //Set the completed status of the item to the opposite of what it is currently
        repository.save(todoItem); //Save the item back to the database
        return "redirect:/";  //Redirect to the root page again, to display the updated todo item
    }

    @DeleteMapping("/{id}") //DeleteMapping to indicate that this method will react to a HTTP DELETE request
    public String deleteTodoItem(@PathVariable("id") Long id) { //Extract the id from the path and assign it to the id variable
        repository.deleteById(id); //Delete the item from the database by using the repository

        return "redirect:/"; //Redirect to the root page again, to display the remaining todo item
    }

    public enum ListFilter { //3 filter options that are available at the bottom of the application
        ALL,
        ACTIVE,
        COMPLETED
    }

    @GetMapping //Endpoint for ALL filter option
    public String index(Model model) {
        addAttributesForIndex(model, ListFilter.ALL); //Display all tasks (Completed and incomplete)
        return "index";
    }

    @GetMapping("/active") //Endpoint for ACTIVE filter option
    public String indexActive(Model model) {
        addAttributesForIndex(model, ListFilter.ACTIVE); //Display only active tasks (incomplete)
        return "index";
    }

    @GetMapping("/completed") //Endpoint for COMPLETED filter option
    public String indexCompleted(Model model) {
        addAttributesForIndex(model, ListFilter.COMPLETED); //Display only completed tasks
        return "index";
    }

    private void addAttributesForIndex(Model model,
                                       ListFilter listFilter) {
        model.addAttribute("item", new TodoItemFormData()); //create an empty TodoItemFormData instance, so the form can bind to it
        model.addAttribute("filter", listFilter); //Add filter attribute in the model, so the view can highlight the correct item in the footer
        model.addAttribute("todos", getTodoItems(listFilter)); //Only display the todo items that are relevant to the selected filter
        model.addAttribute("totalNumberOfItems", repository.count()); //Determine the amount of todo items there are
        model.addAttribute("numberOfActiveItems", getNumberOfActiveItems()); //Allows the view to react to the number of active items
        model.addAttribute("numberOfCompletedItems", getNumberOfCompletedItems()); //Include number of completed items as an attribute

    }

    private int getNumberOfCompletedItems() {
        return repository.countAllByCompleted(true); //Count all the items that are completed
    }

    @DeleteMapping("/completed")
    public String deleteCompletedItems() { //Delete all completed items from the database
        List<TodoItem> items = repository.findAllByCompleted(true);
        for (TodoItem item : items) { //Iterate through the list of completed items and delete them one by one
            repository.deleteById(item.getId());
        }
        return "redirect:/"; //Redirect to the root page again, to display the remaining todo items
    }

    @PutMapping("/toggle-all") //toggle button to mark all active todo items as completed
    public String toggleAll() {
        List<TodoItem> todoItems = repository.findAll(); //Get all todo items
        for (TodoItem todoItem : todoItems) { //Iterate through the list of todo items
            todoItem.setCompleted(!todoItem.isCompleted()); //Set the completed status of the item to the opposite of what it is currently
            repository.save(todoItem); //Save the item and new completion status back to the database
        }
        return "redirect:/";
    }

    private List<TodoItemDto> getTodoItems(ListFilter filter) {
        return switch (filter) { //Use the switch statement to determine what to return based on which filter is selected
            case ALL -> convertToDto(repository.findAll()); //Return all todo items in the database
            case ACTIVE -> convertToDto(repository.findAllByCompleted(false)); //Return all active (incomplete) todo items in the database
            case COMPLETED -> convertToDto(repository.findAllByCompleted(true)); //Return all completed todo items in the database
        };
    }

    private List<TodoItemDto> convertToDto(List<TodoItem> todoItems) {
        return todoItems
                .stream()
                .map(todoItem -> new TodoItemDto(todoItem.getId(), //Get the ID of the todo item
                                                 todoItem.getTitle(), //Get the title of the todo item
                                                 todoItem.isCompleted())) //Get the completion status of the todo item
                .collect(Collectors.toList());
    }


    public static record TodoItemDto(long id, String title, boolean completed) {  //Use a java record for the DTO
    }


}