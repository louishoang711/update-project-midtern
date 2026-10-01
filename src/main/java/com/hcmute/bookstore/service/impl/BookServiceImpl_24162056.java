package com.hcmute.bookstore.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.hcmute.bookstore.dao.BookDao_24162056;
import com.hcmute.bookstore.model.Book_24162056;
import com.hcmute.bookstore.service.BookServiceException_24162056;
import com.hcmute.bookstore.service.BookService_24162056;

public class BookServiceImpl_24162056 implements BookService_24162056 {
    private final BookDao_24162056 bookDao;

    public BookServiceImpl_24162056(BookDao_24162056 bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public List<Book_24162056> getPage(int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Trang va kich thuoc trang phai lon hon 0");
        }
        try {
            return bookDao.findPage((pageNumber - 1) * pageSize, pageSize);
        } catch (SQLException exception) {
            throw new BookServiceException_24162056("Khong the tai danh sach sach", exception);
        }
    }

    @Override
    public Optional<Book_24162056> getById(int bookId) {
        try {
            return bookDao.findById(bookId);
        } catch (SQLException exception) {
            throw new BookServiceException_24162056("Khong the tai thong tin sach", exception);
        }
    }

    @Override
    public long countBooks() {
        try {
            return bookDao.count();
        } catch (SQLException exception) {
            throw new BookServiceException_24162056("Khong the dem so sach", exception);
        }
    }
    @Override public List<Book_24162056> getByAuthor(int id,int page,int size){try{return bookDao.findByAuthor(id,(page-1)*size,size);}catch(SQLException e){throw new BookServiceException_24162056("Khong the tai sach",e);}}
    @Override public long countByAuthor(int id){try{return bookDao.countByAuthor(id);}catch(SQLException e){throw new BookServiceException_24162056("Khong the dem sach",e);}}
    @Override public int create(Book_24162056 b,List<Integer>a){try{return bookDao.insert(b,a);}catch(SQLException e){throw new BookServiceException_24162056("Khong the them sach",e);}}
    @Override public void update(Book_24162056 b,List<Integer>a){try{bookDao.update(b,a);}catch(SQLException e){throw new BookServiceException_24162056("Khong the cap nhat sach",e);}}
    @Override public void delete(int id){try{bookDao.delete(id);}catch(SQLException e){throw new BookServiceException_24162056("Khong the xoa sach",e);}}
}
