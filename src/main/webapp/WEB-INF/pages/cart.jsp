<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.entity.cart.Cart" scope="session"/>
<c:set var="servletContextPath" value="${pageContext.servletContext.contextPath}"/>
<tags:master pageTitle="Cart">
    <p>
        Cart: ${cart}
    </p>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty errors}">
        <div class="error">
            An error occurred when adding a product to the cart
        </div>
    </c:if>
    <form method="post" action="${servletContextPath}/cart">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>Description</td>
                <td class="quantity">Quantity</td>
                <td class="price">Price</td>
            </tr>
            </thead>
            <c:forEach var="item" items="${cart.items}" varStatus="loop">
                <c:set var="product" value="${item.product}"/>
                <tr>
                    <td>
                        <img class="product-tile" src="${product.imageUrl}">
                    </td>
                    <td>
                        <a href="${servletContextPath}/products/${product.id}">${product.description}</a>
                    </td>
                    <td class="quantity">
                        <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                        <c:set var="error" value="${errors[product.id]}"/>
                        <input  class="quantity" name="quantity"
                               value="${not empty error? paramValues['quantity'][loop.index]: quantity}">
                        <input type="hidden" name="productId" value="${product.id}">
                        <c:if test="${not empty error}">
                            <div class="error">
                                    ${errors[product.id]}
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
                        <button form="deleteCartItem"
                                formaction="${servletContextPath}/cart/deleteCartItem/${product.id}">
                            Delete
                        </button>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td>
                    Total
                </td>
                <td>

                </td>
                <td>
                    Total quantity = ${cart.totalQuantity}
                </td>
                <td>
                    Total price = <fmt:formatNumber value="${cart.totalPrice}" type="currency"
                                                    currencySymbol="${cart.currency.symbol}"/>
                </td>
            </tr>
        </table>
        <p>
            <c:if test="${not empty cart.items}">
                <button>
                    Update
                </button>
            </c:if>
        </p>
    </form>
    <form id="deleteCartItem" method="post">
    </form>
</tags:master>
<script src="${servletContextPath}/scripts/showHid.js"></script>
