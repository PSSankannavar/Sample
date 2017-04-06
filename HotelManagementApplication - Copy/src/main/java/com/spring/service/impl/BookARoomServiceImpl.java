package com.spring.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.impl.BookARoomServiceDaoImpl;
import com.spring.exception.FetchException;
import com.spring.exception.ServiceException;
import com.spring.model.Cityinfo;
import com.spring.model.Customerinfo;
import com.spring.model.Hotelinfo;

@Service
public class BookARoomServiceImpl {

	@Autowired
	public BookARoomServiceDaoImpl bookARoomServiceDaoImpl;
	
	
	private static final Logger LOGGER = Logger.getLogger(BookARoomServiceImpl.class);
	
	@Transactional
	public  List<Cityinfo> getAllCityInfo() throws ServiceException {
		List<Cityinfo> cityinfos = null;
		try {
			cityinfos = bookARoomServiceDaoImpl.getAllCityInfo();
		} catch (FetchException exception) {
			LOGGER.debug(exception.getMessage());
			throw new ServiceException(exception.getMessage(), exception);
		}
		return cityinfos;
	}

	@Transactional
	public List<Hotelinfo> getAllHotelInfo(int value) throws ServiceException {
		List<Hotelinfo> hotelinfos = null;
		try {
			hotelinfos = bookARoomServiceDaoImpl.getAllHotelInfo(value);
		} catch (FetchException exception) {
			LOGGER.debug(exception.getMessage());
			throw new ServiceException(exception.getMessage(), exception);
		}
		return hotelinfos;
	}

	@Transactional
	public void saveBookedRoom(Customerinfo bookARoom) throws ServiceException {
		try {
			bookARoomServiceDaoImpl.saveBookedRoom(bookARoom);
		} catch (Exception exception) {
			LOGGER.debug(exception.getMessage());
			throw new ServiceException(exception.getMessage(), exception);
		}

	}

	@Transactional
	public List<Hotelinfo> getHotelTariff(int hotelId) throws ServiceException {
		List<Hotelinfo> hotelinfos = null;
		try {
			hotelinfos = bookARoomServiceDaoImpl.getHotelTariff(hotelId);
		} catch (FetchException exception) {
			LOGGER.debug(exception.getMessage());
			throw new ServiceException(exception.getMessage(), exception);
		}
		return hotelinfos;
	}
	
	
	public List<Hotelinfo> getHotelInfo(int value) throws ServiceException {
		List<Hotelinfo> hotelinfos = null;
		try {
			hotelinfos = bookARoomServiceDaoImpl.getHotelInfo(value);
		} catch (FetchException exception) {
			LOGGER.debug(exception.getMessage());
			throw new ServiceException(exception.getMessage(), exception);
		}
		return hotelinfos;
		
	}

	public List<Hotelinfo> getNumberofHotelRoomsAvailable(int value) throws ServiceException {
		List<Hotelinfo> hotelinfos = null;
		try {
			hotelinfos = bookARoomServiceDaoImpl.getNumberofHotelRoomsAvailable(value);
		} catch (FetchException exception) {
			LOGGER.debug(exception.getMessage());
			throw new ServiceException(exception.getMessage(), exception);
		}
		return hotelinfos;
	}

	public List<Customerinfo> getNumberofHotelRoomsAvailableBooked(int value,Date datepicker1,Date datepicker2) throws ServiceException {
		List<Customerinfo> hotelinfos = null;
		try {
			hotelinfos = bookARoomServiceDaoImpl.getNumberofHotelRoomsAvailableBooked(value,datepicker1,datepicker2);
		} catch (FetchException exception) {
			LOGGER.debug(exception.getMessage());
			throw new ServiceException(exception.getMessage(), exception);
		}
		return hotelinfos;
	}
}
