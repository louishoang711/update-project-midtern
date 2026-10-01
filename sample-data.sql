USE BookStore;
GO

/* Dữ liệu kiểm tra an toàn: chỉ thêm bản ghi chưa có, không xoá dữ liệu hiện hữu. */
IF NOT EXISTS (SELECT 1 FROM dbo.users WHERE email = 'admin@bookstore.vn')
    INSERT INTO dbo.users (email, fullname, phone, passwd, is_admin)
    VALUES ('admin@bookstore.vn', N'Quản trị viên', 901112233, 'e10adc3949ba59abbe56e057f20f883e', 1);

IF NOT EXISTS (SELECT 1 FROM dbo.users WHERE email = 'user@bookstore.vn')
    INSERT INTO dbo.users (email, fullname, phone, passwd, is_admin)
    VALUES ('user@bookstore.vn', N'Người dùng mẫu', 909998877, 'e10adc3949ba59abbe56e057f20f883e', 0);

IF NOT EXISTS (SELECT 1 FROM dbo.author WHERE author_name = 'Nguyen Nhat Anh')
    INSERT INTO dbo.author (author_name, date_of_birth) VALUES ('Nguyen Nhat Anh', '1955-05-07');
IF NOT EXISTS (SELECT 1 FROM dbo.author WHERE author_name = 'Nam Cao')
    INSERT INTO dbo.author (author_name, date_of_birth) VALUES ('Nam Cao', '1915-10-29');
IF NOT EXISTS (SELECT 1 FROM dbo.author WHERE author_name = 'To Hoai')
    INSERT INTO dbo.author (author_name, date_of_birth) VALUES ('To Hoai', '1920-09-27');

IF NOT EXISTS (SELECT 1 FROM dbo.books WHERE isbn = 100001)
    INSERT INTO dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
    VALUES (100001, 'Mat biec', 'NXB Tre', 8500.00, 'Tieu thuyet tuoi moi lon.', '2019-01-01', 'assets/images/mat-biec.jpg', 20);
IF NOT EXISTS (SELECT 1 FROM dbo.books WHERE isbn = 100002)
    INSERT INTO dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
    VALUES (100002, 'Cho toi xin mot ve di tuoi tho', 'NXB Tre', 9000.00, 'Cau chuyen ve tuoi tho.', '2018-06-01', 'assets/images/ve-di-tuoi-tho.jpg', 15);
IF NOT EXISTS (SELECT 1 FROM dbo.books WHERE isbn = 100003)
    INSERT INTO dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
    VALUES (100003, 'Chi Pheo', 'NXB Van Hoc', 6000.00, 'Tac pham hien thuc Viet Nam.', '2017-02-01', 'assets/images/chi-pheo.jpg', 12);
IF NOT EXISTS (SELECT 1 FROM dbo.books WHERE isbn = 100004)
    INSERT INTO dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
    VALUES (100004, 'De Men phieu luu ky', 'NXB Kim Dong', 7500.00, 'Tac pham thieu nhi kinh dien.', '2020-08-01', 'assets/images/de-men.jpg', 30);

INSERT INTO dbo.book_author (bookid, author_id)
SELECT b.bookid, a.author_id FROM dbo.books b CROSS JOIN dbo.author a
WHERE (b.isbn = 100001 OR b.isbn = 100002) AND a.author_name = 'Nguyen Nhat Anh'
  AND NOT EXISTS (SELECT 1 FROM dbo.book_author ba WHERE ba.bookid = b.bookid AND ba.author_id = a.author_id);
INSERT INTO dbo.book_author (bookid, author_id)
SELECT b.bookid, a.author_id FROM dbo.books b CROSS JOIN dbo.author a
WHERE b.isbn = 100003 AND a.author_name = 'Nam Cao'
  AND NOT EXISTS (SELECT 1 FROM dbo.book_author ba WHERE ba.bookid = b.bookid AND ba.author_id = a.author_id);
INSERT INTO dbo.book_author (bookid, author_id)
SELECT b.bookid, a.author_id FROM dbo.books b CROSS JOIN dbo.author a
WHERE b.isbn = 100004 AND a.author_name = 'To Hoai'
  AND NOT EXISTS (SELECT 1 FROM dbo.book_author ba WHERE ba.bookid = b.bookid AND ba.author_id = a.author_id);

IF NOT EXISTS (SELECT 1 FROM dbo.rating r JOIN dbo.users u ON u.id = r.userid JOIN dbo.books b ON b.bookid = r.bookid WHERE u.email = 'user@bookstore.vn' AND b.isbn = 100001)
    INSERT INTO dbo.rating (userid, bookid, rating, review_text)
    SELECT u.id, b.bookid, 5, N'Sách rất hay và giàu cảm xúc.' FROM dbo.users u CROSS JOIN dbo.books b
    WHERE u.email = 'user@bookstore.vn' AND b.isbn = 100001;
GO
