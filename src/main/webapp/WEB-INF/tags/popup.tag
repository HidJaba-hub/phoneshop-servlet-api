<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="product" required="true" type="com.es.phoneshop.model.entity.Product" %>

<div>
    <h2 style="text-align: left">Price history</h2>
    <h3 style="text-align: left">${product.description}</h3>
    <table>
        <thead>
        <tr>
            <th>Start Date</th>
            <th>Price</th>
        </tr>
        </thead>
        <c:forEach var="priceChange" items="${product.priceHistory}">
            <tr>
                <td>
                    <fmt:setLocale value="en_US"/>
                    <fmt:formatDate value="${priceChange.date}" type="date" pattern="dd MMM yyyy"/>
                </td>
                <td>
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>