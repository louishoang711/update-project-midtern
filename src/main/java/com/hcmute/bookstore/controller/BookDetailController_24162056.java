package com.hcmute.bookstore.controller;

import java.io.IOException;

import com.hcmute.bookstore.dao.impl.JdbcBookDao_24162056;
import com.hcmute.bookstore.dao.impl.JdbcRatingDao_24162056;
import com.hcmute.bookstore.service.BookService_24162056;
import com.hcmute.bookstore.service.RatingService_24162056;
import com.hcmute.bookstore.service.impl.BookServiceImpl_24162056;
import com.hcmute.bookstore.service.impl.RatingServiceImpl_24162056;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "BookDetailController_24162056", urlPatterns = "/book")
public class BookDetailController_24162056 extends HttpServlet {
    private BookService_24162056 bookService;
    private RatingService_24162056 ratingService;
    @Override public void init() { bookService = new BookServiceImpl_24162056(new JdbcBookDao_24162056()); ratingService = new RatingServiceImpl_24162056(new JdbcRatingDao_24162056()); }
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id;
        try { id = Integer.parseInt(request.getParameter("id")); } catch (RuntimeException exception) { response.sendError(404); return; }
        try {
            var book = bookService.getById(id);
            if (book.isEmpty()) { response.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
            request.setAttribute("book", book.get());
            request.setAttribute("reviews", ratingService.getByBook(id));
            request.setAttribute("pageTitle", book.get().getTitle());
            request.getRequestDispatcher("/WEB-INF/views/books/detail.jsp").forward(request, response);
        } catch (RuntimeException exception) {
            throw new ServletException("Không tải được thông tin sách", exception);
        }
    }
}
