<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.entity.Order" scope="request"/>
<c:set var="servletContextPath" value="${pageContext.servletContext.contextPath}"/>
<tags:master pageTitle="Order overview">
    <a href="${pageContext.servletContext.contextPath}/cart">
        Back
    </a>
    <h1>Order overview</h1>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>Description</td>
            <td class="quantity">Quantity</td>
            <td class="price">Price</td>
        </tr>
        </thead>
        <c:forEach var="item" items="${order.items}" varStatus="loop">
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
                        ${item.quantity}
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
            </tr>
        </c:forEach>
        <tr>
            <td>Subtotal</td>
            <td></td>
            <td class="quantity">
                    ${order.totalQuantity}
            </td>
            <td class="price">
                <fmt:formatNumber value="${order.subtotal}" type="currency"
                                  currencySymbol="${order.currency.symbol}"/>
            </td>
        </tr>
        <tr>
            <td>Delivery</td>
            <td></td>
            <td></td>
            <td class="price">
                <fmt:formatNumber value="${order.deliveryCost}" type="currency"
                                  currencySymbol="${order.currency.symbol}"/>
            </td>
        </tr>
        <tr>
            <td>Total</td>
            <td></td>
            <td></td>
            <td class="price">
                <fmt:formatNumber value="${order.totalPrice}" type="currency"
                                  currencySymbol="${order.currency.symbol}"/>
            </td>
        </tr>
    </table>
    <h2>Your details</h2>
    <table>
    <table>
        <tags:orderOverviewRow name="firstName" label="First name" order="${order}"/>
        <tags:orderOverviewRow name="lastName" label="Last name" order="${order}"/>
        <tags:orderOverviewRow name="phone" label="Phone" order="${order}"/>
        <tags:orderOverviewRow name="deliveryDate" label="Delivery date" order="${order}"/>
        <tags:orderOverviewRow name="deliveryAddress" label="Delivery address" order="${order}"/>
        <tr>
            <td>Payment method</td>
            <td>${order.paymentMethod}</td>
        </tr>
    </table>
</tags:master>
