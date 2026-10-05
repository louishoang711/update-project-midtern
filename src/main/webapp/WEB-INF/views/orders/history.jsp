<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head><title>Lịch sử đặt hàng</title></head>
<body>
<section class="standard-page">
    <div class="container">
        <div class="page-heading">
            <div>
                <span class="eyebrow">ĐƠN HÀNG CỦA TÔI</span>
                <h1>Lịch sử đặt hàng</h1>
            </div>
            <span class="count-pill">${totalOrders} đơn hàng</span>
        </div>

        <div class="status-tabs">
            <a class="${empty selectedStatus ? 'active' : ''}"
               href="${pageContext.request.contextPath}/orders">Tất cả</a>
            <c:forEach var="option" items="${statusOptions}">
                <a class="${selectedStatus == option.value ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/orders?status=${option.value}">
                    <c:out value="${option.label}"/>
                </a>
            </c:forEach>
        </div>

        <c:choose>
            <c:when test="${empty orders}">
                <div class="empty-state">
                    <strong>Chưa có đơn hàng phù hợp</strong>
                    <p>Đổi trạng thái lọc hoặc đặt thêm sách để xem lịch sử tại đây.</p>
                    <a class="button primary" href="${pageContext.request.contextPath}/home">Mua sách</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="order-history">
                    <c:forEach var="order" items="${orders}">
                        <article class="history-card">
                            <div class="history-head">
                                <div>
                                    <strong>Đơn hàng #${order.orderId}</strong>
                                    <span>${order.createdAtText}</span>
                                </div>
                                <span class="status-badge status-${order.status}">
                                    <c:out value="${order.statusLabel}"/>
                                </span>
                            </div>
                            <div class="history-body">
                                <div class="receiver-box">
                                    <span>Người nhận</span>
                                    <strong><c:out value="${order.receiverName}"/></strong>
                                    <small><c:out value="${order.receiverPhone}"/></small>
                                    <small><c:out value="${order.shippingAddress}"/></small>
                                </div>
                                <div class="history-items">
                                    <c:forEach var="item" items="${order.items}">
                                        <div>
                                            <span><c:out value="${item.bookTitle}"/></span>
                                            <small>${item.quantity} x <fmt:formatNumber value="${item.unitPrice}" type="number"/> đ</small>
                                            <strong><fmt:formatNumber value="${item.lineTotal}" type="number"/> đ</strong>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="history-foot">
                                <span>Thanh toán COD</span>
                                <strong>Tổng: <fmt:formatNumber value="${order.totalAmount}" type="number"/> đ</strong>
                            </div>
                        </article>
                    </c:forEach>
                </div>

                <c:if test="${totalPages gt 1}">
                    <div class="pagination">
                        <c:url var="prevUrl" value="/orders">
                            <c:if test="${not empty selectedStatus}">
                                <c:param name="status" value="${selectedStatus}"/>
                            </c:if>
                            <c:param name="page" value="${page - 1}"/>
                        </c:url>
                        <c:url var="nextUrl" value="/orders">
                            <c:if test="${not empty selectedStatus}">
                                <c:param name="status" value="${selectedStatus}"/>
                            </c:if>
                            <c:param name="page" value="${page + 1}"/>
                        </c:url>
                        <c:choose>
                            <c:when test="${page gt 1}">
                                <a class="pagination-control" href="${prevUrl}">Trang trước</a>
                            </c:when>
                            <c:otherwise>
                                <span class="pagination-control disabled">Trang trước</span>
                            </c:otherwise>
                        </c:choose>
                        <span class="pagination-control disabled">${page} / ${totalPages}</span>
                        <c:choose>
                            <c:when test="${page lt totalPages}">
                                <a class="pagination-control" href="${nextUrl}">Trang sau</a>
                            </c:when>
                            <c:otherwise>
                                <span class="pagination-control disabled">Trang sau</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
</section>
</body>
</html>
