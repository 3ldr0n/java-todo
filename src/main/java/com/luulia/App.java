package com.luulia;

import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class App {

    /**
     * Get the number if todos to be displayed.
     *
     * @param args CLI arguments.
     * @return number of todos that will be displayed.
     */
    public static int getNumberOfTodos(String[] args) {
        if (args.length >= 3) {
            String strNumberOfTodos = args[2];
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
            System.out.println(e);
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
        for (int i = 2;i < args.length;i++) {
            todoToAdd += " " + args[i];
        }

        try {
            todo.addTodo(todoToAdd);
            System.out.println("Todo added.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Deletes the todo given the id.
     *
     * @param args CLI arguments.
     * @param todo Todo object used to delete the given todo.
     */
    public static void deleteTodo(String[] args, Todo todo) {
        String todoId = args[2];
        if (Integer.parseInt(todoId) < 10) {
            todoId = "0" + todoId;
        }

        try {
            todo.deleteTodo(todoId);
            System.out.println("Todo deleted.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Updates the todo given the id and the todo.
     *
     * @param args CLI arguments.
     * @param todo Todo object used to update the given todo.
     */
    public static void updateTodo(String[] args, Todo todo) {
        String todoId = args[2];
        if (Integer.parseInt(todoId) < 10) {
            todoId = "0" + todoId;
        }

        String todoToAdd = "";
        for (int i = 3;i < args.length;i++) {
            todoToAdd += " " + args[i];
        }

        try {
            todo.updateTodo(todoId, todoToAdd);
            System.out.println("Todo updated.");
        } catch (IOException e ) {
            System.out.println("Error.\n" + e);
        }
    }

    /**
     * Checks if the user wants to create a new file to store the todos.
     *
     * @param todo Todo object used to create the file.
     */
    public static void fileDoesNotExist(Todo todo) {
        Scanner scan = new Scanner(System.in);
        System.out.println("File doesn't exist.\nDo you want o create a new file? [y/n]");
        String option = scan.nextLine();

        if (option.toLowerCase().equals("y")) {
            System.out.println("Creating file... ");
            try {
                todo.createFile();
            } catch (IOException e) {
                System.out.println("Error.\n" + e);
            }
        } else {
            System.out.println("Finishing application");
        }
        scan.close();
    }

    /**
     * Shows the possible options for using the program.
     */
    public static void helpMessage() {
        System.out.println("Usage: luulia [FILE] [OPTION]");
        System.out.println("Options:");
        System.out.println("-G, --get\t optional: [NUMBER OF TODOS] default: 10");
        System.out.println("-A, --add\t [TODO]");
        System.out.println("-D, --delete\t [TODO NUMBER]");
        System.out.println("-U, --update\t [TODO NUMBER] [UPDATED TODO]");
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            if (args[0].equals("--help") || args[0].equals("-H")) {
                helpMessage();
            } else {
                String filename = args[0];
                Todo todo = new Todo(filename);
                try {
                    ArrayList<String> todos = todo.getTodos(10);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        } else if (args.length >= 2) {
            String filename = args[0];
            String action = args[1];

            Todo todo = new Todo(filename);
            if (todo.fileExists()) {
                if (action.equals("-G") || action.equals("--get")) {
                    getTodos(args, todo);
                } else if (action.equals("-A") || action.equals("--add")) {
                    addTodo(args, todo);
                } else if (action.equals("-D") || action.equals("--delete")) {
                    deleteTodo(args, todo);
                } else if (action.equals("-U") || action.equals("--update")) {
                    updateTodo(args, todo);
                } else {
                    System.out.println("Invalid argument.");
                    helpMessage();
                }
            } else {
                fileDoesNotExist(todo);
            }

        } else {
            System.out.println("Not enough information.");
        }
    }
}
