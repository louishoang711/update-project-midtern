USE BookStore;
GO

/* Chạy file này cho database hiện có. Script không xoá dữ liệu cũ. */
IF OBJECT_ID(N'dbo.orders', N'U') IS NULL
BEGIN
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
END;
GO

IF OBJECT_ID(N'dbo.order_items', N'U') IS NULL
BEGIN
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
END;
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.indexes
    WHERE name = N'IX_orders_user_status_created'
      AND object_id = OBJECT_ID(N'dbo.orders')
)
BEGIN
    CREATE INDEX IX_orders_user_status_created
        ON dbo.orders(user_id, status, created_at DESC);
END;
GO
