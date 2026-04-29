package main;

import model.Book;
import model.Member;
import service.LibraryService;

import javax.swing.*;
import java.awt.*;

public class Main {

    private static LibraryService library = new LibraryService();
    private static JTextArea outputArea;

    public static void main(String[] args) {

        JFrame frame = new JFrame("Library Management System");
        frame.setSize(650, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addBook = new JButton("Add Book");
        JButton deleteBook = new JButton("Delete Book");
        JButton addMember = new JButton("Add Member");
        JButton viewBooks = new JButton("View Books");
        JButton viewMembers = new JButton("View Members");
        JButton borrowBook = new JButton("Borrow Book");
        JButton returnBook = new JButton("Return Book");
        JButton searchBook = new JButton("Search Book");
        JButton showBorrowed = new JButton("Borrowed Books");

        panel.add(addBook);
        panel.add(deleteBook);
        panel.add(addMember);
        panel.add(viewBooks);
        panel.add(viewMembers);
        panel.add(borrowBook);
        panel.add(returnBook);
        panel.add(searchBook);
        panel.add(showBorrowed);

        frame.add(panel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        frame.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // ===== ADD BOOK =====
        addBook.addActionListener(e -> {
            JTextField id = new JTextField();
            JTextField title = new JTextField();
            JTextField author = new JTextField();

            Object[] fields = {
                    "Book ID:", id,
                    "Title:", title,
                    "Author:", author
            };

            if (JOptionPane.showConfirmDialog(null, fields, "Add Book",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                try {
                    library.addBook(new Book(
                            Integer.parseInt(id.getText()),
                            title.getText(),
                            author.getText()
                    ));
                    outputArea.setText("Book added successfully\n");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input");
                }
            }
        });

        // ===== DELETE BOOK =====
        deleteBook.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter Book ID to delete");

            try {
                int id = Integer.parseInt(input);
                library.deleteBook(id);
                outputArea.setText("Book deleted successfully\n");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        });

        // ===== ADD MEMBER =====
        addMember.addActionListener(e -> {
            JTextField id = new JTextField();
            JTextField name = new JTextField();

            Object[] fields = {
                    "Member ID:", id,
                    "Name:", name
            };

            if (JOptionPane.showConfirmDialog(null, fields, "Add Member",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                try {
                    library.addMember(new Member(
                            Integer.parseInt(id.getText()),
                            name.getText()
                    ));
                    outputArea.setText("Member added successfully\n");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input");
                }
            }
        });

        // ===== VIEW BOOKS =====
        viewBooks.addActionListener(e -> {
            outputArea.setText("=== Books ===\n");
            for (Book b : library.getBooks()) {
                outputArea.append(b.getId() + " - " + b.getTitle() +
                        " - Available: " + b.isAvailable() + "\n");
            }
        });

        // ===== VIEW MEMBERS =====
        viewMembers.addActionListener(e -> {
            outputArea.setText("=== Members ===\n");
            for (Member m : library.getMembers()) {
                outputArea.append(m.getId() + " - " + m.getName() + "\n");
            }
        });

        // ===== BORROW =====
        borrowBook.addActionListener(e -> {
            JTextField bookId = new JTextField();
            JTextField memberId = new JTextField();

            Object[] fields = {
                    "Book ID:", bookId,
                    "Member ID:", memberId
            };

            if (JOptionPane.showConfirmDialog(null, fields, "Borrow Book",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                try {
                    library.borrowBook(
                            Integer.parseInt(bookId.getText()),
                            Integer.parseInt(memberId.getText())
                    );
                    outputArea.setText("Book borrowed successfully\n");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input");
                }
            }
        });

        // ===== RETURN =====
        returnBook.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter Book ID");

            try {
                library.returnBook(Integer.parseInt(input));
                outputArea.setText("Book returned successfully\n");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        });

        // ===== SEARCH =====
        searchBook.addActionListener(e -> {
            String keyword = JOptionPane.showInputDialog("Enter keyword");

            outputArea.setText("=== Search Results ===\n");
            for (Book b : library.getBooks()) {
                if (b.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                    outputArea.append(b.getId() + " - " + b.getTitle() + "\n");
                }
            }
        });

        // ===== BORROWED =====
        showBorrowed.addActionListener(e -> {
            outputArea.setText("=== Borrowed Books ===\n");
            library.getTransactions().stream()
                    .filter(t -> !t.isReturned())
                    .forEach(t ->
                            outputArea.append("Book ID: " + t.getBookId() +
                                    " | Due: " + t.getDueDate() + "\n")
                    );
        });

        frame.setVisible(true);
    }
}