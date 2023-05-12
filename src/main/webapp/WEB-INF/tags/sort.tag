<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="order" required="true" %>


<a href="${pageContext.servletContext.contextPath}/products/sort?sort=${sort}&order=${order}&query=${param.query}"
   style="${sort eq param.sort and order eq param.order ? 'font-weight: bold;' : ''}; text-decoration: none">
    ${order eq 'asc' ? (sort eq param.sort and order eq param.order  ? '&#11014;' : '&#8679;')
            : (sort eq param.sort and order eq param.order ? '&#11015;' : '&#8681;')}
</a>