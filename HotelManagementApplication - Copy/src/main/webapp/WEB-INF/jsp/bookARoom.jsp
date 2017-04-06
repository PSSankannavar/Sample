<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bookaroom.css"/>

<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<!-- <link rel="stylesheet" href="/resources/demos/style.css"> -->
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
$(document).ready(function() {
    $('#dropdown1').change(function() {
        var selectedValue = $(this).val();
        var servletUrl = 'dropdown2options?value=' + selectedValue;

        $.getJSON(servletUrl, function(options) {
            var dropdown2 = $('#dropdown2');
            $('>option', dropdown2).remove(); // Clean old options first.
            if (options) {
                $.each(options, function(key, value) {
                    dropdown2.append($('<option/>').val(key).text(value));
                });
            } else {
                dropdown2.append($('<option/>').text("Please select dropdown1"));
            }
        });
    
        
    });
});




function datepicker1function(){
	$( "#datepicker-1" ).datepicker({ dateFormat: 'dd-mm-yy' });
    $( "#datepicker-1" ).datepicker("show");
	};

	function datepicker2function(){
		$( "#datepicker-2" ).datepicker({ dateFormat: 'dd-mm-yy' });
	    $( "#datepicker-2" ).datepicker("show");
		};

	function checkDropdown1(){
		var card = document.getElementById("dropdown1");
		if(card.value != -1 ) {
			 $('#dropdown1').trigger("change");
			
		}
	}	
	
 function checkNumeric(){
	var value = document.getElementById("numberOfRooms").value;
	var dropdown1 = document.getElementById("dropdown1").value;
	var dropdown2 = document.getElementById("dropdown2").value;
	var name =  document.getElementById("name").value;
	var datepicker1 =  document.getElementById("datepicker-1").value;
	var datepicker2 =  document.getElementById("datepicker-2").value;
		
	if(dropdown1 == -1 && dropdown2 == -1 && name == "" && datepicker1 == "" && datepicker2 == "" && value == 0  ){
		$("#error").text("Please enter the valid ");
		return false;
	}
	
	else if (dropdown1 == -1 ) {
			$("#error").text("The value provided for the field city is incorrect or incomplete");
			return false;

		}
	else if(dropdown2 == -1){
		$("#error").text("The value provided for the field hotel is incorrect or incomplete");
		return false;
	}

	else if( name == ""){
		$("#error").text("The value provided for the field name is incorrect or incomplete");
		return false;
	}
	else if(datepicker1 == ""){
		$("#error").text("The value provided for the field date is incorrect or incomplete ");
		return false;
	}
	else if(datepicker2 == ""){
		$("#error").text("The value provided for the field check out date is incorrect or incomplete");
		return false;
	}
	else if(value == null){
		$("#error").text(" The value provided for the field number of rooms is incorrect or incomplete");
		return false;
	}
		else {
			$("#error").text("");
			if (isNaN(value) || value == null) {
				$("#numberOfRoomsError").text(
						"Please enter a valid number of rooms");
				return false;
			} else {
				$("#numberOfRoomsError").text("");
				return true;
				}
		}
	
		
	}
 
</script>
</head>
<body >

<h1><b>Book A Room</b></h1>
<b><div id="error"></div></b>

<form:form commandName="room" action="bookARoom.view" method="POST" onsubmit="return checkNumeric()" >
		<table id = 'customers'>
			<tr>
				<td><font color="red">*</font> <b>Select a city :</b></td>
			
				<td><form:select path="cityinfo.cityId" id ="dropdown1" class='my_dropdown' >
						<form:option value="-1">Select city: </form:option>
						<c:forEach items="${cityinfo}" var="city">
							<form:option value="${city.cityId}">${city.cityName}</form:option>
						</c:forEach>

					</form:select>
					</td>
					
					
				<td><form:errors path="cityinfo.cityId" cssClass="errorClass" /></td>
			</tr>

			<tr>
				<td><font color="red">*</font> <b>Select a Hotel :</b></td>
				<td><form:select path="hotelinfo.hotelId" id="dropdown2" >

						
						<c:if test="${not empty hotelinfoDropDown }">
								<form:option value="${hotelinfoDropDown.hotelId}">${hotelinfoDropDown.hotelName}</form:option>
						</c:if>
						<c:if
							test="${empty hotelinfoDropDown}">
							<form:option value="-1">Select Hotel Name:</form:option>
						</c:if>

						
						

					</form:select></td>
				<td>
				 <form:errors path="hotelinfo.hotelId" cssClass="errorClass"
						onclick="checkDropdown1()" />
						
				 <c:if
						test="${not empty hotelinfoDropDown}">
				Please click <a onclick="checkDropdown1()"><font size="3"
							color="blue">here</font></a> for list of hotels
				</c:if>
				
				</td>
			</tr>

			<tr>
				<td><font color="red">*</font><b> Name :</b></td>
				<td><form:input path="customerName" id = "name"/></td>
				<td><form:errors path="customerName" cssClass="errorClass" /> </td>
			</tr>

			<tr>
				<td><font color="red">*</font> <b>From (Check-In-date) : </b></td>
				<td><form:input path="checkInDate" id ="datepicker-1" onclick="datepicker1function()" /></td>
				<td><form:errors path="checkInDate" cssClass="errorClass" /></td>
			</tr>
			
			
			<tr>
				<td><font color="red">*</font><b> Until (Check-Out-date) : </b></td>
				<td><form:input path="checkOutDate" id ="datepicker-2" onclick="datepicker2function()" /></td>
				<td><form:errors path="checkOutDate" cssClass="errorClass" /></td>
			</tr>
			
			<tr>
				<td><font color="red">*</font><b> Rooms : </b></td>
				<td><form:input path="numberOfRooms" id="numberOfRooms"/></td>
				<td><form:errors path="numberOfRooms" cssClass="errorClass" /></td>
			</tr>
			
			
			<tr>
				<td><input type="submit" value='Submit' class = "submit1"></td>
				<td><input type="button" id="reset" class = "button1" onclick="document.location.href='home.view'"
					value='Cancel' /></td>
					<td></td>
				
			</tr>
		</table>
	</form:form>



</body>
</html>