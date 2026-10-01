package com.hcmute.bookstore.controller;

import java.io.IOException;

import com.hcmute.bookstore.dao.impl.JdbcRatingDao_24162056;
import com.hcmute.bookstore.model.Rating_24162056;
import com.hcmute.bookstore.model.User_24162056;
import com.hcmute.bookstore.service.RatingService_24162056;
import com.hcmute.bookstore.service.impl.RatingServiceImpl_24162056;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ReviewController_24162056", urlPatterns = "/book/review")
public class ReviewController_24162056 extends HttpServlet {
    private RatingService_24162056 ratingService;
    @Override public void init() { ratingService = new RatingServiceImpl_24162056(new JdbcRatingDao_24162056()); }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookId;
        try { bookId = Integer.parseInt(request.getParameter("bookId")); } catch (RuntimeException exception) { response.sendError(400); return; }
        User_24162056 user = (User_24162056) request.getSession().getAttribute("currentUser");
        if (user == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
        String text = request.getParameter("reviewText");
        try {
            byte stars = Byte.parseByte(request.getParameter("rating"));
            if (stars < 1 || stars > 5 || text == null || text.isBlank()) throw new IllegalArgumentException();
            Rating_24162056 review = new Rating_24162056();
            review.setBookId(bookId); review.setUserId(user.getId()); review.setRating(stars); review.setReviewText(text.trim());
            ratingService.save(review);
            request.getSession().setAttribute("notice", "Đánh giá của bạn đã được lưu.");
        } catch (RuntimeException exception) { request.getSession().setAttribute("notice", "Không thể lưu đánh giá. Vui lòng kiểm tra lại nội dung."); }
        response.sendRedirect(request.getContextPath() + "/book?id=" + bookId);
    }
}
