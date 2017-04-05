package com.spring.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the cityinfo database table.
 * 
 */
@Entity
@NamedQuery(name="Cityinfo.findAll", query="SELECT c FROM Cityinfo c")
public class Cityinfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="city_id")
	private int cityId;

	@Column(name="city_name")
	private String cityName;

	//bi-directional many-to-one association to Customerinfo
	@OneToMany(mappedBy="cityinfo")
	private List<Customerinfo> customerinfos;

	//bi-directional many-to-one association to Hotelinfo
	@OneToMany(mappedBy="cityinfo")
	private List<Hotelinfo> hotelinfos;

	public Cityinfo() {
	}

	public int getCityId() {
		return this.cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public List<Customerinfo> getCustomerinfos() {
		return this.customerinfos;
	}

	public void setCustomerinfos(List<Customerinfo> customerinfos) {
		this.customerinfos = customerinfos;
	}

	public Customerinfo addCustomerinfo(Customerinfo customerinfo) {
		getCustomerinfos().add(customerinfo);
		customerinfo.setCityinfo(this);

		return customerinfo;
	}

	public Customerinfo removeCustomerinfo(Customerinfo customerinfo) {
		getCustomerinfos().remove(customerinfo);
		customerinfo.setCityinfo(null);

		return customerinfo;
	}

	public List<Hotelinfo> getHotelinfos() {
		return this.hotelinfos;
	}

	public void setHotelinfos(List<Hotelinfo> hotelinfos) {
		this.hotelinfos = hotelinfos;
	}

	public Hotelinfo addHotelinfo(Hotelinfo hotelinfo) {
		getHotelinfos().add(hotelinfo);
		hotelinfo.setCityinfo(this);

		return hotelinfo;
	}

	public Hotelinfo removeHotelinfo(Hotelinfo hotelinfo) {
		getHotelinfos().remove(hotelinfo);
		hotelinfo.setCityinfo(null);

		return hotelinfo;
	}

}