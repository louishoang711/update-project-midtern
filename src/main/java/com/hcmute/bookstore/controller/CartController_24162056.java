package com.hcmute.bookstore.controller;

import java.io.IOException;

import com.hcmute.bookstore.dao.impl.JdbcBookDao_24162056;
import com.hcmute.bookstore.model.ShoppingCart_24162056;
import com.hcmute.bookstore.service.BookService_24162056;
import com.hcmute.bookstore.service.CartServiceException_24162056;
import com.hcmute.bookstore.service.CartService_24162056;
import com.hcmute.bookstore.service.impl.BookServiceImpl_24162056;
import com.hcmute.bookstore.service.impl.CartServiceImpl_24162056;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "CartController_24162056",
        urlPatterns = {"/cart", "/cart/add", "/cart/update", "/cart/remove", "/cart/clear"})
public class CartController_24162056 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static final String CART_SESSION_KEY = "shoppingCart";
    private CartService_24162056 cartService;

    @Override
    public void init() {
        BookService_24162056 bookService = new BookServiceImpl_24162056(new JdbcBookDao_24162056());
        cartService = new CartServiceImpl_24162056(bookService);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!"/cart".equals(request.getServletPath())) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        request.setAttribute("cart", getCart(request.getSession()));
        moveFlashToRequest(request, "cartMessage");
        moveFlashToRequest(request, "cartError");
        request.setAttribute("pageTitle", "Giỏ hàng");
        request.getRequestDispatcher("/WEB-INF/views/cart/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        ShoppingCart_24162056 cart = getCart(session);
        String path = request.getServletPath();
        try {
            switch (path) {
                case "/cart/add" -> {
                    cartService.add(cart, positive(request.getParameter("bookId")),
                            quantity(request.getParameter("quantity")));
                    session.setAttribute("cartMessage", "Đã thêm sách vào giỏ hàng.");
                }
                case "/cart/update" -> {
                    cartService.update(cart, positive(request.getParameter("bookId")),
                            quantity(request.getParameter("quantity")));
                    session.setAttribute("cartMessage", "Đã cập nhật số lượng.");
                }
                case "/cart/remove" -> {
                    cartService.remove(cart, positive(request.getParameter("bookId")));
                    session.setAttribute("cartMessage", "Đã xóa sách khỏi giỏ hàng.");
                }
                case "/cart/clear" -> {
                    cartService.clear(cart);
                    session.setAttribute("cartMessage", "Đã xóa toàn bộ giỏ hàng.");
                }
                default -> {
                    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    return;
                }
            }
        } catch (CartServiceException_24162056 exception) {
            session.setAttribute("cartError", exception.getMessage());
        } catch (RuntimeException exception) {
            session.setAttribute("cartError", "Không thể cập nhật giỏ hàng. Vui lòng thử lại.");
        }
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    public static ShoppingCart_24162056 getCart(HttpSession session) {
        Object value = session.getAttribute(CART_SESSION_KEY);
        if (value instanceof ShoppingCart_24162056 cart) return cart;
        ShoppingCart_24162056 cart = new ShoppingCart_24162056();
        session.setAttribute(CART_SESSION_KEY, cart);
        return cart;
    }

    private void moveFlashToRequest(HttpServletRequest request, String name) {
        Object value = request.getSession().getAttribute(name);
        if (value != null) {
            request.setAttribute(name, value);
            request.getSession().removeAttribute(name);
        }
    }

    private int positive(String value) {
        try {
            int number = Integer.parseInt(value);
            return Math.max(number, 0);
        } catch (RuntimeException exception) {
            return 0;
        }
    }

    private int quantity(String value) {
        return positive(value);
    }
}
