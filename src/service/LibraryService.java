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
    private final String MEMBER_FILE = "members.txt";

    public LibraryService() {
        loadBooks();
        loadMembers();
    }

    // ===== LOAD ======
    private void loadBooks() {
        try (BufferedReader br = new BufferedReader(new FileReader(BOOK_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                books.add(new Book(
                        Integer.parseInt(data[0]),
                        data[1],
                        data[2]
                ));
            }
        } catch (Exception e) {
        }
    }

    private void loadMembers() {
        try (BufferedReader br = new BufferedReader(new FileReader(MEMBER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                members.add(new Member(
                        Integer.parseInt(data[0]),
                        data[1]
                ));
            }
        } catch (Exception e) {
        }
    }

    // ======= SAVE =======
    private void saveBooks() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(BOOK_FILE))) {
            for (Book b : books) {
                pw.println(b.getId() + "," + b.getTitle() + "," + b.getAuthor());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveMembers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(MEMBER_FILE))) {
            for (Member m : members) {
                pw.println(m.getId() + "," + m.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ====== ADD ========
    public void addBook(Book book) {
        for (Book b : books) {
            if (b.getId() == book.getId()) return; // prevent duplicate
        }
        books.add(book);
        saveBooks();
    }

    public void addMember(Member member) {
        for (Member m : members) {
            if (m.getId() == member.getId()) return;
        }
        members.add(member);
        saveMembers();
    }

    // ====== DELETE ==========
    public void deleteBook(int id) {
        books.removeIf(b -> b.getId() == id);
        saveBooks();
    }

    public void deleteMember(int id) {
        members.removeIf(m -> m.getId() == id);
        saveMembers();
    }

    // ======= GET ========
    public List<Book> getBooks() {
        return books;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    // ======= FIND =======
    private Book findBook(int id) {
        for (Book b : books) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    // ===== BORROW ======
    public void borrowBook(int bookId, int memberId) {
        Book book = findBook(bookId);

        if (book != null && book.isAvailable()) {
            book.setAvailable(false);

            transactions.add(new Transaction(
                    bookId,
                    memberId,
                    LocalDate.now(),
                    LocalDate.now().plusDays(7)
            ));

            saveBooks(); 
        }
    }

    // ======== RETURN ========
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

            saveBooks();
        }
    }
}