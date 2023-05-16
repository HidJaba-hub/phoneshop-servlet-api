<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style><%@ include file="/styles/main.css"%></style>

<c:if test="${viewedProducts != null}">
<jsp:useBean id="viewedProducts" type="com.es.phoneshop.model.entity.ProductHistory" scope="session"/>
<h2 style="text-align: center">Recently viewed</h2>
<div class="recently-viewed">
    <c:if test="${not empty viewedProducts}">
        <c:forEach var="product" items="${viewedProducts.recentlyViewedProducts}">
            <div class="recent-product">
                <img class="recent-product-img" src="${product.imageUrl}" alt="${product.description}">
                <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                            ${product.description}</a>
                <p class="price">
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </p>
            </div>
        </c:forEach>
    </c:if>
</div>
</c:if>
