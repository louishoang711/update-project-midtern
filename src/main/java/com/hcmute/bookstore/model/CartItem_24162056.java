package com.hcmute.bookstore.model;

import java.math.BigDecimal;

public class CartItem_24162056 {
    private Book_24162056 book;
    private int quantity;

    public CartItem_24162056() {
    }

    public CartItem_24162056(Book_24162056 book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book_24162056 getBook() {
        return book;
    }

    public void setBook(Book_24162056 book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        if (book == null || book.getPrice() == null) {
            return BigDecimal.ZERO;
        }
        return book.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
