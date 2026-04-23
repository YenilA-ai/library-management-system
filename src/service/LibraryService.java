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

        System.out.println("Book borrowed successfully. Due date: " + dueDate);
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

                System.out.println("Book returned successfully");
                return;
            }
        }

        System.out.println("No active transaction found");
    }

    // Helper method
    private Book findBook(int id) {
        for (Book b : books) {
            if (b.getId() == id) {
                return b;
            }
        }
        return null;
    }
}