package main;

import model.Book;
import model.Member;
import service.LibraryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Main {

    private static LibraryService library = new LibraryService();
    private static JTable table;
    private static DefaultTableModel tableModel;

    public static void main(String[] args) {

        JFrame frame = new JFrame("Library Management System");
        frame.setSize(700, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // ===== TITLE =====
        JLabel title = new JLabel("Library Management System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        frame.add(title, BorderLayout.NORTH);

        // ===== BUTTON PANEL =====
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

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

        frame.add(panel, BorderLayout.WEST);

        // ===== TABLE =====
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);

        frame.add(scroll, BorderLayout.CENTER);

        // ===== ADD BOOK =====
        addBook.addActionListener(e -> {
            JTextField id = new JTextField();
            JTextField titleField = new JTextField();
            JTextField author = new JTextField();

            Object[] fields = {
                    "Book ID:", id,
                    "Title:", titleField,
                    "Author:", author
            };

            if (JOptionPane.showConfirmDialog(null, fields, "Add Book",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                try {
                    library.addBook(new Book(
                            Integer.parseInt(id.getText()),
                            titleField.getText(),
                            author.getText()
                    ));
                    JOptionPane.showMessageDialog(frame, "Book added");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input");
                }
            }
        });

        // ===== DELETE BOOK =====
        deleteBook.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter Book ID");

            try {
                library.deleteBook(Integer.parseInt(input));
                JOptionPane.showMessageDialog(frame, "Book deleted");

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
                    JOptionPane.showMessageDialog(frame, "Member added");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input");
                }
            }
        });

        // ===== VIEW BOOKS =====
        viewBooks.addActionListener(e -> {
            tableModel.setRowCount(0);
            tableModel.setColumnIdentifiers(new String[]{"ID", "Title", "Author", "Available"});

            for (Book b : library.getBooks()) {
                tableModel.addRow(new Object[]{
                        b.getId(),
                        b.getTitle(),
                        b.getAuthor(),
                        b.isAvailable()
                });
            }
        });

        // ===== VIEW MEMBERS =====
        viewMembers.addActionListener(e -> {
            tableModel.setRowCount(0);
            tableModel.setColumnIdentifiers(new String[]{"ID", "Name"});

            for (Member m : library.getMembers()) {
                tableModel.addRow(new Object[]{
                        m.getId(),
                        m.getName()
                });
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
                    JOptionPane.showMessageDialog(frame, "Book borrowed");

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
                JOptionPane.showMessageDialog(frame, "Book returned");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        });

        // ===== SEARCH =====
        searchBook.addActionListener(e -> {
            String keyword = JOptionPane.showInputDialog("Enter keyword");

            tableModel.setRowCount(0);
            tableModel.setColumnIdentifiers(new String[]{"ID", "Title"});

            for (Book b : library.getBooks()) {
                if (b.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                    tableModel.addRow(new Object[]{
                            b.getId(),
                            b.getTitle()
                    });
                }
            }
        });

        // ===== BORROWED =====
        showBorrowed.addActionListener(e -> {
            tableModel.setRowCount(0);
            tableModel.setColumnIdentifiers(new String[]{"Book ID", "Due Date"});

            library.getTransactions().stream()
                    .filter(t -> !t.isReturned())
                    .forEach(t ->
                            tableModel.addRow(new Object[]{
                                    t.getBookId(),
                                    t.getDueDate()
                            })
                    );
        });

        frame.setVisible(true);
    }
}