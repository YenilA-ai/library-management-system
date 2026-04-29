package main;

import model.Book;
import model.Member;
import service.LibraryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Main {

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "1234";

    private static LibraryService library = new LibraryService();
    private static JTable table;
    private static DefaultTableModel tableModel;

    public static void main(String[] args) {

        if (!showStyledLogin()) {
            JOptionPane.showMessageDialog(null, "Access Denied");
            System.exit(0);
        }

        JFrame frame = new JFrame("Library Management System");
        frame.setSize(750, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Library Management System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        frame.add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JButton addBook = createButton("Add Book");
        JButton deleteBook = createButton("Delete Book");
        JButton addMember = createButton("Add Member");
        JButton deleteMember = createButton("Delete Member");
        JButton viewBooks = createButton("View Books");
        JButton viewMembers = createButton("View Members");
        JButton borrowBook = createButton("Borrow Book");
        JButton returnBook = createButton("Return Book");
        JButton searchBook = createButton("Search Book");
        JButton showBorrowed = createButton("Borrowed Books");

        panel.add(addBook);
        panel.add(deleteBook);
        panel.add(addMember);
        panel.add(deleteMember);
        panel.add(viewBooks);
        panel.add(viewMembers);
        panel.add(borrowBook);
        panel.add(returnBook);
        panel.add(searchBook);
        panel.add(showBorrowed);

        frame.add(panel, BorderLayout.WEST);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

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

        // ===== DELETE MEMBER =====
        deleteMember.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter Member ID");

            try {
                library.deleteMember(Integer.parseInt(input));
                JOptionPane.showMessageDialog(frame, "Member deleted");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input");
            }
        });

        // ===== VIEW BOOKS =====
        viewBooks.addActionListener(e -> {
            tableModel.setRowCount(0);
            tableModel.setColumnIdentifiers(new String[]{"ID", "Title", "Author", "Available"});

            for (Book b : library.getBooks()) {
                tableModel.addRow(new Object[]{
                        b.getId(), b.getTitle(), b.getAuthor(), b.isAvailable()
                });
            }
        });

        // ===== VIEW MEMBERS =====
        viewMembers.addActionListener(e -> {
            tableModel.setRowCount(0);
            tableModel.setColumnIdentifiers(new String[]{"ID", "Name"});

            for (Member m : library.getMembers()) {
                tableModel.addRow(new Object[]{
                        m.getId(), m.getName()
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
                    tableModel.addRow(new Object[]{b.getId(), b.getTitle()});
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
                                    t.getBookId(), t.getDueDate()
                            })
                    );
        });

        frame.setVisible(true);
    }

    // ===== BUTTON STYLE =====
    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return button;
    }

    // ===== LOGIN =====
    private static boolean showStyledLogin() {

        JFrame frame = new JFrame("Login");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Library Login", JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 40));

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(70, 130, 180));
        loginBtn.setForeground(Color.WHITE);

        JLabel message = new JLabel("", JLabel.CENTER);
        message.setForeground(Color.RED);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        panel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(passLabel, gbc);

        gbc.gridx = 1;
        panel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        panel.add(loginBtn, gbc);

        gbc.gridy++;
        panel.add(message, gbc);

        frame.add(panel);
        frame.setVisible(true);

        final boolean[] success = {false};

        loginBtn.addActionListener(e -> {
            if (userField.getText().equals(ADMIN_USER) &&
                new String(passField.getPassword()).equals(ADMIN_PASS)) {

                success[0] = true;
                frame.dispose();
            } else {
                message.setText("Invalid credentials");
            }
        });

        while (frame.isDisplayable()) {
            try { Thread.sleep(100); } catch (Exception ignored) {}
        }

        return success[0];
    }
}