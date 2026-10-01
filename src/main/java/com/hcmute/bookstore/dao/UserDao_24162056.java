package com.hcmute.bookstore.dao;
import java.sql.SQLException; import java.util.Optional; import com.hcmute.bookstore.model.User_24162056;
public interface UserDao_24162056 { Optional<User_24162056> findByEmail(String email) throws SQLException; int insert(User_24162056 user) throws SQLException; void updateLastLogin(int id) throws SQLException; }
