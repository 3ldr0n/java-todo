package com.luulia;

import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class App {

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

    public static void getTodos(String[] args, Todo todo) {
        int numberOfTodos = getNumberOfTodos(args);
        try {
            ArrayList<String> todos = todo.getTodos(numberOfTodos);
            todo.printTodos(todos);
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public static void addTodo(String[] args, Todo todo) {
        String todoToAdd = "";
        for (int i = 2;i < args.length;i++) {
            todoToAdd += " " + args[i];
        }

        try {
            todo.addTodo(todoToAdd);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            String filename = args[0];
            Todo todo = new Todo(filename);
            try {
                ArrayList<String> todos = todo.getTodos(10);
            } catch (IOException e) {
                System.out.println(e);
            }
        } else if (args.length >= 2) {
            String filename = args[0];
            String action = args[1];

            Todo todo = new Todo(filename);
            if (todo.fileExists()) {
                if (action.equals("get")) {
                    getTodos(args, todo);
                } else if (action.equals("add")) {
                    addTodo(args, todo);
                } else if (action.equals("delete")) {
                    String todoId = args[2];
                    System.out.println(todoId);
                }
            } else {
                Scanner scan = new Scanner(System.in);
                System.out.println("File doesn't exist.\nDo you want o create a new file? [y/n]");
                String option = scan.nextLine();

                if (option.toLowerCase().equals("y")) {
                    System.out.println("Creating file... ");
                    try {
                        todo.createFile();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                } else {
                    System.out.println("Finishing application");
                }
            }

        } else {
            System.out.println("Not enough information.");
        }
    }
}
