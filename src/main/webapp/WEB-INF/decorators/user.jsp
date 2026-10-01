<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title"/> · ${applicationScope.appName}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=6">
    <sitemesh:write property="head"/>
</head>
<body>
<header class="site-header">
    <div class="container nav-wrap">
        <a class="brand" href="${pageContext.request.contextPath}/home" aria-label="BookStore - Trang chủ">
            <span class="brand-mark">B</span>
            <span>${applicationScope.appName}</span>
        </a>
        <nav class="main-nav" aria-label="Điều hướng chính">
            <a href="${pageContext.request.contextPath}/home">Trang Chủ</a>
            <a href="${pageContext.request.contextPath}/books">Sản phẩm</a>
            <c:if test="${not empty sessionScope.currentUser and not sessionScope.currentUser.admin}">
                <a class="cart-link" href="${pageContext.request.contextPath}/cart">
                    Giỏ hàng <span>${empty sessionScope.shoppingCart ? 0 : sessionScope.shoppingCart.totalQuantity}</span>
                </a>
            </c:if>
            <c:choose>
                <c:when test="${not empty sessionScope.currentUser}">
                    <span class="welcome">Xin chào, <c:out value="${sessionScope.currentUser.fullName}"/></span>
                    <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                </c:otherwise>
            </c:choose>
            <c:if test="${not empty sessionScope.currentUser and sessionScope.currentUser.admin}">
                <a class="admin-link" href="${pageContext.request.contextPath}/admin/dashboard">Trang quản trị</a>
            </c:if>
        </nav>
    </div>
</header>

<main class="page-content">
    <sitemesh:write property="body"/>
</main>

<footer class="site-footer">
    <div class="container footer-wrap">
        <div>
            <strong>${applicationScope.appName}</strong>
            <p>Bài thi Quá trình môn Lập trình Web</p>
        </div>
        <div class="student-info">
            <span>MSSV: <strong>${applicationScope.studentId}</strong></span>
            <span>Mã đề: <strong>${applicationScope.examCode}</strong></span>
        </div>
    </div>
</footer>
</body>
</html>
