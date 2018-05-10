import java.io.*;

class Todo {
    private Integer numberOfTodos;
    private String filename;

    public Todo(String username) {
        this.filename = username;
    }

    public void getTodos() {
        try {
            System.out.println("Data");
            File todosFile = new File(this.filename);
            //BufferedReader fileData = new BufferedReader(new FileReader(todosFile));
            FileReader fileData = new FileReader(todosFile);
            try {
                System.out.println(fileData.read());
            } catch (IOException e) {
                System.out.println("Error reading the file.");
            }
            // fileData.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
}

public class Main{
    public static void main(String[] args) {
        System.out.println(args.length);
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
