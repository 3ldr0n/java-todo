package org.luulia;

import org.luulia.todo.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class App {

    private static final String DEFAULT_ERROR_MESSAGE = "Couldn't read the file.\n";
    private static final Logger log = LoggerFactory.getLogger(App.class);

    /**
     * Get the number of to dos to be displayed.
     *
     * @param args CLI arguments.
     * @return number of to dos that will be displayed.
     */
    private static int getNumberOfTodos(String[] args) {
        if (args.length >= 2) {
            try {
                return Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                log.warn("Invalid number, using default value.");
                return 10;
            }
        } else {
            return 10;
        }
    }

    /**
     * Prints a certain number of to dos.
     *
     * @param args CLI arguments.
     * @param todo To do object to retrieve the to dos.
     */
    private static void getTodos(String[] args, Todo todo) {
        int numberOfTodos = getNumberOfTodos(args);
        try {
            List<String> todos = todo.getTodos(numberOfTodos);
            todo.printTodos(todos);
        } catch (IOException e) {
            log.error(DEFAULT_ERROR_MESSAGE, e);
        }
    }

    /**
     * Adds a to do in the file.
     *
     * @param args CLI arguments.
     * @param todo To do object used to add the given cli to do.
     */
    private static void addTodo(String[] args, Todo todo) {
        final var todoToAdd = new StringBuilder();
        for (var i = 1; i < args.length; i++) {
            todoToAdd.append(args[i]);
        }

        try {
            System.out.println(todoToAdd.toString());
            todo.addTodo(todoToAdd.toString());
            System.out.println("Todo added.");
        } catch (IOException e) {
            log.error(DEFAULT_ERROR_MESSAGE, e);
        }
    }

    /**
     * Deletes the to do given the id.
     *
     * @param args CLI arguments.
     * @param todo To do object used to delete the given to do.
     */
    private static void deleteTodo(String[] args, Todo todo) {
        try {
            int todoId = Integer.parseInt(args[1]);
            todo.deleteTodo(todoId);
        } catch (IOException e) {
            log.error(DEFAULT_ERROR_MESSAGE, e);
        } catch (NumberFormatException e) {
            log.error("Invalid value.");
        }
    }

    /**
     * Updates the to do given the id and the to do.
     *
     * @param args CLI arguments.
     * @param todo To do object used to update the given to do.
     */
    private static void updateTodo(String[] args, Todo todo) {
        try {
            int todoId = Integer.parseInt(args[1]);
            String todoToAdd = "";
            for (int i = 2; i < args.length; i++) {
                todoToAdd += " " + args[i];
            }

            todo.updateTodo(todoId, todoToAdd);
            System.out.println("Todo updated.");
        } catch (IOException e) {
            log.error(DEFAULT_ERROR_MESSAGE, e);
        } catch (NumberFormatException e) {
            log.error("Invalid value.");
        }
    }

    /**
     * Checks if the user wants to create a new file to store the todos.
     *
     * @param todo To do object used to create the file.
     */
    private static void fileDoesNotExist(Todo todo) {
        try {
            todo.createFile();
        } catch (IOException e) {
            log.error("Error.\n", e);
        }
    }

    /**
     * Shows the possible options for using the program.
     *
     * @param status Status code to exit.
     */
    private static void usage(int status) {
        System.out.println("Usage: luulia [OPTION]");
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

        switch (action) {
            case "-g":
            case "--get":
                getTodos(args, todo);
                break;
            case "-a":
            case "--add":
                addTodo(args, todo);
                break;
            case "-d":
            case "--delete":
                deleteTodo(args, todo);
                break;
            case "-u":
            case "--update":
                updateTodo(args, todo);
                break;
            default:
                log.error("Invalid argument.");
                usage(-1);
        }
    }
}
