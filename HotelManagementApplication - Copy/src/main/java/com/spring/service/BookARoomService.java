package com.spring.service;

import java.util.Date;
import java.util.List;

import com.spring.exception.ServiceException;
import com.spring.model.Cityinfo;
import com.spring.model.Customerinfo;
import com.spring.model.Hotelinfo;

public interface BookARoomService {

	public  List<Cityinfo> getAllCityInfo() throws ServiceException;
	public List<Hotelinfo> getAllHotelInfo(int value) throws ServiceException;
	public void saveBookedRoom(Customerinfo bookARoom) throws ServiceException;
	public List<Hotelinfo> getHotelTariff(int hotelId) throws ServiceException;
	public List<Hotelinfo> getHotelInfo(int value) throws ServiceException ;
	public List<Hotelinfo> getNumberofHotelRoomsAvailable(int value) throws ServiceException ;
	public List<Customerinfo> getNumberofHotelRoomsAvailableBooked(int value,Date datepicker1,Date datepicker2) throws ServiceException;
}
