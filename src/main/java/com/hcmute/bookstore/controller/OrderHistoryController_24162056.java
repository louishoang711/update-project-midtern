package com.hcmute.bookstore.controller;

import java.io.IOException;
import java.util.List;

import com.hcmute.bookstore.dao.impl.JdbcOrderDao_24162056;
import com.hcmute.bookstore.model.OrderStatus_24162056;
import com.hcmute.bookstore.model.Order_24162056;
import com.hcmute.bookstore.model.User_24162056;
import com.hcmute.bookstore.service.OrderHistoryService_24162056;
import com.hcmute.bookstore.service.impl.OrderHistoryServiceImpl_24162056;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "OrderHistoryController_24162056", urlPatterns = {"/orders"})
public class OrderHistoryController_24162056 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 5;
    private OrderHistoryService_24162056 orderHistoryService;

    @Override
    public void init() {
        orderHistoryService = new OrderHistoryServiceImpl_24162056(new JdbcOrderDao_24162056());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User_24162056 user = (User_24162056) request.getSession().getAttribute("currentUser");
        String status = OrderStatus_24162056.clean(request.getParameter("status"));
        int page = positive(request.getParameter("page"), 1);

        int totalOrders = orderHistoryService.countOrders(user.getId(), status);
        int totalPages = Math.max(1, (int) Math.ceil(totalOrders / (double) PAGE_SIZE));
        if (page > totalPages) {
            page = totalPages;
        }

        List<Order_24162056> orders = orderHistoryService.findOrders(
                user.getId(), status, page, PAGE_SIZE);
        request.setAttribute("orders", orders);
        request.setAttribute("statusOptions", OrderStatus_24162056.OPTIONS);
        request.setAttribute("selectedStatus", status);
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("pageTitle", "Lịch sử đặt hàng");
        request.getRequestDispatcher("/WEB-INF/views/orders/history.jsp").forward(request, response);
    }

    private int positive(String value, int fallback) {
        try {
            return Math.max(1, Integer.parseInt(value));
        } catch (RuntimeException exception) {
            return fallback;
        }
    }
}
