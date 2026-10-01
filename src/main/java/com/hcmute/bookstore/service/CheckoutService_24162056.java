package com.hcmute.bookstore.service;

import com.hcmute.bookstore.model.CheckoutForm_24162056;
import com.hcmute.bookstore.model.Order_24162056;
import com.hcmute.bookstore.model.ShoppingCart_24162056;

public interface CheckoutService_24162056 {
    Order_24162056 placeOrder(int userId, CheckoutForm_24162056 form, ShoppingCart_24162056 cart);
}
