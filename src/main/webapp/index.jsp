<%@ page import="com.inigne.bms.model.Book, com.inigne.bms.service.BookService, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Book Management System</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        form {
            margin-bottom: 20px;
        }
        input[type="text"], input[type="submit"] {
            padding: 8px;
            margin: 5px;
        }
        button {
            padding: 5px 10px;
            margin: 2px;
            cursor: pointer;
        }
    </style>
    <script>
        function addBook() {
            const name = prompt("Enter the book name:");
            const isbn = prompt("Enter the book ISBN:");
            if (name && isbn) {
                console.log("Sending request to add book:", { name, isbn });
                fetch("/api/books", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({ name, isbn })
                }).then(response => {
                    console.log("Response status:", response.status);
                    if (response.status === 201) {
                        location.reload();
                    } else {
                        alert("Failed to add the book.");
                    }
                }).catch(error => {
                    console.error("Error:", error);
                    alert("Failed to add the book.");
                });
            }
        }

        function updateBook(id) {
            const name = prompt("Enter the new name:");
            const isbn = prompt("Enter the new ISBN:");
            if (name && isbn) {
                console.log("Sending request to update book:", { id, name, isbn });
                fetch(`/api/books/${id}`, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({ name, isbn })
                }).then(response => {
                    console.log("Response status:", response.status);
                    if (response.ok) {
                        location.reload();
                        alert("Failed to update the book.");
                    }
                }).catch(error => {
                    console.error("Error:", error);
                    alert("Failed to update the book.");
                });
            }
        }

        function deleteBook(id) {
            if (confirm("Are you sure you want to delete this book?")) {
                console.log("Sending request to delete book with ID:", id);
                fetch(`/api/books/${id}`, {
                    method: "DELETE"
                }).then(response => {
                    console.log("Response status:", response.status);
                    if (response.status === 204) {
                        location.reload();
                    } else {
                        alert("Failed to delete the book.");
                    }
                }).catch(error => {
                    console.error("Error:", error);
                    alert("Failed to delete the book.");
                });
            }
        }
    </script>
</head>
<body>
<h1>Book Management System</h1>

<h2>Add New Book</h2>
<button onclick="addBook()">Add Book</button>

<h2>Book List</h2>
<table>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>ISBN</th>
        <th>Actions</th>
    </tr>
    <%
        List<Book> books = BookService.getAllBooks();
        for (Book book : books) {
    %>
    <tr>
        <td><%= book.getId() %></td>
        <td><%= book.getName() %></td>
        <td><%= book.getIsbn() %></td>
        <td>
            <button onclick="updateBook(<%= book.getId() %>)">Update</button>
            <button onclick="deleteBook(<%= book.getId() %>)">Delete</button>
        </td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>