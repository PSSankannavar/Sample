package com.spring.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gemstone.org.json.JSONArray;
import com.spring.exception.ServiceException;
import com.spring.model.Cityinfo;
import com.spring.model.Hotelinfo;
import com.spring.service.impl.BookARoomServiceImpl;
import com.spring.service.impl.ViewLowestPricedHotelsServiceImpl;

@Controller
public class ViewLowestPricedHotels {

	@Autowired
	public BookARoomServiceImpl bookARoomService;
	
	@Autowired
	public ViewLowestPricedHotelsServiceImpl viewLowestPricedHotels;
	
	
	private static final Logger LOGGER = Logger.getLogger(ViewLowestPricedHotels.class);
	
	@ModelAttribute("cityinfo")
	public List<Cityinfo> getAllCityInfo() throws ServiceException{
		List<Cityinfo> cityinfos = null;
		try {
			cityinfos = bookARoomService.getAllCityInfo();
		} catch (ServiceException exception) {
			LOGGER.debug(exception.getMessage());
			throw new ServiceException(exception.getMessage(), exception);
		}
		
		return cityinfos;
	}
	
	@RequestMapping("viewLowestPricedRooms.view")
	public String getBookARoomPage(Model model)
	{
		model.addAttribute("hotel" , new Hotelinfo());
		return "ViewLowestPricedRooms";
	}
	
	@RequestMapping("viewLowestPrice")
	public  @ResponseBody String getHotelInfo(@RequestParam("value")int value,Model model){
	
		
		List<Hotelinfo> hotelinfos =  viewLowestPricedHotels.getHotelInfo(value);
		
		//JSONObject obj = new JSONObject();
		/*for(Hotelinfo hotelinfo : hotelinfos)
			obj.put(hotelinfo.getTariff(), hotelinfo.getHotelName());*/
		JSONArray array =new JSONArray();
		return array.put(hotelinfos).toString();
	}
	
	
}
