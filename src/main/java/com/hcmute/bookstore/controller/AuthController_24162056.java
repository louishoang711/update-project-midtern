package com.hcmute.bookstore.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import com.hcmute.bookstore.dao.impl.JdbcUserDao_24162056;
import com.hcmute.bookstore.model.User_24162056;
import com.hcmute.bookstore.service.AuthService_24162056;
import com.hcmute.bookstore.service.MailService_24162056;
import com.hcmute.bookstore.service.impl.AuthServiceImpl_24162056;
import com.hcmute.bookstore.service.impl.MailServiceImpl_24162056;
import com.hcmute.bookstore.util.OtpUtil_24162056;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AuthController_24162056", urlPatterns = {"/login", "/register", "/verify-otp", "/logout"})
public class AuthController_24162056 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AuthService_24162056 authService;
    private MailService_24162056 mailService;

    @Override public void init() {
        authService = new AuthServiceImpl_24162056(new JdbcUserDao_24162056());
        mailService = new MailServiceImpl_24162056();
    }

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/logout".equals(path)) {
            HttpSession session = request.getSession(false);
            if (session != null) session.invalidate();
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        if ("/verify-otp".equals(path) && request.getSession().getAttribute("pendingUser") == null) {
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }
        request.setAttribute("pageTitle", "Đăng nhập");
        request.getRequestDispatcher("/WEB-INF/views/auth/" + ("/register".equals(path) ? "register.jsp" : "/verify-otp".equals(path) ? "verify.jsp" : "login.jsp")).forward(request, response);
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getServletPath()) {
            case "/login" -> login(request, response);
            case "/register" -> register(request, response);
            case "/verify-otp" -> verifyOtp(request, response);
            default -> response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<User_24162056> user = authService.login(value(request, "email"), value(request, "password"));
        if (user.isEmpty()) {
            request.setAttribute("error", "Email hoặc mật khẩu chưa đúng.");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            return;
        }
        request.getSession().setAttribute("currentUser", user.get());
        response.sendRedirect(request.getContextPath() + (user.get().isAdmin() ? "/admin/dashboard" : "/home"));
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = value(request, "email");
        String fullName = value(request, "fullName");
        String password = value(request, "password");
        if (email.isBlank() || fullName.isBlank() || password.length() < 6 || !password.equals(value(request, "confirmPassword"))) {
            request.setAttribute("error", "Hãy nhập đủ thông tin; mật khẩu tối thiểu 6 ký tự và phải khớp xác nhận.");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }
        if (authService.emailExists(email)) {
            request.setAttribute("error", "Email này đã được đăng ký.");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }
        User_24162056 pending = new User_24162056();
        pending.setEmail(email);
        pending.setFullName(fullName);
        pending.setPassword(password);
        try { pending.setPhone(Integer.valueOf(value(request, "phone"))); } catch (NumberFormatException ignored) { }
        String otp = OtpUtil_24162056.create();
        HttpSession session = request.getSession();
        session.setAttribute("pendingUser", pending);
        session.setAttribute("pendingOtp", otp);
        session.setAttribute("pendingOtpExpires", Instant.now().plusSeconds(300));
        try {
            mailService.sendOtp(email, otp);
            session.setAttribute("notice", "Mã xác thực đã được gửi tới email của bạn.");
            response.sendRedirect(request.getContextPath() + "/verify-otp");
        } catch (RuntimeException exception) {
            request.setAttribute("error", exception.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
        }
    }

    private void verifyOtp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User_24162056 pending = (User_24162056) session.getAttribute("pendingUser");
        Instant expires = (Instant) session.getAttribute("pendingOtpExpires");
        String otp = (String) session.getAttribute("pendingOtp");
        if (pending == null || expires == null || expires.isBefore(Instant.now())) {
            session.removeAttribute("pendingUser");
            request.setAttribute("error", "Mã OTP đã hết hạn. Hãy đăng ký lại để nhận mã mới.");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }
        if (!value(request, "otp").equals(otp)) {
            request.setAttribute("error", "Mã OTP chưa chính xác.");
            request.getRequestDispatcher("/WEB-INF/views/auth/verify.jsp").forward(request, response);
            return;
        }
        authService.register(pending);
        session.removeAttribute("pendingUser"); session.removeAttribute("pendingOtp"); session.removeAttribute("pendingOtpExpires");
        session.setAttribute("notice", "Đăng ký thành công. Bạn có thể đăng nhập ngay.");
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }
}
