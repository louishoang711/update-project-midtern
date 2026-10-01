package com.hcmute.bookstore.service.impl;

import java.sql.SQLException;

import com.hcmute.bookstore.dao.OrderDao_24162056;
import com.hcmute.bookstore.model.CheckoutForm_24162056;
import com.hcmute.bookstore.model.Order_24162056;
import com.hcmute.bookstore.model.ShoppingCart_24162056;
import com.hcmute.bookstore.service.CheckoutServiceException_24162056;
import com.hcmute.bookstore.service.CheckoutService_24162056;

public class CheckoutServiceImpl_24162056 implements CheckoutService_24162056 {
    private final OrderDao_24162056 orderDao;

    public CheckoutServiceImpl_24162056(OrderDao_24162056 orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Order_24162056 placeOrder(int userId, CheckoutForm_24162056 form,
                                    ShoppingCart_24162056 cart) {
        if (userId < 1) {
            throw new CheckoutServiceException_24162056("Bạn cần đăng nhập để đặt hàng.");
        }
        if (cart == null || cart.isEmpty()) {
            throw new CheckoutServiceException_24162056("Giỏ hàng đang trống.");
        }
        validate(form);

        try {
            return orderDao.createOrder(userId, form, cart);
        } catch (SQLException exception) {
            String message = exception.getMessage();
            if (message != null && (message.startsWith("Sách ") || message.startsWith("Không tìm thấy"))) {
                throw new CheckoutServiceException_24162056(message, exception);
            }
            throw new CheckoutServiceException_24162056("Không thể tạo đơn hàng. Vui lòng thử lại.", exception);
        }
    }

    private void validate(CheckoutForm_24162056 form) {
        if (form == null) {
            throw new CheckoutServiceException_24162056("Thông tin nhận hàng không hợp lệ.");
        }

        form.setReceiverName(trim(form.getReceiverName()));
        form.setReceiverPhone(trim(form.getReceiverPhone()).replaceAll("[ .-]", ""));
        form.setShippingAddress(trim(form.getShippingAddress()));

        if (form.getReceiverName().length() < 2 || form.getReceiverName().length() > 100) {
            throw new CheckoutServiceException_24162056("Họ tên người nhận phải từ 2 đến 100 ký tự.");
        }
        if (!form.getReceiverPhone().matches("0\\d{9}")) {
            throw new CheckoutServiceException_24162056("Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0.");
        }
        if (form.getShippingAddress().length() < 5 || form.getShippingAddress().length() > 255) {
            throw new CheckoutServiceException_24162056("Địa chỉ nhận hàng phải từ 5 đến 255 ký tự.");
        }
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
