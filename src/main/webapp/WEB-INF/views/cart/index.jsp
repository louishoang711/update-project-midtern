<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head><title>Giỏ hàng</title></head>
<body>
<section class="standard-page">
    <div class="container">
        <div class="page-heading">
            <div><span class="eyebrow">MUA SẮM</span><h1>Giỏ hàng của bạn</h1></div>
            <span class="count-pill">${cart.totalQuantity} sản phẩm</span>
        </div>

        <c:if test="${not empty cartMessage}"><div class="notice success"><c:out value="${cartMessage}"/></div></c:if>
        <c:if test="${not empty cartError}"><div class="notice warning"><c:out value="${cartError}"/></div></c:if>

        <c:choose>
            <c:when test="${cart.totalQuantity gt 0}">
                <div class="cart-layout">
                    <div class="table-wrap cart-table">
                        <table>
                            <thead><tr><th>Sách</th><th>Đơn giá</th><th>Số lượng</th><th>Thành tiền</th><th></th></tr></thead>
                            <tbody>
                            <c:forEach items="${cart.items}" var="item">
                                <tr>
                                    <td>
                                        <div class="cart-product">
                                            <c:if test="${not empty item.book.coverImage}">
                                                <c:url var="coverUrl" value="/${item.book.coverImage}"/>
                                                <img src="${coverUrl}" alt="Bìa <c:out value='${item.book.title}'/>"/>
                                            </c:if>
                                            <div><strong><c:out value="${item.book.title}"/></strong><small>ISBN: <c:out value="${item.book.isbn}"/></small></div>
                                        </div>
                                    </td>
                                    <td><fmt:formatNumber value="${item.book.price}" type="number"/> đ</td>
                                    <td>
                                        <form class="quantity-form" action="${pageContext.request.contextPath}/cart/update" method="post">
                                            <input type="hidden" name="bookId" value="${item.book.bookId}"/>
                                            <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.book.quantity}" required/>
                                            <button type="submit">Cập nhật</button>
                                        </form>
                                        <small class="stock-note">Tối đa ${item.book.quantity}</small>
                                    </td>
                                    <td><strong><fmt:formatNumber value="${item.subtotal}" type="number"/> đ</strong></td>
                                    <td class="actions">
                                        <form action="${pageContext.request.contextPath}/cart/remove" method="post">
                                            <input type="hidden" name="bookId" value="${item.book.bookId}"/>
                                            <button type="submit">Xóa</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <aside class="cart-summary">
                        <span class="eyebrow">TÓM TẮT</span>
                        <div><span>Tổng số lượng</span><strong>${cart.totalQuantity}</strong></div>
                        <div class="cart-total"><span>Tổng thanh toán</span><strong><fmt:formatNumber value="${cart.totalAmount}" type="number"/> đ</strong></div>
                        <a class="button primary full" href="${pageContext.request.contextPath}/checkout">Thanh toán COD</a>
                        <a class="button secondary full" href="${pageContext.request.contextPath}/home">Tiếp tục mua sách</a>
                        <form action="${pageContext.request.contextPath}/cart/clear" method="post">
                            <button class="clear-cart" type="submit">Xóa toàn bộ giỏ</button>
                        </form>
                    </aside>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <strong>Giỏ hàng đang trống</strong>
                    <span>Hãy chọn một cuốn sách bạn yêu thích.</span>
                    <a class="button primary" href="${pageContext.request.contextPath}/home">Xem sách</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>
</body>
</html>
