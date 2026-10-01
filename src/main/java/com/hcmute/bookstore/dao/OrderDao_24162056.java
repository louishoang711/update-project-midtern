package com.hcmute.bookstore.dao;

import java.sql.SQLException;

import com.hcmute.bookstore.model.CheckoutForm_24162056;
import com.hcmute.bookstore.model.Order_24162056;
import com.hcmute.bookstore.model.ShoppingCart_24162056;

public interface OrderDao_24162056 {
    Order_24162056 createOrder(int userId, CheckoutForm_24162056 form,
                              ShoppingCart_24162056 cart) throws SQLException;
}
