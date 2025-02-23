<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.entity.cart.Cart" scope="session"/>
<a href="${pageContext.servletContext.contextPath}/cart">
    Cart(${cart.totalQuantity}) <fmt:formatNumber value="${cart.totalPrice}" type="currency"
                                                  currencySymbol="${cart.currency.symbol}"/>
</a>