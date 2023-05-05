<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <form>
    <input name="query" value="${param.query}">
    <button>Search</button>
  </form>
  <table>
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
          <script src="${pageContext.servletContext.contextPath}/scripts/showHide.js"></script>
          <a href="#" onmouseover="showHide(${loop.index})" onmouseleave="showHide(${loop.index})">
            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
          </a>
          <div id="popup${loop.index}" class="popup" style="display: none">
            <tags:popup product="${product}"/>
          </div>
        </td>
      </tr>
    </c:forEach>
  </table>
</tags:master>

