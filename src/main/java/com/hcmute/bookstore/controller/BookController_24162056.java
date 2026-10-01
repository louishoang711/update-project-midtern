package com.hcmute.bookstore.controller;

import java.io.IOException;

import com.hcmute.bookstore.dao.impl.JdbcBookDao_24162056;
import com.hcmute.bookstore.service.BookServiceException_24162056;
import com.hcmute.bookstore.service.BookService_24162056;
import com.hcmute.bookstore.service.impl.BookServiceImpl_24162056;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "BookController_24162056", urlPatterns = "/books")
public class BookController_24162056 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookService_24162056 bookService;

    @Override
    public void init() {
        bookService = new BookServiceImpl_24162056(new JdbcBookDao_24162056());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/home");
    }
}
