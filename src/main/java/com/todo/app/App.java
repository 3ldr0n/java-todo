package com.todo.app;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

class Todo {
    private Integer numberOfTodos;
    private String filename;

    public Todo(String username) {
        this.filename = username;
    }

    public void getTodos() {
        try {
            FileReader fileData = new FileReader(this.filename);
            BufferedReader bufferRead = new BufferedReader(fileData);

            String currentLine;

            try {
                // Reads the file until it ends.
                System.out.println(this.filename + " todos:");
                while ((currentLine = bufferRead.readLine()) != null) {
                    System.out.println(currentLine);
                }
            } catch (IOException e) {
                System.out.println("Error reading the file.");
            } finally {
                try {
                    fileData.close();
                } catch (IOException e) {
                    System.out.println("Error closing the file.");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public void addTodo() {
        System.out.println("Adding todos... ");
    }
}

public class App {
    public static void main(String[] args) {
        if (args.length == 1) {
            System.out.println("Return all the todos.");
        } else if (args.length >= 2) {
            String username = args[0];
            String action = args[1];

            Todo utils = new Todo(username);
            utils.getTodos();

            if (action == "get") {
                System.out.println("Returning todos... ");
                utils.getTodos();
            }

        } else {
            System.out.println("Not enough information.");
        }
    }
}
