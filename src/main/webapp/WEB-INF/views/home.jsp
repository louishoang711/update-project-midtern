<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi"><head><title>Trang chủ</title></head><body>
<section class="hero compact-hero"><div class="container">
    <span class="eyebrow">BOOKSTORE</span><h1>Mỗi cuốn sách, một thế giới mới.</h1>
    <p>Chọn tác giả yêu thích để khám phá các đầu sách nổi bật.</p>
</div></section>
<section class="standard-page"><div class="container">
    <c:if test="${not empty databaseError}"><div class="notice warning"><c:out value="${databaseError}"/></div></c:if>
    <div class="section-heading"><span class="eyebrow">TỦ SÁCH</span><h2>Sách theo tác giả</h2></div>
    <div class="author-tabs">
        <c:forEach items="${authors}" var="author"><a class="${author.authorId == selectedAuthorId ? 'active' : ''}" href="${pageContext.request.contextPath}/home?authorId=${author.authorId}"><c:out value="${author.authorName}"/></a></c:forEach>
    </div>
    <c:choose><c:when test="${not empty books}">
        <div class="book-grid"><c:forEach items="${books}" var="book"><article class="book-card">
            <a class="book-cover" href="${pageContext.request.contextPath}/book?id=${book.bookId}"><c:choose><c:when test="${not empty book.coverImage}"><img src="<c:out value='${book.coverImage}'/>" alt="Bìa <c:out value='${book.title}'/>"/></c:when><c:otherwise>SÁCH</c:otherwise></c:choose></a>
            <div class="book-info"><span class="book-isbn">MÃ ISBN: <c:out value="${book.isbn}"/></span><h2><a href="${pageContext.request.contextPath}/book?id=${book.bookId}"><c:out value="${book.title}"/></a></h2>
                <p><c:forEach items="${book.authors}" var="author" varStatus="loop"><c:out value="${author.authorName}"/><c:if test="${!loop.last}">, </c:if></c:forEach></p>
                <p class="book-meta"><c:out value="${book.publisher}"/> · <c:out value="${book.publishDate}"/></p>
                <p class="book-price"><fmt:formatNumber value="${book.price}" type="number"/> đ</p>
                <span class="book-stock">Còn <c:out value="${book.quantity}"/> cuốn · <c:out value="${book.reviewCount}"/> đánh giá</span>
                <div class="product-actions">
                    <c:choose>
                        <c:when test="${not empty sessionScope.currentUser and not sessionScope.currentUser.admin and book.quantity gt 0}">
                            <form action="${pageContext.request.contextPath}/cart/add" method="post">
                                <input type="hidden" name="bookId" value="${book.bookId}"/>
                                <input type="hidden" name="quantity" value="1"/>
                                <button class="button primary" type="submit">Thêm vào giỏ</button>
                            </form>
                        </c:when>
                        <c:when test="${empty sessionScope.currentUser}">
                            <a class="button secondary" href="${pageContext.request.contextPath}/login">Đăng nhập để mua</a>
                        </c:when>
                        <c:when test="${book.quantity le 0}"><span class="sold-out">Hết hàng</span></c:when>
                    </c:choose>
                </div>
            </div></article></c:forEach></div>
        <c:if test="${totalPages > 1}"><nav class="pagination"><c:forEach begin="1" end="${totalPages}" var="number"><a class="${number == page ? 'active' : ''}" href="${pageContext.request.contextPath}/home?authorId=${selectedAuthorId}&page=${number}">${number}</a></c:forEach></nav></c:if>
    </c:when><c:otherwise><div class="empty-state"><strong>Chưa có sách để hiển thị</strong><span>Hãy thêm dữ liệu vào cơ sở dữ liệu hoặc chọn một tác giả khác.</span></div></c:otherwise></c:choose>
</div></section>
</body></html>
