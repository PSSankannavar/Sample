package com.spring.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the hotelinfo database table.
 * 
 */
@Entity
@NamedQuery(name="Hotelinfo.findAll", query="SELECT h FROM Hotelinfo h")
public class Hotelinfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="hotel_id")
	private int hotelId;

	@Column(name="hotel_name")
	private String hotelName;

	@Column(name="number_of_rooms")
	private int numberOfRooms;

	@Column(name="star_rating")
	private double starRating;

	private double tariff;

	//bi-directional many-to-one association to Customerinfo
	@OneToMany(mappedBy="hotelinfo")
	private List<Customerinfo> customerinfos;

	//bi-directional many-to-one association to Cityinfo
	@ManyToOne
	@JoinColumn(name="city_id")
	private Cityinfo cityinfo;

	//bi-directional many-to-one association to Roominfo
	@OneToMany(mappedBy="hotelinfo")
	private List<Roominfo> roominfos;

	public Hotelinfo() {
	}

	public int getHotelId() {
		return this.hotelId;
	}

	public void setHotelId(int hotelId) {
		this.hotelId = hotelId;
	}

	public String getHotelName() {
		return this.hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public int getNumberOfRooms() {
		return this.numberOfRooms;
	}

	public void setNumberOfRooms(int numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	public double getStarRating() {
		return this.starRating;
	}

	public void setStarRating(double starRating) {
		this.starRating = starRating;
	}

	public double getTariff() {
		return this.tariff;
	}

	public void setTariff(double tariff) {
		this.tariff = tariff;
	}

	public List<Customerinfo> getCustomerinfos() {
		return this.customerinfos;
	}

	public void setCustomerinfos(List<Customerinfo> customerinfos) {
		this.customerinfos = customerinfos;
	}

	public Customerinfo addCustomerinfo(Customerinfo customerinfo) {
		getCustomerinfos().add(customerinfo);
		customerinfo.setHotelinfo(this);

		return customerinfo;
	}

	public Customerinfo removeCustomerinfo(Customerinfo customerinfo) {
		getCustomerinfos().remove(customerinfo);
		customerinfo.setHotelinfo(null);

		return customerinfo;
	}

	public Cityinfo getCityinfo() {
		return this.cityinfo;
	}

	public void setCityinfo(Cityinfo cityinfo) {
		this.cityinfo = cityinfo;
	}

	public List<Roominfo> getRoominfos() {
		return this.roominfos;
	}

	public void setRoominfos(List<Roominfo> roominfos) {
		this.roominfos = roominfos;
	}

	public Roominfo addRoominfo(Roominfo roominfo) {
		getRoominfos().add(roominfo);
		roominfo.setHotelinfo(this);

		return roominfo;
	}

	public Roominfo removeRoominfo(Roominfo roominfo) {
		getRoominfos().remove(roominfo);
		roominfo.setHotelinfo(null);

		return roominfo;
	}

}