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

    // ===== ADD BOOK =====
    public void addBook(Book book) {
        books.add(book);
    }

    // ===== DELETE BOOK (FIXED NAME) =====
    public void deleteBook(int id) {
        books.removeIf(book -> book.getId() == id);
    }

    // ===== ADD MEMBER =====
    public void addMember(Member member) {
        members.add(member);
    }

    // ===== GETTERS =====
    public List<Book> getBooks() {
        return books;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    // ===== FIND BOOK =====
    private Book findBook(int id) {
        for (Book b : books) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    // ===== BORROW =====
    public void borrowBook(int bookId, int memberId) {
        Book book = findBook(bookId);

        if (book != null && book.isAvailable()) {
            book.setAvailable(false);

            Transaction t = new Transaction(
                    bookId,
                    memberId,
                    LocalDate.now(),
                    LocalDate.now().plusDays(7)
            );

            transactions.add(t);
        }
    }

    // ===== RETURN =====
    public void returnBook(int bookId) {
        Book book = findBook(bookId);

        if (book != null) {
            book.setAvailable(true);

            for (Transaction t : transactions) {
                if (t.getBookId() == bookId && !t.isReturned()) {
                    t.markReturned();
                    break;
                }
            }
        }
    }
}