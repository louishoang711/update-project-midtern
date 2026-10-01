# BookStore - Đề 02 - MSSV 24162056

Project Maven WAR sử dụng Servlet, JDBC, JSP, JSTL, SiteMesh 3 và SQL Server.

## Chức năng đã có

- MVC, Service và DAO/JDBC tách theo ba tầng; dữ liệu gồm `books`, `users`, `author`, `book_author`, `rating`.
- Giao diện SiteMesh riêng cho người dùng và quản trị viên, quyền quản trị kiểm tra từ Session.
- Đăng ký qua OTP email, đăng nhập, đăng xuất và Session. Khi chưa cấu hình SMTP, mã OTP được in trong Console Tomcat để kiểm tra luồng; đặt `mail.enabled=true` cùng thông tin SMTP để gửi email thật.
- Danh sách sách phân trang 3 sách/trang theo tác giả; xem chi tiết và thêm/cập nhật đánh giá khi đã đăng nhập.
- CRUD Books có phân trang tại `/admin/books`.
- Tất cả class, interface, controller và service đều có hậu tố `_24162056`.

## Chạy project

1. Với database mới: mở SQL Server Management Studio và chạy `database.sql`. Nếu bạn đã tạo bảng và muốn giữ dữ liệu hiện có, chạy `sample-data.sql` để thêm dữ liệu kiểm tra an toàn.
2. Cập nhật `database.username` và `database.password` trong `src/main/resources/application.properties`.
3. Chạy kiểm thử và đóng gói:

   ```powershell
   .\mvnw.cmd clean package
   ```

4. Deploy `target/bookstore-24162056.war` lên Tomcat 10.1 trở lên.
5. Mở `http://localhost:8081/bookstore-24162056/`.

Tài khoản mẫu sau khi chạy `database.sql`: `admin@bookstore.vn` / `123456` (quản trị) và `user@bookstore.vn` / `123456` (người dùng).
"# update-project-midtern" 
