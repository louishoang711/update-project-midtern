package com.hcmute.bookstore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.hcmute.bookstore.dao.OrderDao_24162056;
import com.hcmute.bookstore.model.Book_24162056;
import com.hcmute.bookstore.model.CheckoutForm_24162056;
import com.hcmute.bookstore.model.Order_24162056;
import com.hcmute.bookstore.model.ShoppingCart_24162056;
import com.hcmute.bookstore.service.BookService_24162056;
import com.hcmute.bookstore.service.CartServiceException_24162056;
import com.hcmute.bookstore.service.CheckoutServiceException_24162056;
import com.hcmute.bookstore.service.impl.CartServiceImpl_24162056;
import com.hcmute.bookstore.service.impl.CheckoutServiceImpl_24162056;

class CartCheckoutServiceTest_24162056 {
    @Test
    void cartAddsSameBookAndEnforcesStockLimit() {
        Book_24162056 book = book(1, 3, "8500.00");
        CartServiceImpl_24162056 service = new CartServiceImpl_24162056(bookService(book));
        ShoppingCart_24162056 cart = new ShoppingCart_24162056();

        service.add(cart, 1, 2);
        service.add(cart, 1, 1);

        assertEquals(3, cart.getTotalQuantity());
        assertEquals(new BigDecimal("25500.00"), cart.getTotalAmount());
        assertThrows(CartServiceException_24162056.class, () -> service.add(cart, 1, 1));
        assertThrows(CartServiceException_24162056.class, () -> service.update(cart, 1, 0));
    }

    @Test
    void checkoutValidatesFormAndDelegatesToOrderDao() {
        ShoppingCart_24162056 cart = new ShoppingCart_24162056();
        cart.put(new com.hcmute.bookstore.model.CartItem_24162056(book(1, 5, "8500.00"), 2));
        OrderDao_24162056 fakeDao = (userId, form, shoppingCart) -> {
            Order_24162056 order = new Order_24162056();
            order.setOrderId(15);
            order.setUserId(userId);
            order.setReceiverName(form.getReceiverName());
            order.setTotalAmount(shoppingCart.getTotalAmount());
            return order;
        };
        CheckoutServiceImpl_24162056 service = new CheckoutServiceImpl_24162056(fakeDao);
        CheckoutForm_24162056 form = form("Nguyễn Văn A", "0912345678", "01 Võ Văn Ngân, Thủ Đức");

        Order_24162056 order = service.placeOrder(2, form, cart);

        assertEquals(15, order.getOrderId());
        assertEquals(2, order.getUserId());
        assertEquals(new BigDecimal("17000.00"), order.getTotalAmount());
    }

    @Test
    void checkoutRejectsEmptyCartAndInvalidPhone() {
        CheckoutServiceImpl_24162056 service = new CheckoutServiceImpl_24162056(
                (userId, form, cart) -> new Order_24162056());
        ShoppingCart_24162056 emptyCart = new ShoppingCart_24162056();
        assertThrows(CheckoutServiceException_24162056.class,
                () -> service.placeOrder(2, form("Nguyễn Văn A", "0912345678", "Thủ Đức"), emptyCart));

        ShoppingCart_24162056 cart = new ShoppingCart_24162056();
        cart.put(new com.hcmute.bookstore.model.CartItem_24162056(book(1, 5, "8500.00"), 1));
        assertThrows(CheckoutServiceException_24162056.class,
                () -> service.placeOrder(2, form("Nguyễn Văn A", "123", "Thủ Đức"), cart));
    }

    private Book_24162056 book(int id, int stock, String price) {
        Book_24162056 book = new Book_24162056();
        book.setBookId(id);
        book.setTitle("Sách kiểm thử");
        book.setQuantity(stock);
        book.setPrice(new BigDecimal(price));
        return book;
    }

    private CheckoutForm_24162056 form(String name, String phone, String address) {
        CheckoutForm_24162056 form = new CheckoutForm_24162056();
        form.setReceiverName(name);
        form.setReceiverPhone(phone);
        form.setShippingAddress(address);
        return form;
    }

    private BookService_24162056 bookService(Book_24162056 book) {
        return new BookService_24162056() {
            @Override public List<Book_24162056> getPage(int pageNumber, int pageSize) { return List.of(book); }
            @Override public Optional<Book_24162056> getById(int bookId) { return bookId == book.getBookId() ? Optional.of(book) : Optional.empty(); }
            @Override public long countBooks() { return 1; }
            @Override public List<Book_24162056> getByAuthor(int authorId, int page, int size) { return List.of(book); }
            @Override public long countByAuthor(int authorId) { return 1; }
            @Override public int create(Book_24162056 value, List<Integer> authorIds) { return value.getBookId(); }
            @Override public void update(Book_24162056 value, List<Integer> authorIds) { }
            @Override public void delete(int bookId) { }
        };
    }
}
