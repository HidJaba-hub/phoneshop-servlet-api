<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.entity.Order" %>
<%@ attribute name="errors" required="true" type="java.util.Map" %>
<%@ attribute name="type" required="true" %>
<%@ attribute name="defaultValue" required="true" %>


<tr>
    <td>${label}<span style="color:red"/>*</td>
    <td>
        <c:set var="error" value="${errors[name]}"/>
        <input name="${name}" type="${type}" value="${not empty error ? param[name] :
        not empty order[name]? order[name] : defaultValue}"/>
        <c:if test="${not empty error}">
            <div class="error">
                    ${error}
            </div>
        </c:if>
    </td>
</tr>