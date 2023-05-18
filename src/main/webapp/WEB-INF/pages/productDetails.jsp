<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<style>
    <%@ include file="/styles/main.css" %>
</style>

<jsp:useBean id="product" type="com.es.phoneshop.model.entity.Product" scope="request"/>
<tags:master pageTitle="Product Details">
    <p>
        Cart: ${cart}
    </p>
    <p>
        <a href="${pageContext.servletContext.contextPath}/products">Go back</a>
    </p>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="error">
            An error occurred when adding a product to the cart
        </div>
    </c:if>
    <p>${product.description}</p>
    <form method="post">
        <table>
            <tr>
                <td>Image</td>
                <td>
                    <img src="${product.imageUrl}">
                </td>
            </tr>
            <tr>
                <td>Code</td>
                <td>${product.code}</td>
            </tr>
            <tr>
                <td>Stock</td>
                <td>${product.stock}</td>
            </tr>
            <tr>
                <td>Price</td>
                <td class="price">
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
            <tr>
                <td>Quantity</td>
                <td>
                    <input type="number" class="quantity" name="quantity" value="${not empty param.error ? param.errorQuantity : 1}">
                    <c:if test="${not empty param.error}">
                        <div class="error">
                                ${param.error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <button>Add to cart</button>
        <tags:viewedProducts/>
    </form>
</tags:master>