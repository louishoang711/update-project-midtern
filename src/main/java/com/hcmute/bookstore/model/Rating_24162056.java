package com.hcmute.bookstore.model;

public class Rating_24162056 {
    private int userId;
    private int bookId;
    private Byte rating;
    private String reviewText;

    public Rating_24162056() {
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public Byte getRating() { return rating; }
    public void setRating(Byte rating) { this.rating = rating; }
    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
}
