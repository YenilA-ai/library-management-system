package main;

import model.Book;
import model.Member;
import service.LibraryService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        LibraryService library = new LibraryService();
        Scanner scanner = new Scanner(System.in);

        library.addBook(new Book(1, "Java Basics", "John Smith"));
        library.addBook(new Book(2, "OOP Concepts", "Jane Doe"));

        library.addMember(new Member(1, "Alice"));
        library.addMember(new Member(2, "Bob"));

        int choice = -1;

        do {
            System.out.println("\n=== Library Menu ===");
            System.out.println("1. Show all books");
            System.out.println("2. Show all members");
            System.out.println("3. Borrow book");
            System.out.println("4. Return book");
            System.out.println("5. Show borrowed books");
            System.out.println("6. Show overdue books");
            System.out.println("7. Search book");
            System.out.println("0. Exit");

            System.out.print("Enter choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // fix input issue

                switch (choice) {
                    case 1:
                        library.showAllBooks();
                        break;

                    case 2:
                        library.showAllMembers();
                        break;

                    case 3:
                        handleBorrow(scanner, library);
                        break;

                    case 4:
                        handleReturn(scanner, library);
                        break;

                    case 5:
                        library.showBorrowedBooks();
                        break;

                    case 6:
                        library.showOverdueBooks();
                        break;

                    case 7:
                        System.out.print("Enter keyword: ");
                        String keyword = scanner.nextLine();
                        library.searchBook(keyword);
                        break;

                    case 0:
                        System.out.println("Exiting system...");
                        break;

                    default:
                        System.out.println("Invalid choice");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Enter numbers only.");
                scanner.nextLine();
            }

        } while (choice != 0);

        scanner.close();
    }

    private static void handleBorrow(Scanner scanner, LibraryService library) {
        try {
            System.out.print("Enter Book ID: ");
            int bookId = scanner.nextInt();

            System.out.print("Enter Member ID: ");
            int memberId = scanner.nextInt();

            library.borrowBook(bookId, memberId);

        } catch (InputMismatchException e) {
            System.out.println("Invalid input");
            scanner.nextLine();
        }
    }

    private static void handleReturn(Scanner scanner, LibraryService library) {
        try {
            System.out.print("Enter Book ID: ");
            int bookId = scanner.nextInt();

            library.returnBook(bookId);

        } catch (InputMismatchException e) {
            System.out.println("Invalid input");
            scanner.nextLine();
        }
    }
}