<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head><title>Đặt hàng thành công</title></head>
<body>
<section class="success-section">
    <div class="success-card">
        <span class="success-mark">✓</span>
        <span class="eyebrow">ĐẶT HÀNG THÀNH CÔNG</span>
        <h1>Cảm ơn bạn đã đặt sách</h1>
        <p>Đơn hàng <strong>#${order.orderId}</strong> đã được ghi nhận và đang chờ xác nhận.</p>
        <dl>
            <dt>Người nhận</dt><dd><c:out value="${order.receiverName}"/></dd>
            <dt>Số điện thoại</dt><dd><c:out value="${order.receiverPhone}"/></dd>
            <dt>Địa chỉ</dt><dd><c:out value="${order.shippingAddress}"/></dd>
            <dt>Thanh toán</dt><dd>COD</dd>
            <dt>Tổng tiền</dt><dd><strong><fmt:formatNumber value="${order.totalAmount}" type="number"/> đ</strong></dd>
        </dl>
        <a class="button primary" href="${pageContext.request.contextPath}/home">Tiếp tục mua sách</a>
    </div>
</section>
</body>
</html>
