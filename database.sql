USE master;
GO

IF DB_ID(N'BookStore') IS NULL
BEGIN
    CREATE DATABASE BookStore;
END;
GO

USE BookStore;
GO

IF OBJECT_ID(N'dbo.order_items', N'U') IS NOT NULL DROP TABLE dbo.order_items;
IF OBJECT_ID(N'dbo.orders', N'U') IS NOT NULL DROP TABLE dbo.orders;
IF OBJECT_ID(N'dbo.rating', N'U') IS NOT NULL DROP TABLE dbo.rating;
IF OBJECT_ID(N'dbo.book_author', N'U') IS NOT NULL DROP TABLE dbo.book_author;
IF OBJECT_ID(N'dbo.books', N'U') IS NOT NULL DROP TABLE dbo.books;
IF OBJECT_ID(N'dbo.author', N'U') IS NOT NULL DROP TABLE dbo.author;
IF OBJECT_ID(N'dbo.users', N'U') IS NOT NULL DROP TABLE dbo.users;
GO

CREATE TABLE dbo.books (
    bookid INT IDENTITY(1, 1) NOT NULL,
    isbn INT NULL,
    title VARCHAR(200) NULL,
    publisher VARCHAR(100) NULL,
    price DECIMAL(6, 2) NULL,
    description TEXT NULL,
    publish_date DATE NULL,
    cover_image VARCHAR(100) NULL,
    quantity INT NULL,
    CONSTRAINT PK_books PRIMARY KEY (bookid),
    CONSTRAINT CK_books_price CHECK (price IS NULL OR price >= 0),
    CONSTRAINT CK_books_quantity CHECK (quantity IS NULL OR quantity >= 0)
);
GO

CREATE TABLE dbo.users (
    id INT IDENTITY(1, 1) NOT NULL,
    email VARCHAR(50) NOT NULL,
    fullname NVARCHAR(50) NULL,
    phone INT NULL,
    passwd VARCHAR(32) NOT NULL,
    signup_date DATETIME NULL CONSTRAINT DF_users_signup_date DEFAULT GETDATE(),
    last_login DATETIME NULL,
    is_admin BIT NULL CONSTRAINT DF_users_is_admin DEFAULT 0,
    CONSTRAINT PK_users PRIMARY KEY (id),
    CONSTRAINT UQ_users_email UNIQUE (email)
);
GO

CREATE TABLE dbo.author (
    author_id INT IDENTITY(1, 1) NOT NULL,
    author_name VARCHAR(100) NULL,
    date_of_birth DATE NULL,
    CONSTRAINT PK_author PRIMARY KEY (author_id)
);
GO

CREATE TABLE dbo.book_author (
    bookid INT NOT NULL,
    author_id INT NOT NULL,
    CONSTRAINT PK_book_author PRIMARY KEY (bookid, author_id),
    CONSTRAINT FK_book_author_books FOREIGN KEY (bookid)
        REFERENCES dbo.books(bookid) ON DELETE CASCADE,
    CONSTRAINT FK_book_author_author FOREIGN KEY (author_id)
        REFERENCES dbo.author(author_id) ON DELETE CASCADE
);
GO

CREATE TABLE dbo.rating (
    userid INT NOT NULL,
    bookid INT NOT NULL,
    rating TINYINT NULL,
    review_text TEXT NULL,
    CONSTRAINT PK_rating PRIMARY KEY (userid, bookid),
    CONSTRAINT FK_rating_users FOREIGN KEY (userid)
        REFERENCES dbo.users(id) ON DELETE CASCADE,
    CONSTRAINT FK_rating_books FOREIGN KEY (bookid)
        REFERENCES dbo.books(bookid) ON DELETE CASCADE,
    CONSTRAINT CK_rating_value CHECK (rating IS NULL OR rating BETWEEN 1 AND 5)
);
GO

CREATE TABLE dbo.orders (
    order_id INT IDENTITY(1, 1) NOT NULL,
    user_id INT NOT NULL,
    receiver_name NVARCHAR(100) NOT NULL,
    receiver_phone VARCHAR(20) NOT NULL,
    shipping_address NVARCHAR(255) NOT NULL,
    total_amount DECIMAL(12, 2) NOT NULL,
    payment_method VARCHAR(10) NOT NULL CONSTRAINT DF_orders_payment_method DEFAULT 'COD',
    status VARCHAR(20) NOT NULL CONSTRAINT DF_orders_status DEFAULT 'NEW',
    created_at DATETIME NOT NULL CONSTRAINT DF_orders_created_at DEFAULT GETDATE(),
    CONSTRAINT PK_orders PRIMARY KEY (order_id),
    CONSTRAINT FK_orders_users FOREIGN KEY (user_id) REFERENCES dbo.users(id),
    CONSTRAINT CK_orders_total CHECK (total_amount >= 0),
    CONSTRAINT CK_orders_payment_method CHECK (payment_method IN ('COD')),
    CONSTRAINT CK_orders_status CHECK (status IN (
        'NEW', 'CONFIRMED', 'PREPARING', 'SHIPPING',
        'DELIVERING', 'DELIVERED', 'CANCELLED', 'RETURNED'
    ))
);
GO

CREATE TABLE dbo.order_items (
    order_item_id INT IDENTITY(1, 1) NOT NULL,
    order_id INT NOT NULL,
    book_id INT NULL,
    book_title NVARCHAR(200) NOT NULL,
    unit_price DECIMAL(12, 2) NOT NULL,
    quantity INT NOT NULL,
    line_total DECIMAL(12, 2) NOT NULL,
    CONSTRAINT PK_order_items PRIMARY KEY (order_item_id),
    CONSTRAINT FK_order_items_orders FOREIGN KEY (order_id)
        REFERENCES dbo.orders(order_id) ON DELETE CASCADE,
    CONSTRAINT FK_order_items_books FOREIGN KEY (book_id)
        REFERENCES dbo.books(bookid) ON DELETE SET NULL,
    CONSTRAINT CK_order_items_price CHECK (unit_price >= 0),
    CONSTRAINT CK_order_items_quantity CHECK (quantity > 0),
    CONSTRAINT CK_order_items_total CHECK (line_total >= 0)
);
GO

CREATE INDEX IX_orders_user_status_created
    ON dbo.orders(user_id, status, created_at DESC);
GO

INSERT INTO dbo.users (email, fullname, phone, passwd, is_admin)
VALUES
    ('admin@bookstore.vn', N'Quản trị viên', 901112233, 'e10adc3949ba59abbe56e057f20f883e', 1),
    ('user@bookstore.vn', N'Người dùng mẫu', 909998877, 'e10adc3949ba59abbe56e057f20f883e', 0);

INSERT INTO dbo.author (author_name, date_of_birth)
VALUES
    ('Nguyen Nhat Anh', '1955-05-07'),
    ('Nam Cao', '1915-10-29'),
    ('To Hoai', '1920-09-27');

INSERT INTO dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
VALUES
    (100001, 'Mat biec', 'NXB Tre', 8500.00, 'Tieu thuyet tuoi moi lon.', '2019-01-01', 'assets/images/mat-biec.jpg', 20),
    (100002, 'Cho toi xin mot ve di tuoi tho', 'NXB Tre', 9000.00, 'Cau chuyen ve tuoi tho.', '2018-06-01', 'assets/images/ve-di-tuoi-tho.jpg', 15),
    (100003, 'Chi Pheo', 'NXB Van Hoc', 6000.00, 'Tac pham hien thuc Viet Nam.', '2017-02-01', 'assets/images/chi-pheo.jpg', 12),
    (100004, 'De Men phieu luu ky', 'NXB Kim Dong', 7500.00, 'Tac pham thieu nhi kinh dien.', '2020-08-01', 'assets/images/de-men.jpg', 30);

INSERT INTO dbo.book_author (bookid, author_id)
VALUES (1, 1), (2, 1), (3, 2), (4, 3);

INSERT INTO dbo.rating (userid, bookid, rating, review_text)
VALUES (2, 1, 5, N'Sách rất hay và giàu cảm xúc.');
GO
