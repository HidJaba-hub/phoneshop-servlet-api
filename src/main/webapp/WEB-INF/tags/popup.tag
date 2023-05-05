<%@ tag trimDirectiveWhitespaces="true" %>
<style><%@ include file="/styles/main.css"%></style>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="product" required="true" type="com.es.phoneshop.model.entity.Product" %>

<div>
    <h2 style="text-align: left">Price history</h2>
    <h3  style="text-align: left">${product.description}</h3>
        <table>
            <thead>
            <tr>
                <th>Start Date</th>
                <th>Price</th>
            </tr>
            </thead>
            <c:forEach var="priceChange" items="${product.priceHistoryList}">
                <tr>
                    <td>
                        <fmt:setLocale value="en_US" />
                        <fmt:formatDate value="${priceChange.date}" type="date" pattern="dd MMM yyyy"/>
                    </td>
                    <td>
                            ${product.currency.symbol}
                        <fmt:formatNumber value="${priceChange.price}"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
</div>