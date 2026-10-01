package com.hcmute.bookstore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.hcmute.bookstore.dao.BookDao_24162056;
import com.hcmute.bookstore.model.Book_24162056;
import com.hcmute.bookstore.service.BookService_24162056;
import com.hcmute.bookstore.service.impl.BookServiceImpl_24162056;

class ProjectStructureTest_24162056 {
    @Test
    void serviceDelegatesToDataAccessLayer() {
        BookDao_24162056 fakeDao = new BookDao_24162056() {
            @Override
            public List<Book_24162056> findPage(int offset, int limit) {
                Book_24162056 book = new Book_24162056();
                book.setBookId(1);
                book.setTitle("Sach mau");
                return List.of(book);
            }

            @Override
            public Optional<Book_24162056> findById(int bookId) {
                return Optional.empty();
            }

            @Override
            public long count() throws SQLException {
                return 1;
            }

            @Override public List<Book_24162056> findByAuthor(int authorId, int offset, int limit) { return List.of(); }
            @Override public long countByAuthor(int authorId) { return 0; }
            @Override public int insert(Book_24162056 book, List<Integer> authorIds) { return 1; }
            @Override public void update(Book_24162056 book, List<Integer> authorIds) { }
            @Override public void delete(int bookId) { }
        };

        BookService_24162056 service = new BookServiceImpl_24162056(fakeDao);

        assertEquals(1, service.getPage(1, 3).size());
        assertEquals("Sach mau", service.getPage(1, 3).get(0).getTitle());
        assertEquals(1, service.countBooks());
    }

    @Test
    void serviceRejectsInvalidPagination() {
        BookDao_24162056 unusedDao = new BookDao_24162056() {
            @Override public List<Book_24162056> findPage(int offset, int limit) { return List.of(); }
            @Override public Optional<Book_24162056> findById(int bookId) { return Optional.empty(); }
            @Override public long count() { return 0; }
            @Override public List<Book_24162056> findByAuthor(int authorId, int offset, int limit) { return List.of(); }
            @Override public long countByAuthor(int authorId) { return 0; }
            @Override public int insert(Book_24162056 book, List<Integer> authorIds) { return 1; }
            @Override public void update(Book_24162056 book, List<Integer> authorIds) { }
            @Override public void delete(int bookId) { }
        };
        BookService_24162056 service = new BookServiceImpl_24162056(unusedDao);

        assertThrows(IllegalArgumentException.class, () -> service.getPage(0, 3));
        assertThrows(IllegalArgumentException.class, () -> service.getPage(1, 0));
    }
}
