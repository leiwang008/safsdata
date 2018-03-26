<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
  <head>
   <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
  </head>

  <body>
    <c:if test="${not empty elements}">
    	<table border="1" id="customers">
    		<tr><th>ID</th><th>Testsuite_ID</th><th>name</th><th>time</th></tr>
			<c:forEach items="${elements}" var="element">
    			<tr>
			    	<td><c:out value="${element.id}"></c:out></td>
			    	<td><c:out value="${element.testsuiteId}"></c:out></td>
			    	<td><c:out value="${element.name}"></c:out></td>
			    	<td><c:out value="${element.time}"></c:out></td>
    			</tr>
			</c:forEach>
    	</table>
    </c:if>
    
  </body>
</html>