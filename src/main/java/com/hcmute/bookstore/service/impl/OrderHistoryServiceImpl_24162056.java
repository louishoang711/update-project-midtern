package com.hcmute.bookstore.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.hcmute.bookstore.dao.OrderDao_24162056;
import com.hcmute.bookstore.model.OrderStatus_24162056;
import com.hcmute.bookstore.model.Order_24162056;
import com.hcmute.bookstore.service.OrderHistoryService_24162056;

public class OrderHistoryServiceImpl_24162056 implements OrderHistoryService_24162056 {
    private final OrderDao_24162056 orderDao;

    public OrderHistoryServiceImpl_24162056(OrderDao_24162056 orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public List<Order_24162056> findOrders(int userId, String status, int page, int pageSize) {
        if (userId < 1) {
            return List.of();
        }
        String cleanStatus = normalize(status);
        int cleanPage = Math.max(1, page);
        int cleanPageSize = Math.max(1, pageSize);
        try {
            return orderDao.findByUser(
                    userId, cleanStatus, (cleanPage - 1) * cleanPageSize, cleanPageSize);
        } catch (SQLException exception) {
            throw new IllegalStateException("Không thể đọc lịch sử đơn hàng.", exception);
        }
    }

    @Override
    public int countOrders(int userId, String status) {
        if (userId < 1) {
            return 0;
        }
        try {
            return orderDao.countByUser(userId, normalize(status));
        } catch (SQLException exception) {
            throw new IllegalStateException("Không thể đếm lịch sử đơn hàng.", exception);
        }
    }

    private String normalize(String status) {
        String clean = OrderStatus_24162056.clean(status);
        return clean.isBlank() ? null : clean;
    }
}
