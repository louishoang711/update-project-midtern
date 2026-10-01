package com.hcmute.bookstore.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hcmute.bookstore.config.DatabaseConnection_24162056;
import com.hcmute.bookstore.dao.OrderDao_24162056;
import com.hcmute.bookstore.model.CartItem_24162056;
import com.hcmute.bookstore.model.CheckoutForm_24162056;
import com.hcmute.bookstore.model.OrderItem_24162056;
import com.hcmute.bookstore.model.Order_24162056;
import com.hcmute.bookstore.model.ShoppingCart_24162056;

public class JdbcOrderDao_24162056 implements OrderDao_24162056 {
    private static final String SELECT_BOOK_FOR_UPDATE = """
            SELECT bookid, title, price, quantity
            FROM dbo.books WITH (UPDLOCK, ROWLOCK)
            WHERE bookid = ?
            """;
    private static final String INSERT_ORDER = """
            INSERT INTO dbo.orders
                (user_id, receiver_name, receiver_phone, shipping_address,
                 total_amount, payment_method, status, created_at)
            VALUES (?, ?, ?, ?, ?, 'COD', 'NEW', GETDATE())
            """;
    private static final String INSERT_ORDER_ITEM = """
            INSERT INTO dbo.order_items
                (order_id, book_id, book_title, unit_price, quantity, line_total)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
    private static final String DECREASE_STOCK = """
            UPDATE dbo.books
            SET quantity = quantity - ?
            WHERE bookid = ? AND quantity >= ?
            """;

    @Override
    public Order_24162056 createOrder(int userId, CheckoutForm_24162056 form,
                                     ShoppingCart_24162056 cart) throws SQLException {
        try (Connection connection = DatabaseConnection_24162056.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<OrderItem_24162056> orderItems = loadAndLockBooks(connection, cart);
                BigDecimal total = orderItems.stream()
                        .map(OrderItem_24162056::getLineTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                int orderId = insertOrder(connection, userId, form, total);
                insertItemsAndDecreaseStock(connection, orderId, orderItems);
                LocalDateTime createdAt = readCreatedAt(connection, orderId);
                connection.commit();
                return toOrder(orderId, userId, form, total, createdAt, orderItems);
            } catch (SQLException | RuntimeException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private List<OrderItem_24162056> loadAndLockBooks(Connection connection,
                                                       ShoppingCart_24162056 cart) throws SQLException {
        List<OrderItem_24162056> orderItems = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BOOK_FOR_UPDATE)) {
            for (CartItem_24162056 cartItem : cart.getItems()) {
                int bookId = cartItem.getBook().getBookId();
                statement.setInt(1, bookId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new SQLException("Không tìm thấy sách có mã " + bookId + ".");
                    }
                    int stock = resultSet.getObject("quantity", Integer.class) == null
                            ? 0 : resultSet.getInt("quantity");
                    int quantity = cartItem.getQuantity();
                    String title = resultSet.getString("title");
                    BigDecimal price = resultSet.getBigDecimal("price");
                    if (quantity < 1) {
                        throw new SQLException("Sách " + title + " có số lượng đặt không hợp lệ.");
                    }
                    if (quantity > stock) {
                        throw new SQLException("Sách " + title + " chỉ còn " + stock + " cuốn.");
                    }
                    if (price == null) {
                        throw new SQLException("Sách " + title + " chưa có giá bán.");
                    }

                    OrderItem_24162056 item = new OrderItem_24162056();
                    item.setBookId(bookId);
                    item.setBookTitle(title);
                    item.setUnitPrice(price);
                    item.setQuantity(quantity);
                    item.setLineTotal(price.multiply(BigDecimal.valueOf(quantity)));
                    orderItems.add(item);
                }
            }
        }
        return orderItems;
    }

    private int insertOrder(Connection connection, int userId, CheckoutForm_24162056 form,
                            BigDecimal total) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userId);
            statement.setString(2, form.getReceiverName());
            statement.setString(3, form.getReceiverPhone());
            statement.setString(4, form.getShippingAddress());
            statement.setBigDecimal(5, total);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("Không lấy được mã đơn hàng.");
                }
                return keys.getInt(1);
            }
        }
    }

    private void insertItemsAndDecreaseStock(Connection connection, int orderId,
                                              List<OrderItem_24162056> items) throws SQLException {
        try (PreparedStatement insertItem = connection.prepareStatement(INSERT_ORDER_ITEM);
             PreparedStatement decreaseStock = connection.prepareStatement(DECREASE_STOCK)) {
            for (OrderItem_24162056 item : items) {
                item.setOrderId(orderId);
                insertItem.setInt(1, orderId);
                insertItem.setInt(2, item.getBookId());
                insertItem.setString(3, item.getBookTitle());
                insertItem.setBigDecimal(4, item.getUnitPrice());
                insertItem.setInt(5, item.getQuantity());
                insertItem.setBigDecimal(6, item.getLineTotal());
                insertItem.executeUpdate();

                decreaseStock.setInt(1, item.getQuantity());
                decreaseStock.setInt(2, item.getBookId());
                decreaseStock.setInt(3, item.getQuantity());
                if (decreaseStock.executeUpdate() != 1) {
                    throw new SQLException("Sách " + item.getBookTitle() + " không còn đủ số lượng.");
                }
            }
        }
    }

    private LocalDateTime readCreatedAt(Connection connection, int orderId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT created_at FROM dbo.orders WHERE order_id = ?")) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("Không đọc được đơn hàng vừa tạo.");
                }
                Timestamp timestamp = resultSet.getTimestamp(1);
                return timestamp == null ? LocalDateTime.now() : timestamp.toLocalDateTime();
            }
        }
    }

    private Order_24162056 toOrder(int orderId, int userId, CheckoutForm_24162056 form,
                                   BigDecimal total, LocalDateTime createdAt,
                                   List<OrderItem_24162056> items) {
        Order_24162056 order = new Order_24162056();
        order.setOrderId(orderId);
        order.setUserId(userId);
        order.setReceiverName(form.getReceiverName());
        order.setReceiverPhone(form.getReceiverPhone());
        order.setShippingAddress(form.getShippingAddress());
        order.setTotalAmount(total);
        order.setPaymentMethod("COD");
        order.setStatus("NEW");
        order.setCreatedAt(createdAt);
        order.setItems(items);
        return order;
    }
}
