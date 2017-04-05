package com.spring.controller;



import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.exception.ServiceException;
import com.spring.model.Cityinfo;
import com.spring.model.Customerinfo;
import com.spring.model.Hotelinfo;
import com.spring.service.impl.BookARoomServiceImpl;
import com.spring.validators.BookARoomValidator;

@Controller
public class BookARoomController {

	@Autowired
	public BookARoomServiceImpl bookARoomService;
	

	private static final Logger LOGGER = Logger.getLogger(BookARoomController.class);
	
	@ModelAttribute("cityinfo")
	public List<Cityinfo> getAllCityInfo(){
		
		List<Cityinfo> cityinfos = null;
		try {
			cityinfos = bookARoomService.getAllCityInfo();
		} catch (ServiceException exception) {
			LOGGER.debug(exception.getMessage());
		}
		
		return cityinfos;
	}
	
	@RequestMapping("book.view")
	public String getBookARoomPage(Model model)
	{
		try{
		model.addAttribute("room" , new Customerinfo());
		}catch(Exception exception){
			
		}
		return "bookARoom";
	}
	
	
	@RequestMapping(value = "bookARoom.view" ,method = RequestMethod.POST)
	public String saveBookedRoom(@ModelAttribute("room") Customerinfo bookARoom,BindingResult errors,Model model) throws ParseException{
		BookARoomValidator roomValidator = new BookARoomValidator();
		if (roomValidator.supports(Customerinfo.class)) {
			roomValidator.validate(bookARoom, errors);
			if (errors.hasErrors()) {		
				if(bookARoom.getCityinfo().getCityId() != -1 && bookARoom.getHotelinfo().getHotelId() != -1)
				{
					Hotelinfo hotelinfo = new Hotelinfo();
					hotelinfo.setHotelId(bookARoom.getHotelinfo().getHotelId());
					try {
						hotelinfo.setHotelName(bookARoomService.getHotelInfo(bookARoom.getHotelinfo().getHotelId()).get(0).getHotelName());
					} catch (ServiceException exception) {
						LOGGER.debug(exception.getMessage());
						model.addAttribute("message", "Something went wrong Please try later");
						return "bookARoom";
					}
					model.addAttribute("hotelinfoDropDown" , hotelinfo);
				}
				return "bookARoom";
			}
		 else {

			try {

				//check if room is available ??
				if(!checknumberOfRooms(bookARoom))
				{
					errors.rejectValue("numberOfRooms", "","Rooms are not available for selected check-in-date and check-out-date/hotel ");
					if (errors.hasErrors()) {		
						if(bookARoom.getCityinfo().getCityId() != -1 && bookARoom.getHotelinfo().getHotelId() != -1)
						{
							Hotelinfo hotelinfo = new Hotelinfo();
							hotelinfo.setHotelId(bookARoom.getHotelinfo().getHotelId());
							hotelinfo.setHotelName(bookARoomService.getHotelInfo(bookARoom.getHotelinfo().getHotelId()).get(0).getHotelName());
							model.addAttribute("hotelinfoDropDown" , hotelinfo);
						}
						return "bookARoom";
					}
					
				}
				bookARoomService.saveBookedRoom(bookARoom);
				List<Hotelinfo> getHotelTariff = bookARoomService.getHotelTariff(bookARoom.getHotelinfo().getHotelId());
				Double cost = (double) ((bookARoom.getCheckOutDate().getTime() - bookARoom.getCheckInDate().getTime())
						/ (24 * 60 * 60 * 1000)) * (((Hotelinfo) getHotelTariff.get(0)).getTariff()) *(bookARoom.getNumberOfRooms());
				model.addAttribute("message",
						"Customer Booking Details is successfully saved with Booking ID "
								+ (146500 + bookARoom.getCustomerId()) + " and total Booking cost is " + cost + " for "
								+ bookARoom.getNumberOfRooms() + " Room/Rooms");

			} catch (ServiceException exception) {
				LOGGER.debug(exception.getMessage());
				model.addAttribute("message", "Customer Booking is Unsuccessful,Please try again");

			}
			
		}
		}
		return "home";
	}
	
	
	@RequestMapping("dropdown2options")
	public  @ResponseBody String getAllHotelInfo(@RequestParam("value")int value,Model model){
		List<Hotelinfo> hotelinfos;
		try {
			hotelinfos = bookARoomService.getAllHotelInfo(value);
		} catch (ServiceException exception) {
			LOGGER.debug(exception.getMessage());
			model.addAttribute("message", "Something went wrong Please try later");
			return "bookARoom";

		}
		JSONObject obj = new JSONObject();
		for(Hotelinfo hotelinfo : hotelinfos)
			obj.put(hotelinfo.getHotelId(), hotelinfo.getHotelName());
		return obj.toJSONString();
	}
	
	public boolean checknumberOfRooms(Customerinfo bookARoom) throws ServiceException{
		int available = 0;
		int booked = 0 ;
		try{
			
			available = bookARoomService.getNumberofHotelRoomsAvailable(bookARoom.getHotelinfo().getHotelId()).get(0).getNumberOfRooms();
			
			
			List<Customerinfo> customerinfos = bookARoomService.getNumberofHotelRoomsAvailableBooked(bookARoom.getHotelinfo().getHotelId(),bookARoom.getCheckInDate(),bookARoom.getCheckOutDate());
			for (Customerinfo customerinfo : customerinfos) {
				booked = booked + customerinfo.getNumberOfRooms();
			}
		}
		catch(Exception exception){
			LOGGER.debug(exception.getMessage());
			throw new ServiceException(exception);
		}
	
		if(available < (booked +bookARoom.getNumberOfRooms()) )
		{
			return false;
		}
		else
			return true;
	}
	
	
	@RequestMapping("getNumberofHotelRoomsAvailable")
	public  @ResponseBody String getNumberofHotelRoomsAvailable(@RequestParam("value")int value,@RequestParam("datepicker1")Date datepicker1,@RequestParam("datepicker2")Date datepicker2,Model model){
		int available;
		int booked = 0 ;
		try {
			available = bookARoomService.getNumberofHotelRoomsAvailable(value).get(0).getNumberOfRooms();
			
			List<Customerinfo> customerinfos = bookARoomService.getNumberofHotelRoomsAvailableBooked(value,datepicker1,datepicker2);
			for (Customerinfo customerinfo : customerinfos) {
				booked = booked + customerinfo.getNumberOfRooms();
			}
		} catch (ServiceException exception) {
			LOGGER.debug(exception.getMessage());
			model.addAttribute("message", "Something went wrong Please try later");
			return "bookARoom";

		}
		
		JSONObject obj = new JSONObject();
			obj.put(available, booked );
		return obj.toJSONString();
	}
	
}
