package model;

import java.time.LocalDate;

public class Transcation {
    private int bookId;
    private int memberId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private boolean returned;

    public Transcation(int bookId, int memberId, LocalDate borrowDate, LocalDate dueDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returned = false;
    }

    public int getBookId() { return bookId; }
    public int getMemberId() { return memberId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isReturned() { return returned; }

    public void markReturned() {
        this.returned = true;
    }
}