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

        // Sample data

        library.addMember(new Member(1, "Alice"));
        library.addMember(new Member(2, "Bob"));
        library.addMember(new Member(3, "Yenil"));

        // Frame
        JFrame frame = new JFrame("Library Management System");
        frame.setSize(600, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Colors
        Color bgColor = new Color(30, 30, 30);
        Color panelColor = new Color(45, 45, 45);
        Color btnColor = new Color(70, 130, 180);
        Color textColor = Color.WHITE;

        frame.getContentPane().setBackground(bgColor);

        // Title
        JLabel title = new JLabel("Library Management System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(textColor);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        frame.add(title, BorderLayout.NORTH);

        // Button panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 15, 15));
        panel.setBackground(panelColor);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton viewBooks = createButton("View Books", btnColor);
        JButton viewMembers = createButton("View Members", btnColor);
        JButton borrowBook = createButton("Borrow Book", btnColor);
        JButton returnBook = createButton("Return Book", btnColor);
        JButton searchBook = createButton("Search Book", btnColor);
        JButton showBorrowed = createButton("Borrowed Books", btnColor);

        panel.add(viewBooks);
        panel.add(viewMembers);
        panel.add(borrowBook);
        panel.add(returnBook);
        panel.add(searchBook);
        panel.add(showBorrowed);

        frame.add(panel, BorderLayout.CENTER);

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setBackground(new Color(20, 20, 20));
        outputArea.setForeground(Color.GREEN);
        outputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(600, 220));

        frame.add(scrollPane, BorderLayout.SOUTH);

        // Actions

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

        borrowBook.addActionListener(e -> {
            try {
                int bookId = Integer.parseInt(JOptionPane.showInputDialog("Book ID"));
                int memberId = Integer.parseInt(JOptionPane.showInputDialog("Member ID"));

                library.borrowBook(bookId, memberId);
                outputArea.append("✔ Book borrowed successfully\n");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        });

        returnBook.addActionListener(e -> {
            try {
                int bookId = Integer.parseInt(JOptionPane.showInputDialog("Book ID"));

                library.returnBook(bookId);
                outputArea.append("✔ Book returned successfully\n");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        });

        searchBook.addActionListener(e -> {
            String keyword = JOptionPane.showInputDialog("Enter keyword");

            outputArea.setText("=== Search Results ===\n");
            for (Book b : library.getBooks()) {
                if (b.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                    outputArea.append(b.getId() + " | " + b.getTitle() + "\n");
                }
            }
        });

        showBorrowed.addActionListener(e -> {
            outputArea.setText("=== Borrowed Books ===\n");
            library.getTransactions().stream()
                    .filter(t -> !t.isReturned())
                    .forEach(t -> outputArea.append(
                            "Book: " + t.getBookId() +
                                    " | Due: " + t.getDueDate() + "\n"
                    ));
        });

        frame.setVisible(true);
    }

    // Button styling
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