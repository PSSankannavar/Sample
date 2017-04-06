package com.spring.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.spring.dao.ViewLowestPricedHotelsServiceDao;
import com.spring.model.Hotelinfo;

@Repository
public class ViewLowestPricedHotelsServiceDaoImpl implements  ViewLowestPricedHotelsServiceDao{

	@Autowired
	public HibernateTemplate hibernateTemplate;
	
	public List<Hotelinfo> getHotelInfo(int value) {
		String query = "from Hotelinfo where cityinfo.cityId =  :value  order by tariff asc";
		List<Hotelinfo> hotelinfos =  (List<Hotelinfo>) (hibernateTemplate.findByNamedParam(query, "value", value));
		if(hotelinfos.size() > 5){
			List<Hotelinfo> hotelreturn = new ArrayList<Hotelinfo>();
			for (int i = 0; i < 5; i++) {
				  hotelreturn.add(( (Hotelinfo)hotelinfos.get(i)));
			}
			return hotelreturn;
		}
			
		return hotelinfos;
		
	}

}
