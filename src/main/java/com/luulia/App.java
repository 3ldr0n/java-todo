package com.luulia;

import java.util.Scanner;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        if (args.length == 1) {
            System.out.println("Return all the todos.");
        } else if (args.length >= 2) {
            String username = args[0];
            String action = args[1];

            Todo todo = new Todo(username);
            if (todo.fileExists()) {
                ArrayList<String> todos = todo.getTodos();
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

            if (action == "get") {
                System.out.println("Returning todos... ");
            }

        } else {
            System.out.println("Not enough information.");
        }
    }
}
