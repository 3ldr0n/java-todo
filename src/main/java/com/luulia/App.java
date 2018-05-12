package com.luulia;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;

class Todo {
    private Integer numberOfTodos;
    private String filename;

    public Todo(String username) {
        this.filename = username;
    }

    public boolean fileExists() {
        try {
            FileReader file = new FileReader(this.filename);
            BufferedReader bufferRead = new BufferedReader(file);
            return true;
        } catch (FileNotFoundException e) {
            return false; 
        }
    }

    public ArrayList<String> getTodos() {
        ArrayList<String> fileData = new ArrayList<String>();
        try {
            FileReader file = new FileReader(this.filename);
            BufferedReader bufferRead = new BufferedReader(file);

            String currentLine;

            try {
                // Reads the file until it ends.
                System.out.println(this.filename + " todos:");
                while ((currentLine = bufferRead.readLine()) != null) {
                    fileData.add(currentLine);
                    System.out.println(currentLine);
                }
            } catch (IOException e) {
                System.out.println("Error reading the file.");
            } finally {
                try {
                    file.close();
                } catch (IOException e) {
                    System.out.println("Error closing the file.");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        return fileData;

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
            if (utils.fileExists()) {
                ArrayList<String> todos = utils.getTodos();
            } else {
                System.out.println("File doesn't exist.");
            }

            if (action == "get") {
                System.out.println("Returning todos... ");
            }

        } else {
            System.out.println("Not enough information.");
        }
    }
}
