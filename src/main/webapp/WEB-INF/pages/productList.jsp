<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="cart" type="com.es.phoneshop.model.entity.cart.Cart" scope="session"/>
<%
    String json = new ObjectMapper().writeValueAsString(products);
    request.setAttribute("json", json);
%>
<tags:master pageTitle="Product List">
    <c:set var="servletContextPath" value="${pageContext.servletContext.contextPath}"/>
    <input type="hidden" id="products" value='${json}'>
    <p>
        Welcome to Expert-Soft training!
    </p>
    <c:if test="${not empty param.message && empty param.errors}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty param.errors}">
        <div class="error">
            An error occurred when adding a product to the cart
        </div>
    </c:if>
    <form>
        <input name="query" value="${param.query}">
        <button>Search</button>
    </form>
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
            <td>
                Add product to cart
            </td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}" varStatus="loop">
        <c:set var="productId" value="${product.id}"/>
        <tr>
            <input type="hidden" name="path"
                   value="${servletContextPath}/products?query=${param.query}">
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
</tags:master>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${servletContextPath}/scripts/sortProducts.js"></script>
<script src="${servletContextPath}/scripts/showHid.js"></script>