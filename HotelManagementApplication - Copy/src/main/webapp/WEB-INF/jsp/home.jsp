<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/home.css"/>

</head>
<body>

	<div><b>${message}</b></div>
	<br>
	<br>
	<div class="center">
	<div class = "padding1"><a href="book.view"><b>Book a Room</b></a></div>
	<br />
	<br>
	<div class = "padding2"><a href="viewLowestPricedRooms.view"><b>View Lowest Priced Hotels</b></a></div>
	<br />
	</div>
	
</body>
</html>