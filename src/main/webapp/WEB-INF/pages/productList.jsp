<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<%
    String json = new ObjectMapper().writeValueAsString(products);
    request.setAttribute("json", json);
%>
<tags:master pageTitle="Product List">
    <input type="hidden" id="products" value='${json}'>
    <p>
        Welcome to Expert-Soft training!
    </p>
    <form>
        <input name="query" value="${param.query}">
        <button>Search</button>
    </form>
    <table id="productsTable">
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
                <tags:sort sort="description" order="asc"></tags:sort>
                <tags:sort sort="description" order="desc"></tags:sort>
            </td>
            <td class="price">
                Price
                    <tags:sort sort="price" order="asc"></tags:sort>
                    <tags:sort sort="price" order="desc"></tags:sort>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}" varStatus="loop">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">${product.description}</a>
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
    </table>
</tags:master>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/scripts/sortProducts.js"></script>
<script src="${pageContext.servletContext.contextPath}/scripts/showHid.js"></script>

