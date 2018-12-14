package com.luulia;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.APPEND;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;

/**
 * Controle the todo file of a certain user. Each user in the machine has file
 * if he ran the program at least once. The file is located at
 * ~/.local/share/luulia/username
 */
class Todo {

    private String filename;
    private Path userPath;
    private String filePath;

    public Todo() {
        setFilename(System.getProperty("user.name"));
        setUserPath(Paths.get(System.getProperty("user.home"), ".local/share/luulia/"));
        filePath = userPath.toString() + "/" + filename;
    }

    public String getFilename() {
        return filename;
    }

    public Path getUserPath() {
        return userPath;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setUserPath(Path userPath) {
        this.userPath = userPath;
    }

    /**
     * Checks if the file exists.
     *
     * @return true if the file exists, and false if it doesn't.
     */
    public boolean fileExists() {
        return Files.exists(Paths.get(filePath));
    }

    /**
     * Creates an id for a todo.
     *
     * @return A string which is the number of the last todo id on the file plus one.
     */
    private int createTodoId()
        throws IOException {
        try {
            ArrayList<String> data = getTodos(99);
            if (data.size() == 0) {
                return 0;
            }

            String lastLine = data.get(data.size() - 1);
            String[] lastLineSplitted = lastLine.split("\\s+");
            int lastId = Integer.parseInt(lastLineSplitted[0]);
            int newId = lastId + 1;

            return newId;
        } catch (IOException e) {
            throw new IOException("An error occurred while trying to create the file.\n");
        }
    }

    /**
     * Checks if the user already has a file in his path.
     */
    private boolean checkUserPath() {
        if (Files.notExists(userPath)) {
            File dir = new File(userPath.toString());
            return dir.mkdirs();
        }
        return false;
    }

    /**
     * Creates a file to store the todos.
     */
    public void createFile()
        throws  IOException {
        checkUserPath();

        String str = " ";
        byte data[] = str.getBytes();
        Path path = Paths.get(userPath.toString(), filename);

        try {
            OutputStream file = new BufferedOutputStream(
                        Files.newOutputStream(path, CREATE, APPEND));
            file.write(data, 0, data.length);
        } catch (IOException e) {
            throw new IOException("An error occoured.\n");
        }
    }

    /**
     * Gets a certain numbers of todos.
     *
     * @param numberOfTodos maximum number of todos to be returned.
     *
     * @return an ArrayList of Strings containing each line of the file.
     *
     * @throws IOException
     */
    public ArrayList<String> getTodos(int numberOfTodos)
        throws IOException {
        ArrayList<String> fileData = new ArrayList<String>();
        try {
            FileReader file = new FileReader(filePath);
            BufferedReader bufferRead = new BufferedReader(file);

            String currentLine;

            try {
                while ((currentLine = bufferRead.readLine()) != null && numberOfTodos > 0) {
                    fileData.add(currentLine);
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
            throw new IOException("File not found.");
        }

        return fileData;

    }

    /**
     * Prints all the todos inside an ArrayList.
     *
     * @param todos ArrayList of strings with the todos.
     */
    public void printTodos(ArrayList<String> todos) {
        for (String todo : todos ) {
            System.out.println(todo);
        }
    }

    /**
     * Adds the todo to the user file.
     *
     * @param todoToAdd The todo that the user will add.
     */
    public void addTodo(String todoToAdd)
        throws IOException {
        todoToAdd = todoToAdd.trim();
        try {
            todoToAdd = Integer.toString(createTodoId()) + " " + todoToAdd + "\n";
            byte[] data = todoToAdd.getBytes();

            try {
                Path path = Paths.get(filePath);
                Files.write(path, data, APPEND);
                ArrayList<String> todos = getTodos(10);
                printTodos(todos);
            } catch (IOException e) {
                System.out.println("Error opening the file.\n" + e);
            }
        } catch (IOException e) {
            throw new IOException("An error occurred.\n");
        }
    }

    /**
     * Creates a temporary file to put the the data without the deleted todo.
     *
     * @param todoId a String that represents the todo id on the file.
     *
     * @return true if the file was correctly renamed.
     *
     * @throws IOException
     */
    public boolean deleteTodo(int todoId)
        throws IOException {
        File file = new File(filePath);
        String tempFilename = "temp_" + filename;
        File temporaryFile = new File(tempFilename);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(temporaryFile));

            String currentLine;

            // Searches in each line of the file to the given id.
            while ((currentLine = reader.readLine()) != null) {
                String idLine = currentLine.trim();
                if (idLine.startsWith(Integer.toString(todoId))) {
                    continue;
                }
                writer.write(currentLine + "\n");
            }

            writer.close();
            reader.close();
            boolean success = temporaryFile.renameTo(file);
            return success;
        } catch (FileNotFoundException e) {
            throw new IOException("Error while trying to open the file for reading.\n");
        }
    }

    /**
     * Updates the todo given its id.
     *
     * @param todoId The todo's id that will be updated.
     * @param todoToAdd The todo "text" that will replace the old one.
     *
     * @return true if the temporary file was renamed correctly.
     * 
     * @throws IOException
     */
    public boolean updateTodo(int todoId, String todoToAdd)
        throws IOException {
        File file = new File(filePath);
        String tempFilename = "temp_" + filename;
        File temporaryFile = new File(tempFilename);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(temporaryFile));

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String idLine = currentLine.trim();
                if (idLine.startsWith(Integer.toString(todoId))) {
                    currentLine = todoId + todoToAdd;
                }
                writer.write(currentLine + "\n");
            }

            writer.close();
            reader.close();
            boolean success = temporaryFile.renameTo(file);
            return success;
        } catch (FileNotFoundException e) {
            throw new IOException("Error while trying to open the file for reading.\n");
        }
    }
}
