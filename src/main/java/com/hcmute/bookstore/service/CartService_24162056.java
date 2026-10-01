package com.hcmute.bookstore.service;

import com.hcmute.bookstore.model.ShoppingCart_24162056;

public interface CartService_24162056 {
    void add(ShoppingCart_24162056 cart, int bookId, int quantity);
    void update(ShoppingCart_24162056 cart, int bookId, int quantity);
    void remove(ShoppingCart_24162056 cart, int bookId);
    void clear(ShoppingCart_24162056 cart);
}
