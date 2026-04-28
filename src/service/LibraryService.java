package service;

import model.Book;
import model.Member;
import model.Transaction;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibraryService {

    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

    private final String BOOK_FILE = "books.txt";

    public LibraryService() {
        loadBooks();
    }

    // ================= ADD =================
    public void addBook(Book book) {
        books.add(book);
        saveBooks();
        System.out.println("Book added: " + book.getTitle());
    }

    public void addMember(Member member) {
        members.add(member);
        System.out.println("Member added: " + member.getName());
    }

    // ================= BORROW =================
    public void borrowBook(int bookId, int memberId) {
        Book book = findBook(bookId);

        if (book == null) {
            System.out.println("Book not found");
            return;
        }

        if (!book.isAvailable()) {
            System.out.println("Book already borrowed");
            return;
        }

        book.setAvailable(false);
        saveBooks();

        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(14);

        transactions.add(new Transaction(bookId, memberId, borrowDate, dueDate));

        System.out.println("Borrowed. Due: " + dueDate);
    }

    // ================= RETURN =================
    public void returnBook(int bookId) {
        for (Transaction t : transactions) {
            if (t.getBookId() == bookId && !t.isReturned()) {
                t.markReturned();

                Book book = findBook(bookId);
                if (book != null) {
                    book.setAvailable(true);
                    saveBooks();
                }

                System.out.println("Returned");
                return;
            }
        }

        System.out.println("No active transaction");
    }

    // ================= DISPLAY =================
    public void showAllBooks() {
        for (Book b : books) {
            System.out.println(b.getId() + " - " + b.getTitle());
        }
    }

    public void showAllMembers() {
        for (Member m : members) {
            System.out.println(m.getId() + " - " + m.getName());
        }
    }

    public void showBorrowedBooks() {
        for (Transaction t : transactions) {
            if (!t.isReturned()) {
                System.out.println("Book: " + t.getBookId() + " Due: " + t.getDueDate());
            }
        }
    }

    // ================= SEARCH =================
    public void searchBook(String keyword) {
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(b.getTitle());
            }
        }
    }

    // ================= FIND =================
    private Book findBook(int id) {
        for (Book b : books) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    // ================= FILE SAVE =================
    private void saveBooks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOK_FILE))) {
            for (Book b : books) {
                writer.write(b.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving books");
        }
    }

    private void loadBooks() {
        File file = new File(BOOK_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(BOOK_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                books.add(Book.fromFileString(line));
            }
        } catch (IOException e) {
            System.out.println("Error loading books");
        }
    }

    // ================= IMPORTANT FIX =================
    public List<Book> getBooks() {
        return books;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}