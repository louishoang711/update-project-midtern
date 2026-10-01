package com.hcmute.bookstore.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.hcmute.bookstore.dao.impl.JdbcAuthorDao_24162056;
import com.hcmute.bookstore.dao.impl.JdbcBookDao_24162056;
import com.hcmute.bookstore.model.Book_24162056;
import com.hcmute.bookstore.service.AuthorService_24162056;
import com.hcmute.bookstore.service.BookService_24162056;
import com.hcmute.bookstore.service.impl.AuthorServiceImpl_24162056;
import com.hcmute.bookstore.service.impl.BookServiceImpl_24162056;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(name = "AdminBookController_24162056", urlPatterns = {"/admin/books", "/admin/books/create", "/admin/books/edit", "/admin/books/delete"})
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public class AdminBookController_24162056 extends HttpServlet {
    private static final int PAGE_SIZE = 5;
    private static final Set<String> IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp", "image/gif");
    private BookService_24162056 bookService;
    private AuthorService_24162056 authorService;

    @Override public void init() {
        bookService = new BookServiceImpl_24162056(new JdbcBookDao_24162056());
        authorService = new AuthorServiceImpl_24162056(new JdbcAuthorDao_24162056());
    }

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/admin/books/create".equals(path)) { form(request, response, new Book_24162056()); return; }
        if ("/admin/books/edit".equals(path)) {
            int id = positive(request.getParameter("id"));
            var book = bookService.getById(id);
            if (book.isEmpty()) { response.sendError(404); return; }
            form(request, response, book.get()); return;
        }
        list(request, response);
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/admin/books/delete".equals(path)) {
            bookService.delete(positive(request.getParameter("id")));
            request.getSession().setAttribute("notice", "Đã xoá sách.");
            response.sendRedirect(request.getContextPath() + "/admin/books");
            return;
        }
        try {
            Book_24162056 book = bind(request);
            book.setCoverImage(saveCover(request, book.getCoverImage()));
            List<Integer> authorIds = authorIds(request);
            if (book.getTitle() == null || book.getTitle().isBlank() || authorIds.isEmpty()) throw new IllegalArgumentException("Vui lòng nhập tiêu đề và chọn ít nhất một tác giả.");
            if ("/admin/books/edit".equals(path)) {
                book.setBookId(positive(request.getParameter("id")));
                bookService.update(book, authorIds);
                request.getSession().setAttribute("notice", "Đã cập nhật sách.");
            } else {
                bookService.create(book, authorIds);
                request.getSession().setAttribute("notice", "Đã thêm sách mới.");
            }
            response.sendRedirect(request.getContextPath() + "/admin/books");
        } catch (RuntimeException exception) {
            request.setAttribute("error", exception.getMessage());
            form(request, response, safeBind(request));
        }
    }

    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = Math.max(1, positive(request.getParameter("page")));
        long total = bookService.countBooks();
        int pages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));
        if (page > pages) page = pages;
        request.setAttribute("books", bookService.getPage(page, PAGE_SIZE));
        request.setAttribute("page", page); request.setAttribute("totalPages", pages); request.setAttribute("pageTitle", "Quản lý sách");
        request.getRequestDispatcher("/WEB-INF/views/admin/books/list.jsp").forward(request, response);
    }

    private void form(HttpServletRequest request, HttpServletResponse response, Book_24162056 book) throws ServletException, IOException {
        request.setAttribute("book", book);
        request.setAttribute("authors", authorService.getAll());
        request.setAttribute("pageTitle", book.getBookId() == 0 ? "Thêm sách" : "Cập nhật sách");
        request.getRequestDispatcher("/WEB-INF/views/admin/books/form.jsp").forward(request, response);
    }

    private Book_24162056 bind(HttpServletRequest request) {
        Book_24162056 book = new Book_24162056();
        book.setIsbn(integer(request.getParameter("isbn"))); book.setTitle(text(request, "title")); book.setPublisher(text(request, "publisher"));
        book.setPrice(decimal(request.getParameter("price"))); book.setDescription(text(request, "description")); book.setCoverImage(text(request, "existingCoverImage"));
        book.setQuantity(integer(request.getParameter("quantity"))); String date = text(request, "publishDate");
        if (date != null && !date.isBlank()) book.setPublishDate(LocalDate.parse(date));
        return book;
    }
    private Book_24162056 safeBind(HttpServletRequest request) { try { return bind(request); } catch (RuntimeException exception) { return new Book_24162056(); } }
    private String saveCover(HttpServletRequest request, String existingCover) {
        try {
            Part file = request.getPart("coverFile");
            if (file == null || file.getSize() == 0) return existingCover;
            if (!IMAGE_TYPES.contains(file.getContentType())) throw new IllegalArgumentException("Chỉ chấp nhận ảnh JPG, PNG, WEBP hoặc GIF.");
            String extension = switch (file.getContentType()) { case "image/png" -> ".png"; case "image/webp" -> ".webp"; case "image/gif" -> ".gif"; default -> ".jpg"; };
            Path directory = Path.of(getServletContext().getRealPath("/assets/images"));
            Files.createDirectories(directory);
            String fileName = "book-" + UUID.randomUUID() + extension;
            try (var input = file.getInputStream()) { Files.copy(input, directory.resolve(fileName)); }
            return "assets/images/" + fileName;
        } catch (IOException | ServletException exception) {
            throw new IllegalArgumentException("Không thể tải ảnh bìa lên.", exception);
        }
    }
    private List<Integer> authorIds(HttpServletRequest request) { List<Integer> ids = new ArrayList<>(); String[] values = request.getParameterValues("authorIds"); if (values != null) for (String value : values) { int id = positive(value); if (id > 0) ids.add(id); } return ids; }
    private String text(HttpServletRequest request, String name) { String value = request.getParameter(name); return value == null ? null : value.trim(); }
    private int positive(String value) { try { return Integer.parseInt(value); } catch (RuntimeException exception) { return 0; } }
    private Integer integer(String value) { int result = positive(value); return result == 0 ? null : result; }
    private BigDecimal decimal(String value) { try { return value == null || value.isBlank() ? null : new BigDecimal(value); } catch (NumberFormatException exception) { throw new IllegalArgumentException("Giá sách chưa hợp lệ."); } }
}
