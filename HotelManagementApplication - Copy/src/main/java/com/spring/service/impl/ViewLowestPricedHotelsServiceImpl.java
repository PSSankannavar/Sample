package com.spring.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dao.impl.ViewLowestPricedHotelsServiceDaoImpl;
import com.spring.model.Hotelinfo;

@Service
public class ViewLowestPricedHotelsServiceImpl {

	@Autowired
	public ViewLowestPricedHotelsServiceDaoImpl viewLowestPricedHotelsServiceDaoImpl;
	
	public List<Hotelinfo> getHotelInfo(int value) {
		List<Hotelinfo> hotelinfos =  viewLowestPricedHotelsServiceDaoImpl.getHotelInfo(value);
		return hotelinfos;
	}

}
