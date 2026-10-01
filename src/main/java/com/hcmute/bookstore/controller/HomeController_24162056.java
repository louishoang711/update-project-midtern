package com.hcmute.bookstore.controller;

import java.io.IOException;
import java.util.List;

import com.hcmute.bookstore.dao.impl.JdbcAuthorDao_24162056;
import com.hcmute.bookstore.dao.impl.JdbcBookDao_24162056;
import com.hcmute.bookstore.model.Author_24162056;
import com.hcmute.bookstore.service.AuthorService_24162056;
import com.hcmute.bookstore.service.BookService_24162056;
import com.hcmute.bookstore.service.impl.AuthorServiceImpl_24162056;
import com.hcmute.bookstore.service.impl.BookServiceImpl_24162056;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "HomeController_24162056", urlPatterns = "/home")
public class HomeController_24162056 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 3;
    private AuthorService_24162056 authorService;
    private BookService_24162056 bookService;

    @Override public void init() {
        authorService = new AuthorServiceImpl_24162056(new JdbcAuthorDao_24162056());
        bookService = new BookServiceImpl_24162056(new JdbcBookDao_24162056());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Author_24162056> authors = authorService.getAll();
            int authorId = positive(request.getParameter("authorId"));
            if (authorId == 0 && !authors.isEmpty()) authorId = authors.get(0).getAuthorId();
            int page = Math.max(1, positive(request.getParameter("page")));
            if (page == 0) page = 1;
            long total = authorId == 0 ? 0 : bookService.countByAuthor(authorId);
            int totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));
            if (page > totalPages) page = totalPages;
            request.setAttribute("authors", authors);
            request.setAttribute("selectedAuthorId", authorId);
            request.setAttribute("books", authorId == 0 ? List.of() : bookService.getByAuthor(authorId, page, PAGE_SIZE));
            request.setAttribute("page", page);
            request.setAttribute("totalPages", totalPages);
        } catch (RuntimeException exception) {
            request.setAttribute("databaseError", "Chưa kết nối được SQL Server. Hãy kiểm tra lại database.sql và application.properties.");
        }
        request.setAttribute("pageTitle", "Trang chủ");
        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }

    private int positive(String value) {
        try { return Integer.parseInt(value); } catch (RuntimeException exception) { return 0; }
    }
}
