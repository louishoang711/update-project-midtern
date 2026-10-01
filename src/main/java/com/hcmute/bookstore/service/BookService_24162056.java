package com.hcmute.bookstore.service;

import java.util.List;
import java.util.Optional;

import com.hcmute.bookstore.model.Book_24162056;

public interface BookService_24162056 {
    List<Book_24162056> getPage(int pageNumber, int pageSize);
    Optional<Book_24162056> getById(int bookId);
    long countBooks();
    List<Book_24162056> getByAuthor(int authorId, int page, int size);
    long countByAuthor(int authorId);
    int create(Book_24162056 book, List<Integer> authorIds);
    void update(Book_24162056 book, List<Integer> authorIds);
    void delete(int bookId);
}
