package com.luulia;

import java.io.IOException;

import java.util.ArrayList;

public class App {

    /**
     * Get the number of todos to be displayed.
     *
     * @param args CLI arguments.
     * @return number of todos that will be displayed.
     */
    public static int getNumberOfTodos(String[] args) {
        if (args.length >= 2) {
            String strNumberOfTodos = args[1];
            int numberOfTodos = Integer.parseInt(strNumberOfTodos);
            return numberOfTodos;
        } else {
            // Returns the base number of todos.
            return 10;
        }
    }

    /**
     * Prints a certain number of todos.
     *
     * @param args CLI arguments.
     * @param todo Todo object to retrieve the todos.
     */
    public static void getTodos(String[] args, Todo todo) {
        int numberOfTodos = getNumberOfTodos(args);
        try {
            ArrayList<String> todos = todo.getTodos(numberOfTodos);
            todo.printTodos(todos);
        } catch (IOException e) {
            System.out.println("Couldn't read the file.\n" + e);
        }

    }

    /**
     * Adds a todo in the file.
     *
     * @param args CLI arguments.
     * @param todo Todo object used to add the given cli todo.
     */
    public static void addTodo(String[] args, Todo todo) {
        String todoToAdd = "";
        for (int i = 1;i < args.length;i++) {
            todoToAdd += " " + args[i];
        }

        try {
            todo.addTodo(todoToAdd);
            System.out.println("Todo added.");
        } catch (IOException e) {
            System.out.println("Couldn't read the file.\n" + e);
        }
    }

    /**
     * Deletes the todo given the id.
     *
     * @param args CLI arguments.
     * @param todo Todo object used to delete the given todo.
     */
    public static void deleteTodo(String[] args, Todo todo) {
        String todoId = args[1];
        if (Integer.parseInt(todoId) < 10) {
            todoId = "0" + todoId;
        }

        try {
            todo.deleteTodo(todoId);
        } catch (IOException e) {
            System.out.println("Couldn't read the file.\n" + e);
        }
    }

    /**
     * Updates the todo given the id and the todo.
     *
     * @param args CLI arguments.
     * @param todo Todo object used to update the given todo.
     */
    public static void updateTodo(String[] args, Todo todo) {
        String todoId = args[1];
        if (Integer.parseInt(todoId) < 10) {
            todoId = "0" + todoId;
        }

        String todoToAdd = "";
        for (int i = 2;i < args.length;i++) {
            todoToAdd += " " + args[i];
        }

        try {
            todo.updateTodo(todoId, todoToAdd);
            System.out.println("Todo updated.");
        } catch (IOException e ) {
            System.out.println("Couldn't read the file.\n" + e);
        }
    }

    /**
     * Checks if the user wants to create a new file to store the todos.
     *
     * @param todo Todo object used to create the file.
     */
    public static void fileDoesNotExist(Todo todo) {
       try {
           todo.createFile();
       } catch (IOException e) {
           System.out.println("Error.\n" + e);
       }
    }

    /**
     * Shows the possible options for using the program.
     */
    public static void usage(int status) {
        System.out.println("Usage: luulia [FILE] [OPTION]");
        System.out.println("\nOptions:");
        System.out.println(" -g, --get\t optional: [NUMBER OF TODOS] default: 10");
        System.out.println(" -a, --add\t [TODO]\t Adds a todo");
        System.out.println(" -d, --delete\t [TODO NUMBER]\t Deletes a todo");
        System.out.println(" -u, --update\t [TODO NUMBER] [UPDATED TODO]\t Updates the todo");
        System.out.println(" -h, --help\t This help message");
        System.exit(status);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            usage(-1);
        }

        String action = args[0];

        if (action.equals("-h") || action.equals("--help")) {
            usage(0);
        }

        Todo todo = new Todo();
        if (!todo.fileExists()) {
            fileDoesNotExist(todo);
        }

        if (action.equals("-g") || action.equals("--get")) {
            getTodos(args, todo);
        } else if (action.equals("-a") || action.equals("--add")) {
            addTodo(args, todo);
        } else if (action.equals("-d") || action.equals("--delete")) {
            deleteTodo(args, todo);
        } else if (action.equals("-u") || action.equals("--update")) {
            updateTodo(args, todo);
        } else {
            System.out.println("Invalid argument.");
            usage(0);
        }
    }
}
