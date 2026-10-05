package com.hcmute.bookstore.dao;

import java.sql.SQLException;
import java.util.List;

import com.hcmute.bookstore.model.CheckoutForm_24162056;
import com.hcmute.bookstore.model.Order_24162056;
import com.hcmute.bookstore.model.ShoppingCart_24162056;

public interface OrderDao_24162056 {
    Order_24162056 createOrder(int userId, CheckoutForm_24162056 form,
                              ShoppingCart_24162056 cart) throws SQLException;

    default List<Order_24162056> findByUser(int userId, String status, int offset, int limit)
            throws SQLException {
        throw new UnsupportedOperationException("Chưa hỗ trợ đọc lịch sử đơn hàng.");
    }

    default int countByUser(int userId, String status) throws SQLException {
        throw new UnsupportedOperationException("Chưa hỗ trợ đếm lịch sử đơn hàng.");
    }
}
