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

    public void addBook(Book book) {
        books.add(book);
        System.out.println("Book added: " + book.getTitle());
    }

    public void addMember(Member member) {
        members.add(member);
        System.out.println("Member added: " + member.getName());
    }

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

        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(14);

        Transaction t = new Transaction(bookId, memberId, borrowDate, dueDate);
        transactions.add(t);

        System.out.println("Borrowed. Due date: " + dueDate);
    }

    public void returnBook(int bookId) {
        for (Transaction t : transactions) {
            if (t.getBookId() == bookId && !t.isReturned()) {
                t.markReturned();

                Book book = findBook(bookId);
                if (book != null) {
                    book.setAvailable(true);
                }

                System.out.println("Returned successfully");
                return;
            }
        }

        System.out.println("No active transaction found");
    }

    public void showAllBooks() {
        System.out.println("\nAll Books:");
        for (Book b : books) {
            System.out.println(b.getId() + " - " + b.getTitle() + " - Available: " + b.isAvailable());
        }
    }

    public void showAllMembers() {
        System.out.println("\nAll Members:");
        for (Member m : members) {
            System.out.println(m.getId() + " - " + m.getName());
        }
    }

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

    public void showOverdueBooks() {
        System.out.println("\nOverdue Books:");
        for (Transaction t : transactions) {
            if (!t.isReturned() && t.getDueDate().isBefore(LocalDate.now())) {
                System.out.println("Book ID: " + t.getBookId() +
                        ", Member ID: " + t.getMemberId());
            }
        }
    }

    // NEW: Search book by title
    public void searchBook(String keyword) {
        System.out.println("\nSearch Results:");
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(b.getId() + " - " + b.getTitle());
            }
        }
    }

    private Book findBook(int id) {
        for (Book b : books) {
            if (b.getId() == id) return b;
        }
        return null;
    }
}