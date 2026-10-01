<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title"/> · Quản trị ${applicationScope.appName}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=5">
    <sitemesh:write property="head"/>
</head>
<body class="admin-body">
<div class="admin-shell">
    <aside class="admin-sidebar">
        <a class="brand brand-admin" href="${pageContext.request.contextPath}/admin/dashboard">
            <span class="brand-mark">B</span>
            <span>BookStore Admin</span>
        </a>
        <p class="nav-caption">QUẢN LÝ</p>
        <nav class="admin-nav">
            <a href="${pageContext.request.contextPath}/admin/dashboard">Tổng quan</a>
            <a href="${pageContext.request.contextPath}/admin/books">Quản lý sách</a>
            <a href="${pageContext.request.contextPath}/home">Xem trang người dùng</a>
        </nav>
        <div class="admin-user">
            <span class="avatar">A</span>
            <div>
                <strong><c:out value="${sessionScope.currentUser.fullName}"/></strong>
                <small>Administrator</small>
            </div>
        </div>
    </aside>
    <section class="admin-main">
        <header class="admin-topbar">
            <span>Khu vực quản trị</span>
            <span>MSSV ${applicationScope.studentId} · Đề ${applicationScope.examCode}</span>
        </header>
        <main class="admin-content">
            <sitemesh:write property="body"/>
        </main>
    </section>
</div>
</body>
</html>
