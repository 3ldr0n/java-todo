package com.luulia;

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
import java.nio.file.StandardOpenOption;

import java.util.ArrayList;

/**
 * Control the todo file of a certain user. Each user in the machine has file
 * if he ran the program at least once. The file is located at
 * ~/.local/share/luulia/
 *
 * @author Edison Neto
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
     *
     * @throws IOException If it can't read the file.
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
            throw new IOException("An error occurred while trying to create the file.\n" + e);
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
     *
     * @throws IOException If it can't read the file.
     */
    public void createFile() throws IOException {
        checkUserPath();

        byte data[] = " ".getBytes();
        Path path = Paths.get(userPath.toString(), filename);

        try {
            OutputStream file = new BufferedOutputStream(
                        Files.newOutputStream(path, StandardOpenOption.CREATE,
                                              StandardOpenOption.APPEND));
            file.write(data, 0, data.length);
            file.close();
        } catch (IOException e) {
            throw new IOException("An error occoured.\n" + e);
        }
    }

    /**
     * Gets a certain numbers of todos.
     *
     * @param numberOfTodos maximum number of todos to be returned.
     *
     * @return an ArrayList of Strings containing each line of the file.
     *
     * @throws FileNotFoundException If it can't read the file.
     */
    public ArrayList<String> getTodos(int numberOfTodos) throws FileNotFoundException {
        ArrayList<String> fileData = new ArrayList<String>();
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(filePath));

            String currentLine;

            try {
                while ((currentLine = buffer.readLine()) != null && numberOfTodos > 0) {
                    fileData.add(currentLine);
                    numberOfTodos--;
                }
            } catch (IOException e) {
                System.out.println("Error reading the file.\n" + e);
            } finally {
                try {
                    buffer.close();
                } catch (IOException e) {
                    System.out.println("Error closing the file.\n" + e);
                }
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found." + e);
        }

        return fileData;
    }

    /**
     * Prints all the todos inside an ArrayList.
     *
     * @param todos ArrayList of strings with the todos.
     */
    public void printTodos(ArrayList<String> todos) {
        todos.forEach(System.out::println);
    }

    /**
     * Adds the todo to the user file.
     *
     * @param todoToAdd The todo that the user will add.
     *
     * @throws IOException If it can't read the file.
     */
    public void addTodo(String todoToAdd) throws IOException {
        todoToAdd = todoToAdd.trim();
        try {
            todoToAdd = Integer.toString(createTodoId()) + " " + todoToAdd + "\n";
            byte[] data = todoToAdd.getBytes();

            try {
                Path path = Paths.get(filePath);
                Files.write(path, data, StandardOpenOption.APPEND);
                ArrayList<String> todos = getTodos(10);
                printTodos(todos);
            } catch (IOException e) {
                System.out.println("Error opening the file.\n" + e);
            }
        } catch (IOException e) {
            throw new IOException("An error occurred.\n" + e);
        }
    }

    /**
     * Creates a temporary file to put the the data without the deleted todo.
     *
     * @param todoId a String that represents the todo id on the file.
     *
     * @return true if the file was correctly renamed.
     *
     * @throws IOException If it can't open the file.
     */
    public boolean deleteTodo(int todoId) throws IOException {
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
                    continue;
                }
                writer.write(currentLine + "\n");
            }

            writer.close();
            reader.close();
            boolean success = temporaryFile.renameTo(file);
            return success;
        } catch (FileNotFoundException e) {
            throw new IOException("Error while trying to open the file for reading.\n" + e);
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
     * @throws IOException If it can't open the file.
     */
    public boolean updateTodo(int todoId, String todoToAdd) throws IOException {
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
            throw new IOException("Error while trying to open the file for reading.\n" + e);
        }
    }
}
