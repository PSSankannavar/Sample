package com.spring.junit;
/*package com.spring.hotelmanagementapplication.junit;

import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.constraints.AssertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.gemstone.gemfire.internal.Assert;
import com.spring.hotelmanagementapplication.exception.ServiceException;
import com.spring.hotelmanagementapplication.model.Cityinfo;
import com.spring.hotelmanagementapplication.model.Customerinfo;
import com.spring.hotelmanagementapplication.model.Hotelinfo;
import com.spring.hotelmanagementapplication.service.impl.BookARoomServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true, transactionManager = "transactionManager")
@Transactional
public class BookARoomTest {
	
	@Autowired
	public BookARoomServiceImpl bookARoomService;

	@Test
	public void testBookARoom(){
		Customerinfo bookARoom = new Customerinfo();
		Cityinfo cityinfo = new Cityinfo();
		cityinfo.setCityId(1);
		Hotelinfo hotelinfo = new Hotelinfo();
		hotelinfo.setHotelId(1);
		bookARoom.setCityinfo(cityinfo);
		bookARoom.setHotelinfo(hotelinfo);
		bookARoom.setCustomerName("User1");
		String date1 ="1-12-2016";
		String date2 ="12-12-2016";
		bookARoom.setCheckInDate(new Date (date1));
		bookARoom.setCheckOutDate(new Date(date2));
		bookARoom.setNumberOfRooms(5);
		try {
			bookARoomService.saveBookedRoom(bookARoom);
			Assert.assertTrue(true); 
		} catch (ServiceException e) {
			Assert.assertTrue(false); 
		}
	}
}
*/