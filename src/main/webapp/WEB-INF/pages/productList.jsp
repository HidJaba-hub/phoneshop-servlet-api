<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="cart" type="com.es.phoneshop.model.entity.cart.Cart" scope="session"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <c:if test="${not empty param.message && empty param.errors}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty param.errors}">
        <div class="error">
            An error occurred when adding a product to the cart
        </div>
    </c:if>
    <form>
        <input name="query" value="${param.query}">
        <button>Search</button>
    </form>
    <form action="${pageContext.servletContext.contextPath}/advanced/search">
        <button>
            Advanced search
        </button>
    </form>
    <tags:products products="${products}" redirectPath="/products?query=${param.query}"/>
</tags:master>