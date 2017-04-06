package com.spring.validators;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.spring.model.Customerinfo;
import com.spring.service.impl.BookARoomServiceImpl;


public class BookARoomValidator implements Validator {

	@Autowired
	public BookARoomServiceImpl bookARoomService;
	
	@Override
	public boolean supports(Class<?> arg0) {
		return this.getClass().isAssignableFrom(BookARoomValidator.class);
	}

	@Override
	public void validate(Object object, Errors errors) {
		
		Customerinfo customerinfo = (Customerinfo)object;
		if(customerinfo.getCheckInDate() == null)
			errors.rejectValue("checkInDate","", "Please enter a Check In Date");
		if(customerinfo.getCheckOutDate() == null)
			errors.rejectValue("checkOutDate", "","Please enter a Check Out Date");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "customerName",
				"error.checkInDate.required", "Please enter a Customer Name");
		if(customerinfo.getNumberOfRooms() == 0)
			errors.rejectValue("numberOfRooms", "","Please enter a Number Of Rooms");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cityinfo.cityId",
				"error.checkInDate.required", "Please select a City");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "hotelinfo.hotelId",
				"error.checkInDate.required", "Please select a Hotel Name");
		
		if(customerinfo.getCityinfo().getCityId() == -1)
			errors.rejectValue("cityinfo.cityId","", "Please select a City");
		
		if(customerinfo.getHotelinfo().getHotelId() == -1)
			errors.rejectValue("hotelinfo.hotelId","", "Please select a Hotel Name");
		
		if(customerinfo.getCheckInDate() != null && customerinfo.getCheckOutDate() != null ){
			if(customerinfo.getCheckInDate().compareTo(new Date()) < 0)
				errors.rejectValue("checkInDate", "","Please enter a check-in-date greater than current date");
			if(customerinfo.getCheckInDate().compareTo(customerinfo.getCheckOutDate()) > 0){
				errors.rejectValue("checkOutDate", "","Please enter a check-out-date greater than check-in-date");
			}
		}
		
		/*int available = bookARoomService.getNumberofHotelRoomsAvailable(customerinfo.getHotelinfo().getHotelId()).get(0).getNumberOfRooms();
		int booked = 0;
		List<Customerinfo> customerinfos = bookARoomService.getNumberofHotelRoomsAvailableBooked(customerinfo.getHotelinfo().getHotelId());
		for (Customerinfo info : customerinfos) {
			booked = booked + info.getNumberOfRooms();
		}
		if(available < booked+customerinfo.getNumberOfRooms()){
			errors.rejectValue("numberOfRooms", "","Rooms are not available in");
		}*/
	}

}
