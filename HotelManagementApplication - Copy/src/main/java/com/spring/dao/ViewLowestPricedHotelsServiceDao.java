package com.spring.dao;

import java.util.List;

import com.spring.model.Hotelinfo;

public interface ViewLowestPricedHotelsServiceDao {

	public List<Hotelinfo> getHotelInfo(int value) ;
}
