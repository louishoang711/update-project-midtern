package com.hcmute.bookstore.service.impl;

import com.hcmute.bookstore.model.Book_24162056;
import com.hcmute.bookstore.model.CartItem_24162056;
import com.hcmute.bookstore.model.ShoppingCart_24162056;
import com.hcmute.bookstore.service.BookService_24162056;
import com.hcmute.bookstore.service.CartServiceException_24162056;
import com.hcmute.bookstore.service.CartService_24162056;

public class CartServiceImpl_24162056 implements CartService_24162056 {
    private final BookService_24162056 bookService;

    public CartServiceImpl_24162056(BookService_24162056 bookService) {
        this.bookService = bookService;
    }

    @Override
    public void add(ShoppingCart_24162056 cart, int bookId, int quantity) {
        requireCart(cart);
        if (quantity < 1) {
            throw new CartServiceException_24162056("Số lượng phải lớn hơn 0.");
        }

        Book_24162056 book = findBook(bookId);
        CartItem_24162056 currentItem = cart.getItem(bookId);
        int newQuantity = quantity + (currentItem == null ? 0 : currentItem.getQuantity());
        validateStock(book, newQuantity);
        cart.put(new CartItem_24162056(book, newQuantity));
    }

    @Override
    public void update(ShoppingCart_24162056 cart, int bookId, int quantity) {
        requireCart(cart);
        if (cart.getItem(bookId) == null) {
            throw new CartServiceException_24162056("Sản phẩm không có trong giỏ hàng.");
        }
        if (quantity < 1) {
            throw new CartServiceException_24162056("Số lượng phải lớn hơn 0.");
        }

        Book_24162056 book = findBook(bookId);
        validateStock(book, quantity);
        cart.put(new CartItem_24162056(book, quantity));
    }

    @Override
    public void remove(ShoppingCart_24162056 cart, int bookId) {
        requireCart(cart);
        cart.remove(bookId);
    }

    @Override
    public void clear(ShoppingCart_24162056 cart) {
        requireCart(cart);
        cart.clear();
    }

    private Book_24162056 findBook(int bookId) {
        if (bookId < 1) {
            throw new CartServiceException_24162056("Mã sách không hợp lệ.");
        }
        return bookService.getById(bookId)
                .orElseThrow(() -> new CartServiceException_24162056("Không tìm thấy sách."));
    }

    private void validateStock(Book_24162056 book, int requestedQuantity) {
        int stock = book.getQuantity() == null ? 0 : book.getQuantity();
        if (stock < 1) {
            throw new CartServiceException_24162056("Sách đã hết hàng.");
        }
        if (requestedQuantity > stock) {
            throw new CartServiceException_24162056("Sách chỉ còn " + stock + " cuốn.");
        }
    }

    private void requireCart(ShoppingCart_24162056 cart) {
        if (cart == null) {
            throw new CartServiceException_24162056("Giỏ hàng không hợp lệ.");
        }
    }
}
