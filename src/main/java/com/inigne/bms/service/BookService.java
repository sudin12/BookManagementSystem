package com.inigne.bms.service;


import com.inigne.bms.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookService {
    private static final String URL = "jdbc:mysql://localhost:3306/book_management_system";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String isbn = rs.getString("isbn");
                books.add(new Book(id, name, isbn));
            }
        }
        return books;
    }

    public static void addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (name, isbn) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, book.getName());
            stmt.setString(2, book.getIsbn());
            stmt.executeUpdate();

            // Retrieve the generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getInt(1)); // Set the generated ID
                } else {
                    throw new SQLException("Failed to retrieve generated ID.");
                }
            }
        }
    }

    public static void updateBook(Book book) throws SQLException {
        String sql = "UPDATE books SET name = ?, isbn = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getName());
            stmt.setString(2, book.getIsbn());
            stmt.setInt(3, book.getId());
            stmt.executeUpdate();
        }
    }

    public static void deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public static Book getBook(int id) throws SQLException {
        Book book = null;
        String sql = "SELECT * FROM books WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String isbn = rs.getString("isbn");
                book = new Book(id, name, isbn);
            }
        }
        return book;
    }
}