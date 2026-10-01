<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head><title>Thanh toán COD</title></head>
<body>
<section class="standard-page">
    <div class="container">
        <div class="page-heading"><div><span class="eyebrow">THANH TOÁN</span><h1>Xác nhận đơn hàng</h1></div></div>
        <c:if test="${not empty error}"><div class="notice warning"><c:out value="${error}"/></div></c:if>

        <div class="checkout-layout">
            <form class="checkout-form" action="${pageContext.request.contextPath}/checkout/place" method="post">
                <h2>Thông tin nhận hàng</h2>
                <label>Họ và tên
                    <input type="text" name="receiverName" value="<c:out value='${checkoutForm.receiverName}'/>" maxlength="100" required/>
                </label>
                <label>Số điện thoại
                    <input type="tel" name="receiverPhone" value="<c:out value='${checkoutForm.receiverPhone}'/>" maxlength="20" placeholder="0912345678" required/>
                </label>
                <label>Địa chỉ giao hàng
                    <textarea name="shippingAddress" maxlength="255" required><c:out value="${checkoutForm.shippingAddress}"/></textarea>
                </label>
                <div class="payment-method">
                    <span class="payment-icon">COD</span>
                    <div><strong>Thanh toán khi nhận hàng</strong><small>Thanh toán bằng tiền mặt khi đơn hàng được giao tới bạn.</small></div>
                </div>
                <div class="form-actions">
                    <a class="button secondary" href="${pageContext.request.contextPath}/cart">Quay lại giỏ</a>
                    <button class="button primary" type="submit">Đặt hàng COD</button>
                </div>
            </form>

            <aside class="order-summary">
                <h2>Đơn hàng</h2>
                <c:forEach items="${cart.items}" var="item">
                    <div class="order-line">
                        <div><strong><c:out value="${item.book.title}"/></strong><small>Số lượng: ${item.quantity}</small></div>
                        <span><fmt:formatNumber value="${item.subtotal}" type="number"/> đ</span>
                    </div>
                </c:forEach>
                <div class="order-total"><span>Tổng cộng</span><strong><fmt:formatNumber value="${cart.totalAmount}" type="number"/> đ</strong></div>
            </aside>
        </div>
    </div>
</section>
</body>
</html>
