package main;

import model.Book;
import model.Member;
import service.LibraryService;

public class Main {
    public static void main(String[] args) {

        LibraryService library = new LibraryService();

        // Add books
        library.addBook(new Book(1, "Java Basics", "John Smith"));
        library.addBook(new Book(2, "OOP Concepts", "Jane Doe"));

        // Add members
        library.addMember(new Member(1, "Alice"));
        library.addMember(new Member(2, "Bob"));

        // Borrow book
        library.borrowBook(1, 1);

        // Return book
        library.returnBook(1);
    }
}