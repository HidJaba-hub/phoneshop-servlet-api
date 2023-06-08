<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Advanced search">
    <a href="${pageContext.servletContext.contextPath}">
        Back
    </a>
    <c:if test="${not empty requestScope.message && empty requestScope.errors}">
        <div class="success">
                ${requestScope.message}
        </div>
    </c:if>
    <c:if test="${not empty requestScope.errors}">
        <div class="error">
            An error occurred while searching
        </div>
    </c:if>
    <h2>Advanced search</h2>
    <form action="${pageContext.servletContext.contextPath}/advanced/search">
        <div>
            Description:
            <input name="query" value="${param.query}">
            <select name="searchCriteria">
                <c:forEach var="searchCriteria" items="${searchCriteria}">
                    <option name="${searchCriteria}" ${param.searchCriteria eq searchCriteria ? 'selected' : '' }> ${searchCriteria}</option>
                </c:forEach>
            </select>
        </div>
        <br>
        <div>
            <c:set var="error" value="${errors['minPrice']}"/>
            Min Price:
            <input name="minPrice" type="number" value="${param['minPrice']}">
            <c:if test="${not empty error}">
                <div class="error">
                        ${error}
                </div>
            </c:if>
        </div>
        <br>
        <div>
            <c:set var="error" value="${errors['maxPrice']}"/>
            Max Price:
            <input name="maxPrice" type="number" value="${param['maxPrice']}">
            <c:if test="${not empty error}">
                <div class="error">
                        ${error}
                </div>
            </c:if>
        </div>
        <br>
        <button>Search</button>
    </form>

    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
            </td>
            <td class="price">
                Price
            </td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}" varStatus="loop">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                        ${product.description}
                </td>
                <td class="price">
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
        </c:forEach>
    </table>
</tags:master>
