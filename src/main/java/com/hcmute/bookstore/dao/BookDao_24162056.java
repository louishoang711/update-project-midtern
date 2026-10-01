package com.hcmute.bookstore.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.hcmute.bookstore.model.Book_24162056;

public interface BookDao_24162056 {
    List<Book_24162056> findPage(int offset, int limit) throws SQLException;
    Optional<Book_24162056> findById(int bookId) throws SQLException;
    long count() throws SQLException;
    List<Book_24162056> findByAuthor(int authorId, int offset, int limit) throws SQLException;
    long countByAuthor(int authorId) throws SQLException;
    int insert(Book_24162056 book, List<Integer> authorIds) throws SQLException;
    void update(Book_24162056 book, List<Integer> authorIds) throws SQLException;
    void delete(int bookId) throws SQLException;
}
