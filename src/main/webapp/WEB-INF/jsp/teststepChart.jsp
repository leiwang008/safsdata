<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
  <head>
   <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
  </head>

  <body>
    <c:if test="${not empty elements}">
    	<table border="1" id="customers">
    		<tr><th>ID</th><th>Testcase_ID</th><th>Status_ID</th><th>LogMessage</th></tr>
			<c:forEach items="${elements}" var="element">
    			<tr>
			    	<td><c:out value="${element.id}"></c:out></td>
			    	<td><c:out value="${element.testcaseId}"></c:out></td>
			    	<td><c:out value="${element.statusId}"></c:out></td>
			    	<td><c:out value="${element.logMessage}"></c:out></td>
    			</tr>
			</c:forEach>
    	</table>
    </c:if>
    
  </body>
</html>