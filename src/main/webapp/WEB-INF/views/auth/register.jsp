<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><title>Đăng ký</title></head><body><section class="auth-section"><div class="auth-card">
    <span class="eyebrow">TÀI KHOẢN MỚI</span><h1>Đăng ký</h1><p class="muted">Chúng tôi sẽ gửi mã xác thực đến email của bạn.</p>
    <c:if test="${not empty error}"><div class="notice warning"><c:out value="${error}"/></div></c:if>
    <form action="${pageContext.request.contextPath}/register" method="post"><label>Họ và tên<input name="fullName" required maxlength="50" value="<c:out value='${param.fullName}'/>"></label><label>Email<input type="email" name="email" required maxlength="50" value="<c:out value='${param.email}'/>"></label><label>Số điện thoại<input type="tel" name="phone" inputmode="numeric" value="<c:out value='${param.phone}'/>"></label><label>Mật khẩu<input type="password" name="password" minlength="6" required></label><label>Xác nhận mật khẩu<input type="password" name="confirmPassword" minlength="6" required></label><button class="button primary full" type="submit">Nhận mã xác thực</button></form>
    <p class="auth-link">Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></p>
</div></section></body></html>
