package com.spring.dao;

import java.util.Date;
import java.util.List;

import com.spring.exception.FetchException;
import com.spring.model.Cityinfo;
import com.spring.model.Customerinfo;
import com.spring.model.Hotelinfo;

public interface BookARoomServiceDao {

	
	public List<Cityinfo> getAllCityInfo() throws FetchException ;
	public List<Hotelinfo> getAllHotelInfo(int value) throws FetchException;
	public List<Hotelinfo> getHotelInfo(int value) throws FetchException;
	public void saveBookedRoom(Customerinfo bookARoom) throws FetchException ;
	public List<Hotelinfo> getHotelTariff(int hotelId) throws FetchException ;
	public List<Hotelinfo> getNumberofHotelRoomsAvailable(int hotelId) throws FetchException;
	public List<Customerinfo> getNumberofHotelRoomsAvailableBooked(int hotelId,Date datepicker1,Date datepicker2) throws FetchException;
}
