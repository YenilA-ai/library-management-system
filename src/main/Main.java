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
        frame.setLayout(new BorderLayout(10, 10));

        Color bgColor = new Color(30, 30, 30);
        Color panelColor = new Color(45, 45, 45);
        Color btnColor = new Color(70, 130, 180);

        frame.getContentPane().setBackground(bgColor);

        JLabel title = new JLabel("Library Management System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        frame.add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 15, 15));
        panel.setBackground(panelColor);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton addBook = createButton("Add Book", btnColor);
        JButton deleteBook = createButton("Delete Book", btnColor);
        JButton addMember = createButton("Add Member", btnColor);
        JButton viewBooks = createButton("View Books", btnColor);
        JButton viewMembers = createButton("View Members", btnColor);
        JButton borrowBook = createButton("Borrow Book", btnColor);
        JButton returnBook = createButton("Return Book", btnColor);
        JButton searchBook = createButton("Search Book", btnColor);
        JButton showBorrowed = createButton("Borrowed Books", btnColor);

        panel.add(addBook);
        panel.add(deleteBook);
        panel.add(addMember);
        panel.add(viewBooks);
        panel.add(viewMembers);
        panel.add(borrowBook);
        panel.add(returnBook);
        panel.add(searchBook);
        panel.add(showBorrowed);

        frame.add(panel, BorderLayout.CENTER);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setBackground(new Color(20, 20, 20));
        outputArea.setForeground(Color.GREEN);
        outputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(650, 220));
        frame.add(scrollPane, BorderLayout.SOUTH);

        // ================= ADD BOOK =================
        addBook.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Book ID"));

                for (Book b : library.getBooks()) {
                    if (b.getId() == id) {
                        JOptionPane.showMessageDialog(frame, "Book ID already exists");
                        return;
                    }
                }

                String titleInput = JOptionPane.showInputDialog("Title");
                String author = JOptionPane.showInputDialog("Author");

                library.addBook(new Book(id, titleInput, author));
                outputArea.append("✔ Book added\n");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        });

        // ================= DELETE BOOK =================
        deleteBook.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Book ID to delete"));

                library.getBooks().removeIf(b -> b.getId() == id);
                outputArea.append("✔ Book deleted (if existed)\n");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        });

        // ================= ADD MEMBER =================
        addMember.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Member ID"));

                for (Member m : library.getMembers()) {
                    if (m.getId() == id) {
                        JOptionPane.showMessageDialog(frame, "Member ID exists");
                        return;
                    }
                }

                String name = JOptionPane.showInputDialog("Name");

                library.addMember(new Member(id, name));
                outputArea.append("✔ Member added\n");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        });

        // ================= VIEW =================
        viewBooks.addActionListener(e -> {
            outputArea.setText("=== Books ===\n");
            for (Book b : library.getBooks()) {
                outputArea.append(b.getId() + " | " + b.getTitle() +
                        " | Available: " + b.isAvailable() + "\n");
            }
        });

        viewMembers.addActionListener(e -> {
            outputArea.setText("=== Members ===\n");
            for (Member m : library.getMembers()) {
                outputArea.append(m.getId() + " | " + m.getName() + "\n");
            }
        });

        // ================= BORROW =================
        borrowBook.addActionListener(e -> {
            try {
                int bookId = Integer.parseInt(JOptionPane.showInputDialog("Book ID"));
                int memberId = Integer.parseInt(JOptionPane.showInputDialog("Member ID"));

                library.borrowBook(bookId, memberId);
                outputArea.append("✔ Borrowed\n");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        });

        // ================= RETURN =================
        returnBook.addActionListener(e -> {
            try {
                int bookId = Integer.parseInt(JOptionPane.showInputDialog("Book ID"));

                library.returnBook(bookId);
                outputArea.append("✔ Returned\n");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        });

        // ================= SEARCH =================
        searchBook.addActionListener(e -> {
            String keyword = JOptionPane.showInputDialog("Keyword");

            outputArea.setText("=== Search ===\n");
            for (Book b : library.getBooks()) {
                if (b.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                    outputArea.append(b.getId() + " | " + b.getTitle() + "\n");
                }
            }
        });

        // ================= BORROWED =================
        showBorrowed.addActionListener(e -> {
            outputArea.setText("=== Borrowed ===\n");
            library.getTransactions().stream()
                    .filter(t -> !t.isReturned())
                    .forEach(t -> outputArea.append(
                            "Book: " + t.getBookId() +
                                    " | Due: " + t.getDueDate() + "\n"
                    ));
        });

        frame.setVisible(true);
    }

    private static JButton createButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return button;
    }
}