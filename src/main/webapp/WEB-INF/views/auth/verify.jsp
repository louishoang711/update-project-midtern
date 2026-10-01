<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><title>Xác thực OTP</title></head><body><section class="auth-section"><div class="auth-card">
    <span class="eyebrow">XÁC THỰC EMAIL</span><h1>Nhập mã OTP</h1><p class="muted">Mã gồm 6 chữ số đã được gửi đến <strong><c:out value="${sessionScope.pendingUser.email}"/></strong> và có hiệu lực trong 5 phút.</p>
    <c:if test="${not empty sessionScope.notice}"><div class="notice success"><c:out value="${sessionScope.notice}"/></div><c:remove var="notice" scope="session"/></c:if><c:if test="${not empty error}"><div class="notice warning"><c:out value="${error}"/></div></c:if>
    <form action="${pageContext.request.contextPath}/verify-otp" method="post"><label>Mã OTP<input class="otp-input" name="otp" inputmode="numeric" pattern="[0-9]{6}" maxlength="6" required autofocus></label><button class="button primary full" type="submit">Xác nhận</button></form>
</div></section></body></html>
