<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html lang="vi"><head><title><c:out value="${book.title}"/></title></head><body>
<section class="standard-page"><div class="container">
    <c:if test="${not empty sessionScope.notice}"><div class="notice success"><c:out value="${sessionScope.notice}"/></div><c:remove var="notice" scope="session"/></c:if>
    <div class="detail-grid"><div class="detail-cover"><c:choose><c:when test="${not empty book.coverImage}"><img src="<c:out value='${book.coverImage}'/>" alt="Bìa <c:out value='${book.title}'/>"/></c:when><c:otherwise>SÁCH</c:otherwise></c:choose></div>
    <article class="detail-info"><span class="book-isbn">MÃ ISBN: <c:out value="${book.isbn}"/></span><h1><c:out value="${book.title}"/></h1><p class="lead"><c:out value="${book.description}"/></p>
        <dl><dt>Tác giả</dt><dd><c:forEach items="${book.authors}" var="author" varStatus="loop"><c:out value="${author.authorName}"/><c:if test="${!loop.last}">, </c:if></c:forEach></dd><dt>Nhà xuất bản</dt><dd><c:out value="${book.publisher}"/></dd><dt>Ngày xuất bản</dt><dd><c:out value="${book.publishDate}"/></dd><dt>Giá bán</dt><dd><fmt:formatNumber value="${book.price}" type="number"/> đ</dd><dt>Số lượng</dt><dd><c:out value="${book.quantity}"/></dd><dt>Đánh giá</dt><dd><c:out value="${book.reviewCount}"/> lượt</dd></dl>
        <div class="detail-purchase">
            <c:choose>
                <c:when test="${not empty sessionScope.currentUser and not sessionScope.currentUser.admin and book.quantity gt 0}">
                    <form action="${pageContext.request.contextPath}/cart/add" method="post">
                        <input type="hidden" name="bookId" value="${book.bookId}"/>
                        <label>Số lượng
                            <input type="number" name="quantity" value="1" min="1" max="${book.quantity}" required/>
                        </label>
                        <button class="button primary" type="submit">Thêm vào giỏ</button>
                    </form>
                </c:when>
                <c:when test="${empty sessionScope.currentUser}">
                    <a class="button primary" href="${pageContext.request.contextPath}/login">Đăng nhập để mua sách</a>
                </c:when>
                <c:when test="${book.quantity le 0}"><span class="sold-out">Sách đã hết hàng</span></c:when>
            </c:choose>
        </div>
    </article></div>
    <section class="reviews"><h2>Đánh giá</h2><c:choose><c:when test="${not empty reviews}"><c:forEach items="${reviews}" var="review"><article class="review"><strong><c:out value="${review.rating}"/> / 5</strong><p><c:out value="${review.reviewText}"/></p></article></c:forEach></c:when><c:otherwise><p class="muted">Chưa có đánh giá nào.</p></c:otherwise></c:choose>
    <c:choose><c:when test="${not empty sessionScope.currentUser}"><form class="review-form" action="${pageContext.request.contextPath}/book/review" method="post"><h3>Viết đánh giá</h3><input type="hidden" name="bookId" value="${book.bookId}"/><label>Số sao<select name="rating"><option value="5">5 - Rất hay</option><option value="4">4 - Hay</option><option value="3">3 - Bình thường</option><option value="2">2 - Chưa tốt</option><option value="1">1 - Không phù hợp</option></select></label><label>Nhận xét<textarea name="reviewText" required maxlength="1000"></textarea></label><button class="button primary" type="submit">Gửi đánh giá</button></form></c:when><c:otherwise><p class="notice warning">Hãy <a href="${pageContext.request.contextPath}/login">đăng nhập</a> để viết đánh giá.</p></c:otherwise></c:choose>
    </section>
</div></section></body></html>
