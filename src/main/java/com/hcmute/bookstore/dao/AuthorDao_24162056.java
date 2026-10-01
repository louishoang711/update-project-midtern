package com.hcmute.bookstore.dao;
import java.sql.SQLException; import java.util.List; import java.util.Optional; import com.hcmute.bookstore.model.Author_24162056;
public interface AuthorDao_24162056 { List<Author_24162056> findAll() throws SQLException; Optional<Author_24162056> findById(int id) throws SQLException; }
