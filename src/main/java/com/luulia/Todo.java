package com.luulia;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.APPEND;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;

class Todo {
    private String filename;

    public Todo(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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

    public void createFile() {
        String str = " ";
        byte data[] = str.getBytes();
        Path path = Paths.get("./" + this.filename);

        try {
            OutputStream file = new BufferedOutputStream(
                        Files.newOutputStream(path, CREATE, APPEND));
            file.write(data, 0, data.length);
            System.out.print("File created.");
        } catch (IOException e) {
            System.out.println("An error occoured.\n" + e);
        }
    }

    public ArrayList<String> getTodos(int numberOfTodos) {
        ArrayList<String> fileData = new ArrayList<String>();
        try {
            FileReader file = new FileReader(this.filename);
            BufferedReader bufferRead = new BufferedReader(file);

            String currentLine;

            try {
                // Reads the file until it ends.
                System.out.println(this.filename + " todos:");
                while ((currentLine = bufferRead.readLine()) != null && numberOfTodos > 0) {
                    fileData.add(currentLine);
                    System.out.println(currentLine);
                    numberOfTodos--;
                }
            } catch (IOException e) {
                System.out.println("Error reading the file.\n" + e);
            } finally {
                try {
                    file.close();
                } catch (IOException e) {
                    System.out.println("Error closing the file.\n" + e);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found." + e);
        }

        return fileData;

    }

    public void addTodo() {
        System.out.println("Adding todos... ");
    }
}
