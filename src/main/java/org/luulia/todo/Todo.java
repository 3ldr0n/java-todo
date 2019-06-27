package org.luulia.todo;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Control the to do file of a certain user. Each user in the machine has file
 * if he ran the program at least once. The file is located at
 * ~/.local/share/luulia/
 *
 * @author Edison Neto
 */
public class Todo {

    private String filename;
    private Path userPath;
    private final String filePath;

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

    private void setFilename(String filename) {
        this.filename = filename;
    }

    private void setUserPath(Path userPath) {
        this.userPath = userPath;
    }

    /**
     * Checks if the file exists.
     *
     * @return true if the file exists, and false if it doesn't.
     */
    public boolean fileExists() {
        return Paths.get(filePath).toFile().exists();
    }

    /**
     * Creates an id for a to do.
     *
     * @return A string which is the number of the last to do id on the file plus one.
     * @throws IOException If it can't read the file.
     */
    private int createTodoId() throws IOException {
        try {
            List<String> data = getTodos(99);
            if (data.isEmpty()) {
                return 0;
            }

            String lastLine = data.get(data.size() - 1);
            String[] lastLineSplit = lastLine.split("\\s+");
            int lastId = Integer.parseInt(lastLineSplit[0]);
            return lastId + 1;
        } catch (IOException e) {
            throw new IOException("An error occurred while trying to create the file.\n" + e);
        }
    }

    /**
     * Checks if the user already has a file in his path.
     */
    private void checkUserPath() {
        if (!userPath.toFile().exists()) {
            File dir = new File(userPath.toString());
            dir.mkdirs();
        }
    }

    /**
     * Creates a file to store the to dos.
     *
     * @throws IOException If it can't read the file.
     */
    public void createFile() throws IOException {
        checkUserPath();

        byte[] data = " ".getBytes();
        Path path = Paths.get(userPath.toString(), filename);

        try (OutputStream file = new BufferedOutputStream(
                Files.newOutputStream(path, StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND))
        ) {
            file.write(data, 0, data.length);
        } catch (IOException e) {
            throw new IOException("An error occurred.\n" + e);
        }
    }

    /**
     * Gets a certain numbers of to dos.
     *
     * @param numberOfTodos maximum number of to dos to be returned.
     * @return an ArrayList of Strings containing each line of the file.
     * @throws FileNotFoundException If it can't read the file.
     */
    public List<String> getTodos(int numberOfTodos) throws FileNotFoundException {
        List<String> fileData = new ArrayList<>();
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
     * Prints all the to dos inside an ArrayList.
     *
     * @param todos ArrayList of strings with the to dos.
     */
    public void printTodos(List<String> todos) {
        todos.forEach(System.out::println);
    }

    /**
     * Adds the to do to the user file.
     *
     * @param todoToAdd The to do that the user will add.
     * @throws IOException If it can't read the file.
     */
    public void addTodo(String todoToAdd) throws IOException {
        todoToAdd = todoToAdd.trim();
        try {
            todoToAdd = createTodoId() + " " + todoToAdd + "\n";
            byte[] data = todoToAdd.getBytes();

            writeToTodo(data);
        } catch (IOException e) {
            throw new IOException("An error occurred.\n" + e);
        }
    }

    private void writeToTodo(byte[] data) {
        try {
            Path path = Paths.get(filePath);
            Files.write(path, data, StandardOpenOption.APPEND);
            List<String> todos = getTodos(10);
            printTodos(todos);
        } catch (IOException e) {
            System.out.println("Error opening the file.\n" + e);
        }
    }

    /**
     * Creates a temporary file to put the the data without the deleted to do.
     *
     * @param todoId a String that represents the todo id on the file.
     * @return true if the file was correctly renamed.
     * @throws IOException If it can't open the file.
     */
    public boolean deleteTodo(int todoId) throws IOException {
        final File file = new File(filePath);
        final String tempFilename = "temp_" + filename;
        final File temporaryFile = new File(tempFilename);

        try (
                final BufferedReader reader = new BufferedReader(new FileReader(file));
                final BufferedWriter writer = new BufferedWriter(new FileWriter(temporaryFile))

        ) {
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String idLine = currentLine.trim();
                if (idLine.startsWith(Integer.toString(todoId))) {
                    continue;
                }
                writer.write(currentLine + "\n");
            }

            return temporaryFile.renameTo(file);
        } catch (FileNotFoundException e) {
            throw new IOException("Error while trying to open the file for reading.\n" + e);
        }
    }

    /**
     * Updates the to do given its id.
     *
     * @param todoId    The to do's id that will be updated.
     * @param todoToAdd The to do "text" that will replace the old one.
     * @return true if the temporary file was renamed correctly.
     * @throws IOException If it can't open the file.
     */
    public boolean updateTodo(int todoId, String todoToAdd) throws IOException {
        File file = new File(filePath);
        String tempFilename = "temp_" + filename;
        File temporaryFile = new File(tempFilename);

        try (
                BufferedReader reader = new BufferedReader(new FileReader(file));
                BufferedWriter writer = new BufferedWriter(new FileWriter(temporaryFile))

        ) {
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String idLine = currentLine.trim();
                if (idLine.startsWith(Integer.toString(todoId))) {
                    currentLine = todoId + todoToAdd;
                }
                writer.write(currentLine + "\n");
            }

            return temporaryFile.renameTo(file);
        } catch (FileNotFoundException e) {
            throw new IOException("Error while trying to open the file for reading.\n" + e);
        }
    }
}
