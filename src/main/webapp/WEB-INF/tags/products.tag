<%@ tag import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="products" required="true" type="java.util.ArrayList" %>
<%@ attribute name="redirectPath" required="true" type="java.lang.String" %>

<style>
    <%@ include file="/styles/main.css" %>
</style>
<%
    if (products.size() > 0) {
        String json = new ObjectMapper().writeValueAsString(products);
        request.setAttribute("json", json);
    }
%>
<input type="hidden" id="products" value='${json}'>
<c:set var="servletContextPath" value="${pageContext.servletContext.contextPath}"/>
<c:if test="${not empty products}">
    <form method="post" >
    <table id="productsTable">
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
                <tags:sort sort="description" order="asc"/>
                <tags:sort sort="description" order="desc"/>
            </td>
            <td>
                Quantity
            </td>
            <td class="price">
                Price
                <tags:sort sort="price" order="asc"/>
                <tags:sort sort="price" order="desc"/>
            </td>
        </tr>
        </thead>

        <c:forEach var="product" items="${products}" varStatus="loop">
        <c:set var="productId" value="${product.id}"/>
        <tr>
            <input type="hidden" name="path"
                   value="${servletContextPath}${redirectPath}">
            <td>
                <img class="product-tile" src="${product.imageUrl}">
            </td>
            <td>
                <a href="${servletContextPath}/products/${productId}">${product.description}</a>
            </td>
            <td>
                <c:set var="error" value="${errors[productId]}"/>
                <input type="number" class="quantity" name="quantity"
                       value="${productId eq param.id ? param.quantity : 1}">
                <input type="hidden" name="productId" value="${productId}"/>
                <c:if test="${not empty param.errors && param.id eq productId}">
                    <div class="error">
                            ${param.errors}
                    </div>
                </c:if>
            </td>
            <td class="price">
                <a href="#" onmouseover="showHid(${loop.index})" onmouseleave="showHid(${loop.index})">
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </a>
                <div id="${loop.index}" class="popup" style="display: none">
                    <tags:popup product="${product}"/>
                </div>
            </td>
            <td>
                <button formaction="${servletContextPath}/cart/addToCart/${productId}">
                    Add to cart
                </button>
            </td>
        </tr>
        <form>
            </c:forEach>
    </table>
</c:if>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${servletContextPath}/scripts/sortProducts.js"></script>
<script src="${servletContextPath}/scripts/showHid.js"></script>