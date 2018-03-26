<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
  <head>
    <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
    <!--Load the AJAX API-->
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">

      // Load the Visualization API and the corechart package.
      google.charts.load('current', {'packages':['corechart']});

      // Set a callback to run when the Google Visualization API is loaded.
      google.charts.setOnLoadCallback(drawStatusStatistic);

      // Callback that creates and populates a data table,
      // instantiates the pie chart, passes in the data and
      // draws it.
      function drawStatusStatistic() {

        // Create the data table.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'status');
        data.addColumn('number', 'count');
        
        <c:forEach items="${statusStatistic}" var="statusStatistic">
        	data.addRow(['${statusStatistic.key}', ${statusStatistic.value}]);
  		</c:forEach>
        
        //This doesn't work, at the server side the javascript variable 'statusStatistic' has not been defined, it is void.
        /*
        var statusStatistic = new Object();
        <c:forEach items="${customers}" var="customers">
          if('${customers.city}' in statusStatistic){
        	  statusStatistic['${customers.city}']=1;
          }else{
        	  statusStatistic['${customers.city}']=statusStatistic['${customers.city}']+1;
          }
    	</c:forEach>
    	
        <c:forEach items="${statusStatistic}" var="statusStatistic">
          data.addRow(['${statusStatistic.key}', ${statusStatistic.value}]);
    	</c:forEach>
    	*/

    	//This arrayToDataTable can also set data to table.
    	/*
        var data = google.visualization.arrayToDataTable([
          [{label: 'City', id: 'City', type: 'string'}, {label: 'count', id: 'count', type: 'number'}],
           <c:forEach var="customers" items="${customers}">
                ['${customers.city}', 1], 
           </c:forEach>
        ]);*/
        
        //DataTable can also insert data by method addRows().
        /*
        data.addRows([
          ['Mushrooms', 3],
          ['Onions', 1],
          ['Olives', 1],
          ['Zucchini', 1],
          ['Pepperoni', 2]
        ]);
        */

        // Set chart options
        var options = {'title':'Status statistic',
                       'width':500,
                       'height':300};

        // Instantiate and draw our chart, passing in some options.
        var chart = new google.visualization.PieChart(document.getElementById('chart_city'));
        chart.draw(data, options);
      }
    </script>
  </head>

  <body>
    <!--Div that will hold the pie chart-->
    <div id="chart_city"></div>
    <div id="chart_gender"></div>
    <c:if test="${not empty elements}">
    	<table border="1" id="customers">
    		<tr><th>ID</th><th>Type</th><th>Description</th></tr>
			<c:forEach items="${elements}" var="element">
    			<tr>
			    	<td><c:out value="${element.id}"></c:out></td>
			    	<td><c:out value="${element.type}"></c:out></td>
			    	<td><c:out value="${element.description}"></c:out></td>
    			</tr>
			</c:forEach>
    	</table>
    </c:if>
    
  </body>
</html>