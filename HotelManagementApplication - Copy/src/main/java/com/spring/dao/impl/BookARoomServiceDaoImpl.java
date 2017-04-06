package com.spring.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.spring.dao.BookARoomServiceDao;
import com.spring.exception.FetchException;
import com.spring.model.Cityinfo;
import com.spring.model.Customerinfo;
import com.spring.model.Hotelinfo;

@Repository
public class BookARoomServiceDaoImpl implements BookARoomServiceDao {

	@Autowired
	public HibernateTemplate hibernateTemplate;
	
	private static final Logger LOGGER = Logger.getLogger(BookARoomServiceDaoImpl.class);
	
	
	public List<Cityinfo> getAllCityInfo() throws FetchException {
		System.out.println("BookARoomServiceDaoImpl getAllCityInfo ");
		try{
		return hibernateTemplate.loadAll(Cityinfo.class);
		}
		catch (DataAccessException exception) {
			LOGGER.debug(exception.getMessage());
			throw new FetchException(exception.getMessage(), exception);
		}
		
	}

	public List<Hotelinfo> getAllHotelInfo(int value) throws FetchException {
		String query = "from Hotelinfo where cityinfo.cityId =  :value";
		try{
		return (List<Hotelinfo>) hibernateTemplate.findByNamedParam(query, "value", value);
		}
		catch (DataAccessException exception) {
			LOGGER.debug(exception.getMessage());
			throw new FetchException(exception.getMessage(), exception);
		}
		
	}

	
	public List<Hotelinfo> getHotelInfo(int value) throws FetchException {
		String query = "from Hotelinfo where hotelId =  :value";
		try{
		return (List<Hotelinfo>) hibernateTemplate.findByNamedParam(query, "value", value);
		}
		catch (DataAccessException exception) {
			LOGGER.debug(exception.getMessage());
			throw new FetchException(exception.getMessage(), exception);
		}
		
	}
	
	public void saveBookedRoom(Customerinfo bookARoom) throws FetchException {
		try {
			hibernateTemplate.save(bookARoom);
		} catch (DataAccessException exception) {
			LOGGER.debug(exception.getMessage());
			throw new FetchException(exception.getMessage(), exception);
		} catch (Exception exception) {
			LOGGER.debug(exception.getMessage());
			throw new FetchException(exception.getMessage(), exception);
		}
	}

	public List<Hotelinfo> getHotelTariff(int hotelId) throws FetchException {
		String query = "from Hotelinfo where hotelId =  :value";
		try{
		return (List<Hotelinfo>) hibernateTemplate.findByNamedParam(query, "value", hotelId);
		}
		catch (DataAccessException exception) {
			LOGGER.debug(exception.getMessage());
			throw new FetchException(exception.getMessage(), exception);
		}
	}


	public List<Hotelinfo> getNumberofHotelRoomsAvailable(int hotelId) throws FetchException {
		String query = "from Hotelinfo where hotelId =  :hotelId";
		try{
		return (List<Hotelinfo>) hibernateTemplate.findByNamedParam(query, "hotelId" ,hotelId);
		}
		catch (DataAccessException exception) {
			LOGGER.debug(exception.getMessage());
			throw new FetchException(exception.getMessage(), exception);
		}
	}
	public List<Customerinfo> getNumberofHotelRoomsAvailableBooked(int hotelId,Date datepicker1,Date datepicker2) throws FetchException {
		try{
		String query = "from Customerinfo where hotelinfo.hotelId =  ? and (((? BETWEEN checkInDate and checkOutDate) and (? BETWEEN checkInDate and checkOutDate)) or (? < checkInDate and (checkOutDate <= ? or ? <= checkOutDate)))";
		return (List<Customerinfo>) hibernateTemplate.find( query, new Object[]{hotelId,datepicker1,datepicker2,datepicker1,datepicker2,datepicker2});
		}
		catch (DataAccessException exception) {
			LOGGER.debug(exception.getMessage());
			throw new FetchException(exception.getMessage(), exception);
		}
	}
	
}
