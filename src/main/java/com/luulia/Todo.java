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
 * All methods that control the todos file of a certain "user". Each file represents
 * a "user".
 */
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

    /**
     * Checks if the file exists.
     *
     * @return true if the file exists, and false if it doesn't.
     */
    public boolean fileExists() {
        try {
            FileReader file = new FileReader(this.filename);
            try {
                BufferedReader bufferRead = new BufferedReader(file);
                bufferRead.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        } catch (FileNotFoundException e) {
            return false; 
        }
    }

    /**
     * Creates an id for a todo.
     *
     * @return A string which is the number of the last todo id on the file plus one.
     */
    private String createTodoId()
        throws IOException {
        try {
            ArrayList<String> data = getTodos(99);
            String lastLine = data.get(data.size() - 1);
            String[] lastLineSplitted = lastLine.split("\\s+");
            int lastId = Integer.parseInt(lastLineSplitted[0]);
            int newId = lastId + 1;

            if (newId > 9) {
                return Integer.toString(newId);
            }
            return "0" + Integer.toString(newId);
        } catch (IOException e) {
            throw new IOException("An error occurred while trying to create the file.\n");
        }
    }

    /**
     * Creates a file to store the todos.
     */
    public void createFile()
        throws  IOException {
        String str = " ";
        byte data[] = str.getBytes();
        Path path = Paths.get("./" + this.filename);

        try {
            OutputStream file = new BufferedOutputStream(
                        Files.newOutputStream(path, CREATE, APPEND));
            file.write(data, 0, data.length);
            System.out.print("File created.");
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
            FileReader file = new FileReader(this.filename);
            BufferedReader bufferRead = new BufferedReader(file);

            String currentLine;

            try {
                // Reads the file until it ends.
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
        System.out.println(this.filename + " todos:");
        for (String todo : todos ) {
            System.out.println(todo);
        }
    }

    /**
     * Adds the todo, and its id to a file.
     *
     * @param todoToAdd The todo that the user will add.
     */
    public void addTodo(String todoToAdd)
        throws IOException {
        todoToAdd = todoToAdd.trim();
        // Adds the id in front of the todo, and add a line break in the end.
        try {
            todoToAdd = createTodoId() + " " + todoToAdd + "\n";
            byte[] data = todoToAdd.getBytes();

            try {
                Path path = Paths.get("./" + this.filename);
                Files.write(path, data, APPEND);
                System.out.println("Todo added.");
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
	 * Searches for a given id, in a given list.
	 *
	 * @param ids List of todos ids.
	 * @param start First elment index in the list.
	 * @param end Last element index in the list.
	 * @param id Id to be found.
	 *
	 * @return Element's index.
	 */
	public int searchId(ArrayList<Integer> ids, int start, int end, int id) {
		if (start < end) {
			int mid = start + (end - start) / 2;
			if (id < ids.get(mid)) {
				return this.searchId(ids, start, mid, id);
			} else if (id > ids.get(mid)) {
				return this.searchId(ids, mid+1, end, id);
			} else {
				return mid;
			}
		}
		return -1;
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
    public boolean deleteTodo(String todoId)
        throws IOException {
        File file = new File(this.filename);
        String tempFilename = "temp_" + this.filename;
        File temporaryFile = new File(tempFilename);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(temporaryFile));

            String currentLine;

            // Searches in each line of the file to the given id.
            while ((currentLine = reader.readLine()) != null) {
                String idLine = currentLine.trim();
                if (idLine.startsWith(todoId)) {
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

     * @param todoId The todo's id that will be updated.
     * @param todoToAdd The todo "text" that will replace the old one.
     *
     * @return true if the temporary file was renamed correctly.
     * 
     * @throws IOException
     */
    public boolean updateTodo(String todoId, String todoToAdd)
        throws IOException {
        File file = new File(this.filename);
        String tempFilename = "temp_" + this.filename;
        File temporaryFile = new File(tempFilename);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(temporaryFile));

            String currentLine;

            // Searches in each line of the file to the given id.
            while ((currentLine = reader.readLine()) != null) {
                String idLine = currentLine.trim();
                if (idLine.startsWith(todoId)) {
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
