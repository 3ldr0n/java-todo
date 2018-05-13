package com.luulia;

import java.util.Scanner;
import java.util.ArrayList;

public class App {

    public static int getNumberOfTodos(String[] args) {
        for (String arg : args) {
            System.out.println(arg);
        }
        if (args.length >= 3) {
            String strNumberOfTodos = args[2];
            int numberOfTodos = Integer.parseInt(strNumberOfTodos);
            return numberOfTodos;
        } else {
            // Returns the base number of todos.
            return 10;
        }
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            System.out.println("Return all the todos.");
        } else if (args.length >= 2) {
            String filename = args[0];
            String action = args[1];

            Todo todo = new Todo(filename);
            if (todo.fileExists()) {
                if (action.equals("get")) {
                    int numberOfTodos = getNumberOfTodos(args);
                    ArrayList<String> todos = todo.getTodos(numberOfTodos);
                }
            } else {
                Scanner scan = new Scanner(System.in);
                System.out.println("File doesn't exist.\nDo you want o create a new file? [y/n]");
                String option = scan.nextLine();

                if (option.toLowerCase().equals("y")) {
                    System.out.println("Creating file... ");
                    todo.createFile();
                } else {
                    System.out.println("Finishing application");
                }
            }

        } else {
            System.out.println("Not enough information.");
        }
    }
}
