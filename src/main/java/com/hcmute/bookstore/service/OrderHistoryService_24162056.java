package com.hcmute.bookstore.service;

import java.util.List;

import com.hcmute.bookstore.model.Order_24162056;

public interface OrderHistoryService_24162056 {
    List<Order_24162056> findOrders(int userId, String status, int page, int pageSize);

    int countOrders(int userId, String status);
}
