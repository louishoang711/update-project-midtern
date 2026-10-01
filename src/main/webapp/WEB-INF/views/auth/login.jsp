<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><title>Đăng nhập</title></head><body><section class="auth-section"><div class="auth-card">
    <span class="eyebrow">TÀI KHOẢN</span><h1>Đăng nhập</h1><p class="muted">Chào mừng bạn trở lại BookStore.</p>
    <c:if test="${param.required == 'admin'}"><div class="notice warning">Vui lòng đăng nhập bằng tài khoản quản trị.</div></c:if>
    <c:if test="${not empty error}"><div class="notice warning"><c:out value="${error}"/></div></c:if>
    <c:if test="${not empty sessionScope.notice}"><div class="notice success"><c:out value="${sessionScope.notice}"/></div><c:remove var="notice" scope="session"/></c:if>
    <form action="${pageContext.request.contextPath}/login" method="post"><label>Email<input type="email" name="email" placeholder="name@example.com" required autofocus></label><label>Mật khẩu<input type="password" name="password" required></label><button class="button primary full" type="submit">Đăng nhập</button></form>
    <p class="auth-link">Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a></p>
</div></section></body></html>
