package service;

import model.Book;
import model.Member;
import model.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibraryService {

    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

    // Add Book
    public void addBook(Book book) {
        books.add(book);
        System.out.println("Book added: " + book.getTitle());
    }

    // Add Member
    public void addMember(Member member) {
        members.add(member);
        System.out.println("Member added: " + member.getName());
    }

    // Borrow Book
    public void borrowBook(int bookId, int memberId) {
        Book book = findBook(bookId);

        if (book == null) {
            System.out.println("Book not found");
            return;
        }

        if (!book.isAvailable()) {
            System.out.println("Book is already borrowed");
            return;
        }

        book.setAvailable(false);

        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(14);

        Transaction transaction = new Transaction(bookId, memberId, borrowDate, dueDate);
        transactions.add(transaction);

        System.out.println("Book borrowed. Due date: " + dueDate);
    }

    // Return Book
    public void returnBook(int bookId) {
        for (Transaction t : transactions) {
            if (t.getBookId() == bookId && !t.isReturned()) {
                t.markReturned();

                Book book = findBook(bookId);
                if (book != null) {
                    book.setAvailable(true);
                }

                System.out.println("Book returned");
                return;
            }
        }

        System.out.println("No active transaction found");
    }

    // Show all books
    public void showAllBooks() {
        System.out.println("\nAll Books:");
        for (Book b : books) {
            System.out.println(b.getId() + " - " + b.getTitle() + " - Available: " + b.isAvailable());
        }
    }

    // Show all members
    public void showAllMembers() {
        System.out.println("\nAll Members:");
        for (Member m : members) {
            System.out.println(m.getId() + " - " + m.getName());
        }
    }

    // Show borrowed books
    public void showBorrowedBooks() {
        System.out.println("\nBorrowed Books:");
        for (Transaction t : transactions) {
            if (!t.isReturned()) {
                System.out.println("Book ID: " + t.getBookId() +
                        ", Member ID: " + t.getMemberId() +
                        ", Due: " + t.getDueDate());
            }
        }
    }

    // Show overdue books
    public void showOverdueBooks() {
        System.out.println("\nOverdue Books:");
        for (Transaction t : transactions) {
            if (!t.isReturned() && t.getDueDate().isBefore(LocalDate.now())) {
                System.out.println("Book ID: " + t.getBookId() +
                        ", Member ID: " + t.getMemberId());
            }
        }
    }

    // Helper
    private Book findBook(int id) {
        for (Book b : books) {
            if (b.getId() == id) {
                return b;
            }
        }
        return null;
    }
}