package com.hcmute.bookstore.dao;
import java.sql.SQLException; import java.util.List; import com.hcmute.bookstore.model.Rating_24162056;
public interface RatingDao_24162056 { List<Rating_24162056> findByBookId(int id) throws SQLException; void save(Rating_24162056 rating) throws SQLException; }
