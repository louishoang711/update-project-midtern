package com.hcmute.bookstore.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart_24162056 {
    private final Map<Integer, CartItem_24162056> items = new LinkedHashMap<>();

    public List<CartItem_24162056> getItems() {
        return new ArrayList<>(items.values());
    }

    public CartItem_24162056 getItem(int bookId) {
        return items.get(bookId);
    }

    public void put(CartItem_24162056 item) {
        items.put(item.getBook().getBookId(), item);
    }

    public void remove(int bookId) {
        items.remove(bookId);
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getTotalQuantity() {
        return items.values().stream().mapToInt(CartItem_24162056::getQuantity).sum();
    }

    public BigDecimal getTotalAmount() {
        return items.values().stream()
                .map(CartItem_24162056::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
