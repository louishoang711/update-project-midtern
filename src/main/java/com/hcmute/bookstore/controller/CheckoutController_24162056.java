package com.hcmute.bookstore.controller;

import java.io.IOException;

import com.hcmute.bookstore.dao.impl.JdbcOrderDao_24162056;
import com.hcmute.bookstore.model.CheckoutForm_24162056;
import com.hcmute.bookstore.model.Order_24162056;
import com.hcmute.bookstore.model.ShoppingCart_24162056;
import com.hcmute.bookstore.model.User_24162056;
import com.hcmute.bookstore.service.CheckoutServiceException_24162056;
import com.hcmute.bookstore.service.CheckoutService_24162056;
import com.hcmute.bookstore.service.impl.CheckoutServiceImpl_24162056;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "CheckoutController_24162056",
        urlPatterns = {"/checkout", "/checkout/place", "/checkout/success"})
public class CheckoutController_24162056 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String LAST_ORDER_SESSION_KEY = "lastPlacedOrder";
    private CheckoutService_24162056 checkoutService;

    @Override
    public void init() {
        checkoutService = new CheckoutServiceImpl_24162056(new JdbcOrderDao_24162056());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("/checkout/success".equals(request.getServletPath())) {
            showSuccess(request, response);
            return;
        }
        if (!"/checkout".equals(request.getServletPath())) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        ShoppingCart_24162056 cart = CartController_24162056.getCart(request.getSession());
        if (cart.isEmpty()) {
            request.getSession().setAttribute("cartError", "Giỏ hàng đang trống.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        CheckoutForm_24162056 form = defaultForm(currentUser(request));
        showForm(request, response, cart, form);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!"/checkout/place".equals(request.getServletPath())) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        HttpSession session = request.getSession();
        ShoppingCart_24162056 cart = CartController_24162056.getCart(session);
        CheckoutForm_24162056 form = bind(request);
        try {
            Order_24162056 order = checkoutService.placeOrder(
                    currentUser(request).getId(), form, cart);
            session.removeAttribute(CartController_24162056.CART_SESSION_KEY);
            session.setAttribute(LAST_ORDER_SESSION_KEY, order);
            response.sendRedirect(request.getContextPath() + "/checkout/success?orderId=" + order.getOrderId());
        } catch (CheckoutServiceException_24162056 exception) {
            request.setAttribute("error", exception.getMessage());
            showForm(request, response, cart, form);
        }
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response,
                          ShoppingCart_24162056 cart, CheckoutForm_24162056 form)
            throws ServletException, IOException {
        request.setAttribute("cart", cart);
        request.setAttribute("checkoutForm", form);
        request.setAttribute("pageTitle", "Thanh toán COD");
        request.getRequestDispatcher("/WEB-INF/views/checkout/index.jsp").forward(request, response);
    }

    private void showSuccess(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Object value = request.getSession().getAttribute(LAST_ORDER_SESSION_KEY);
        int requestedId = positive(request.getParameter("orderId"));
        if (!(value instanceof Order_24162056 order) || order.getOrderId() != requestedId) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        request.setAttribute("order", order);
        request.setAttribute("pageTitle", "Đặt hàng thành công");
        request.getRequestDispatcher("/WEB-INF/views/checkout/success.jsp").forward(request, response);
    }

    private User_24162056 currentUser(HttpServletRequest request) {
        return (User_24162056) request.getSession().getAttribute("currentUser");
    }

    private CheckoutForm_24162056 defaultForm(User_24162056 user) {
        CheckoutForm_24162056 form = new CheckoutForm_24162056();
        form.setReceiverName(user.getFullName());
        if (user.getPhone() != null) {
            String phone = String.valueOf(user.getPhone());
            form.setReceiverPhone(phone.startsWith("0") ? phone : "0" + phone);
        }
        return form;
    }

    private CheckoutForm_24162056 bind(HttpServletRequest request) {
        CheckoutForm_24162056 form = new CheckoutForm_24162056();
        form.setReceiverName(value(request, "receiverName"));
        form.setReceiverPhone(value(request, "receiverPhone"));
        form.setShippingAddress(value(request, "shippingAddress"));
        return form;
    }

    private String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    private int positive(String value) {
        try { return Math.max(0, Integer.parseInt(value)); }
        catch (RuntimeException exception) { return 0; }
    }
}
