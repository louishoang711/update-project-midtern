package com.hcmute.bookstore.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hcmute.bookstore.config.DatabaseConnection_24162056;
import com.hcmute.bookstore.dao.BookDao_24162056;
import com.hcmute.bookstore.model.Author_24162056;
import com.hcmute.bookstore.model.Book_24162056;

public class JdbcBookDao_24162056 implements BookDao_24162056 {
    private static final String BOOK_COLUMNS = """
            b.bookid, b.isbn, b.title, b.publisher, b.price, b.description,
            b.publish_date, b.cover_image, b.quantity
            """;
    private static final String SELECT_PAGE = """
            SELECT %s
            FROM dbo.books b
            ORDER BY b.bookid
            OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
            """.formatted(BOOK_COLUMNS);
    private static final String SELECT_BY_ID = """
            SELECT %s
            FROM dbo.books b
            WHERE b.bookid = ?
            """.formatted(BOOK_COLUMNS);
    private static final String SELECT_BY_AUTHOR = """
            SELECT %s
            FROM dbo.books b
            INNER JOIN dbo.book_author ba ON ba.bookid = b.bookid
            WHERE ba.author_id = ?
            ORDER BY b.bookid
            OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
            """.formatted(BOOK_COLUMNS);
    private static final String INSERT_BOOK = """
            INSERT INTO dbo.books
                (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
    private static final String UPDATE_BOOK = """
            UPDATE dbo.books
            SET isbn = ?, title = ?, publisher = ?, price = ?, description = ?,
                publish_date = ?, cover_image = ?, quantity = ?
            WHERE bookid = ?
            """;

    @Override
    public List<Book_24162056> findPage(int offset, int limit) throws SQLException {
        try (Connection connection = DatabaseConnection_24162056.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_PAGE)) {
            statement.setInt(1, offset);
            statement.setInt(2, limit);
            return readBooks(connection, statement);
        }
    }

    @Override
    public Optional<Book_24162056> findById(int bookId) throws SQLException {
        try (Connection connection = DatabaseConnection_24162056.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, bookId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) return Optional.empty();
                Book_24162056 book = mapBook(resultSet);
                loadRelations(connection, book);
                return Optional.of(book);
            }
        }
    }

    @Override
    public long count() throws SQLException {
        try (Connection connection = DatabaseConnection_24162056.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM dbo.books");
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getLong(1);
        }
    }

    @Override
    public List<Book_24162056> findByAuthor(int authorId, int offset, int limit) throws SQLException {
        try (Connection connection = DatabaseConnection_24162056.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_AUTHOR)) {
            statement.setInt(1, authorId);
            statement.setInt(2, offset);
            statement.setInt(3, limit);
            return readBooks(connection, statement);
        }
    }

    @Override
    public long countByAuthor(int authorId) throws SQLException {
        try (Connection connection = DatabaseConnection_24162056.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM dbo.book_author WHERE author_id = ?")) {
            statement.setInt(1, authorId);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getLong(1);
            }
        }
    }

    @Override
    public int insert(Book_24162056 book, List<Integer> authorIds) throws SQLException {
        try (Connection connection = DatabaseConnection_24162056.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int bookId;
                try (PreparedStatement statement = connection.prepareStatement(
                        INSERT_BOOK, Statement.RETURN_GENERATED_KEYS)) {
                    bindBook(statement, book);
                    statement.executeUpdate();
                    try (ResultSet keys = statement.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("Không lấy được mã sách.");
                        bookId = keys.getInt(1);
                    }
                }
                replaceAuthors(connection, bookId, authorIds);
                connection.commit();
                return bookId;
            } catch (SQLException | RuntimeException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    @Override
    public void update(Book_24162056 book, List<Integer> authorIds) throws SQLException {
        try (Connection connection = DatabaseConnection_24162056.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement statement = connection.prepareStatement(UPDATE_BOOK)) {
                    bindBook(statement, book);
                    statement.setInt(9, book.getBookId());
                    if (statement.executeUpdate() != 1) throw new SQLException("Không tìm thấy sách cần cập nhật.");
                }
                replaceAuthors(connection, book.getBookId(), authorIds);
                connection.commit();
            } catch (SQLException | RuntimeException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    @Override
    public void delete(int bookId) throws SQLException {
        try (Connection connection = DatabaseConnection_24162056.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM dbo.books WHERE bookid = ?")) {
            statement.setInt(1, bookId);
            statement.executeUpdate();
        }
    }

    private List<Book_24162056> readBooks(Connection connection, PreparedStatement statement) throws SQLException {
        List<Book_24162056> books = new ArrayList<>();
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) books.add(mapBook(resultSet));
        }
        for (Book_24162056 book : books) loadRelations(connection, book);
        return books;
    }

    private void loadRelations(Connection connection, Book_24162056 book) throws SQLException {
        String authorsSql = """
                SELECT a.author_id, a.author_name, a.date_of_birth
                FROM dbo.author a
                INNER JOIN dbo.book_author ba ON ba.author_id = a.author_id
                WHERE ba.bookid = ?
                ORDER BY a.author_name
                """;
        List<Author_24162056> authors = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(authorsSql)) {
            statement.setInt(1, book.getBookId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Author_24162056 author = new Author_24162056();
                    author.setAuthorId(resultSet.getInt("author_id"));
                    author.setAuthorName(resultSet.getString("author_name"));
                    Date birthDate = resultSet.getDate("date_of_birth");
                    author.setDateOfBirth(birthDate == null ? null : birthDate.toLocalDate());
                    authors.add(author);
                }
            }
        }
        book.setAuthors(authors);
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) FROM dbo.rating WHERE bookid = ?")) {
            statement.setInt(1, book.getBookId());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                book.setReviewCount(resultSet.getLong(1));
            }
        }
    }

    private void replaceAuthors(Connection connection, int bookId, List<Integer> authorIds) throws SQLException {
        try (PreparedStatement delete = connection.prepareStatement(
                "DELETE FROM dbo.book_author WHERE bookid = ?")) {
            delete.setInt(1, bookId);
            delete.executeUpdate();
        }
        try (PreparedStatement insert = connection.prepareStatement(
                "INSERT INTO dbo.book_author (bookid, author_id) VALUES (?, ?)")) {
            for (Integer authorId : authorIds) {
                insert.setInt(1, bookId);
                insert.setInt(2, authorId);
                insert.addBatch();
            }
            insert.executeBatch();
        }
    }

    private void bindBook(PreparedStatement statement, Book_24162056 book) throws SQLException {
        if (book.getIsbn() == null) statement.setNull(1, Types.INTEGER); else statement.setInt(1, book.getIsbn());
        statement.setString(2, book.getTitle());
        statement.setString(3, book.getPublisher());
        if (book.getPrice() == null) statement.setNull(4, Types.DECIMAL); else statement.setBigDecimal(4, book.getPrice());
        statement.setString(5, book.getDescription());
        if (book.getPublishDate() == null) statement.setNull(6, Types.DATE); else statement.setDate(6, Date.valueOf(book.getPublishDate()));
        statement.setString(7, book.getCoverImage());
        if (book.getQuantity() == null) statement.setNull(8, Types.INTEGER); else statement.setInt(8, book.getQuantity());
    }

    private Book_24162056 mapBook(ResultSet resultSet) throws SQLException {
        Book_24162056 book = new Book_24162056();
        book.setBookId(resultSet.getInt("bookid"));
        book.setIsbn(resultSet.getObject("isbn", Integer.class));
        book.setTitle(resultSet.getString("title"));
        book.setPublisher(resultSet.getString("publisher"));
        book.setPrice(resultSet.getBigDecimal("price"));
        book.setDescription(resultSet.getString("description"));
        Date publishDate = resultSet.getDate("publish_date");
        book.setPublishDate(publishDate == null ? null : publishDate.toLocalDate());
        book.setCoverImage(resultSet.getString("cover_image"));
        book.setQuantity(resultSet.getObject("quantity", Integer.class));
        return book;
    }
}
