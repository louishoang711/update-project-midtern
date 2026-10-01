package com.hcmute.bookstore.service; import java.util.Optional; import com.hcmute.bookstore.model.User_24162056;
public interface AuthService_24162056 { boolean emailExists(String email); int register(User_24162056 user); Optional<User_24162056> login(String email,String password); }
