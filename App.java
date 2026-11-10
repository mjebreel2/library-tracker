import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class App {
    private static final Path BOOKS_FILE = Path.of("books.json");

    public static void main(String[] args) throws IOException {
        printBanner();

        if (args.length == 0) {
            return;
        }

        if (args.length == 1 && args[0].equals("list")) {
            listBooks();
            return;
        }

        if (args.length != 3 || !args[0].equals("add")) {
            System.out.println("Usage: java App add \"Title\" \"Author\"");
            System.out.println("       java App list");
            return;
        }

        if (args[1].isBlank() || args[2].isBlank()) {
            System.out.println("Enter both a title and an author.");
            return;
        }

        addBook(args[1], args[2]);
        System.out.println("Book added.");
    }

    static void printBanner() {
        System.out.println("Library Tracker");
    }

    static void listBooks() throws IOException {
        String books = Files.readString(BOOKS_FILE).strip();

        if (books.matches("\\[\\s*\\]")) {
            System.out.println("No books saved.");
        } else {
            System.out.println(books);
        }
    }

    static void addBook(String title, String author) throws IOException {
        String json = Files.readString(BOOKS_FILE).strip();
        if (!json.startsWith("[") || !json.endsWith("]")) {
            throw new IOException("books.json must contain a JSON array.");
        }

        String savedBooks = json.substring(1, json.length() - 1).strip();
        String newBook = "{\"title\": " + quote(title)
                + ", \"author\": " + quote(author) + "}";

        if (!savedBooks.isEmpty()) {
            savedBooks += ",\n  ";
        }

        Files.writeString(BOOKS_FILE, "[\n  " + savedBooks + newBook + "\n]\n");
    }

    static String quote(String text) {
        StringBuilder result = new StringBuilder("\"");
        for (char ch : text.toCharArray()) {
            if (ch == '"' || ch == '\\') {
                result.append('\\').append(ch);
            } else if (ch < ' ') {
                result.append(String.format("\\u%04x", (int) ch));
            } else {
                result.append(ch);
            }
        }
        return result.append('"').toString();
    }
}