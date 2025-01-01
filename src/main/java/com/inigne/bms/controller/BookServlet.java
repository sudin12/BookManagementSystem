package com.inigne.bms.controller;


import com.google.gson.Gson;
import com.inigne.bms.model.Book;
import com.inigne.bms.service.BookService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/books/*")
public class BookServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(BookServlet.class.getName());
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Book> books = BookService.getAllBooks();
                LOGGER.info("Retrieved all books: " + books.size());
                response.getWriter().write(gson.toJson(books));
            } else {
                String[] splits = pathInfo.split("/");
                int id = Integer.parseInt(splits[1]);
                Book book = BookService.getBook(id);

                if (book != null) {
                    LOGGER.info("Retrieved book with ID: " + id);
                    response.getWriter().write(gson.toJson(book));
                } else {
                    sendError(response, HttpServletResponse.SC_NOT_FOUND, "Book not found");
                }
            }
        } catch (NumberFormatException e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing GET request", e);
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        try {
            Book book = gson.fromJson(request.getReader(), Book.class);

            if (book.getName() == null || book.getIsbn() == null) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Name and ISBN are required");
                return;
            }

            BookService.addBook(book);
            LOGGER.info("Added new book: " + book);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson(book));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing POST request", e);
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add book");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Book ID is required");
                return;
            }

            String[] splits = pathInfo.split("/");
            int id = Integer.parseInt(splits[1]);
            Book updatedBook = gson.fromJson(request.getReader(), Book.class);
            updatedBook.setId(id);

            if (updatedBook.getName() == null || updatedBook.getIsbn() == null) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Name and ISBN are required");
                return;
            }

            BookService.updateBook(updatedBook);
            LOGGER.info("Updated book with ID: " + id);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(updatedBook));
        } catch (NumberFormatException e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing PUT request", e);
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update book");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Book ID is required");
                return;
            }

            String[] splits = pathInfo.split("/");
            int id = Integer.parseInt(splits[1]);
            BookService.deleteBook(id);
            LOGGER.info("Deleted book with ID: " + id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing DELETE request", e);
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete book");
        }
    }

    private void sendError(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}