<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.entity.Order" scope="request"/>
<c:set var="servletContextPath" value="${pageContext.servletContext.contextPath}"/>
<tags:master pageTitle="CheckOut">
    <a href="${pageContext.servletContext.contextPath}/cart">
        Back
    </a>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty errors}">
        <div class="error">
            An error occurred when the form was filling
        </div>
    </c:if>
    <form method="post" action="${servletContextPath}/checkout">
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
        <tags:orderFormRow name="firstName" label="First name" order="${order}" errors="${errors}" type="text" defaultValue="Ваше имя"/>
        <tags:orderFormRow name="lastName" label="Last name" order="${order}" errors="${errors}" type="text" defaultValue="Ваша фамилия"/>
        <tags:orderFormRow name="phone" label="Phone" order="${order}" errors="${errors}" type="text" defaultValue="+375(xx)xxxxxxx"/>
        <tags:orderFormRow name="deliveryDate" label="Delivery date" order="${order}" errors="${errors}" type="date" defaultValue=""/>
        <tags:orderFormRow name="deliveryAddress" label="Delivery address" order="${order}" errors="${errors}"
                           type="text" defaultValue="г. Город ул.Ваша улица, дом"/>
        <tr>
            <td>Payment method <span style="color: red">*</span></td>
            <td>
                <select name="paymentMethod">
                    <option></option>
                    <c:forEach var="paymentMethod" items="${paymentMethods}">
                        <option value="${paymentMethod}" ${param.paymentMethod eq paymentMethod ? 'selected' : '' }>
                                ${paymentMethod}</option>
                    </c:forEach>
                </select>
                <c:set var="error" value="${errors['paymentMethod']}"/>
                <c:if test="${not empty error}">
                    <div class="error">
                            ${error}
                    </div>
                </c:if>
            </td>
        </tr>
        <p>
            <button>
                Place Order
            </button>
        </p>
    </table>
</tags:master>
