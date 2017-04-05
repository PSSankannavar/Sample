<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
   <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Lowest Priced Hotel</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/viewlowestprice.css"/>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
var tableContent ="";
$(document).ready(function() {
    $('#dropdown1').change(function() {
        var selectedValue = $(this).val();
        var servletUrl = 'viewLowestPrice?value=' + selectedValue;

        
        $.getJSON(servletUrl, function(options) {
          
        		generateHeader() ;       	
				display( options);
				generateFooter();
        	
        	
        }); 
        
    });
});

function generateFooter() {
	tableContent += "</table>";
	$("#hotelTable").html(tableContent);
}

function display(options) {
	
	options[0].forEach(function(item){
		tableContent += "<tr><td>"
			+ item.hotelName + "</td><td> Rs." + item.tariff
			+ " per day</td></tr>";
	});
}

function generateHeader() {
	tableContent = "<table border='1' id = 'customers'><tr class='thead'><th>Hotel Name</th><th>Tariff</th></tr>";
}
</script>

</head>
<body>
<h1><b>View Lowest Priced Hotels</b></h1>

<form:form commandName="hotel" action="bookARoom.view" method="POST">
		<table id="customers">
			<tr>
				<td><b>Select a city :</b> </td>
				<td><form:select path="cityinfo.cityId" id ="dropdown1" class='my_dropdown' >
						<div id = "options">
						<form:option value="-1"><b>Select city:</b> </form:option>
						<c:forEach items="${cityinfo}" var="city">
						<form:option value="${city.cityId}"><b>${city.cityName}</b></form:option>
						</c:forEach>
						</div>
					</form:select></td>
				
				<td><input type="button" id="reset" onclick="document.location.href='home.view'"
					value='Cancel' class="button"/></td>
			</tr>
			
				
			
		</table>
	
</form:form>

<div id="hotelTable"></div>
</body>
</html>