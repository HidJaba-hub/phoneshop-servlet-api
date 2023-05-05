<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="order" required="true" %>


<a href="?sort=${sort}&order=${order}&query=${param.query}"
   style="${sort eq param.field or order eq param.order ? 'font-weight: bold;' : ''}; text-decoration: none">
    <c:choose>
        <c:when test="${order eq 'asc' and order eq param.order}">&#11014;</c:when>
        <c:when test="${order eq 'asc' }">&#8679;</c:when>
    </c:choose>
    <c:choose>
        <c:when test="${order eq 'desc' and order eq param.order}">&#11015;</c:when>
        <c:when test="${order eq 'desc'}">&#8681;</c:when>
    </c:choose>
</a>