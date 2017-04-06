package com.spring.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * The persistent class for the customerinfo database table.
 * 
 */
@Entity
public class Customerinfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="customer_id")
	private int customerId;

	@DateTimeFormat(pattern="dd-MM-yyyy")
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name="check_in_date")
	private Date checkInDate;

	@DateTimeFormat(pattern="dd-MM-yyyy")
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name="check_out_date")
	private Date checkOutDate;

	@Column(name="customer_name")
	private String customerName;


	@Column(name="number_of_rooms")
	private int numberOfRooms;

	//bi-directional many-to-one association to Cityinfo
	@ManyToOne
	@JoinColumn(name="city_id")
	private Cityinfo cityinfo;

	//bi-directional many-to-one association to Hotelinfo
	@ManyToOne
	@JoinColumn(name="hotel_id")
	private Hotelinfo hotelinfo;

	public Customerinfo() {
	}

	public int getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public Date getCheckInDate() {
		return this.checkInDate;
	}

	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}

	public Date getCheckOutDate() {
		return this.checkOutDate;
	}

	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public int getNumberOfRooms() {
		return this.numberOfRooms;
	}

	public void setNumberOfRooms(int numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	public Cityinfo getCityinfo() {
		return this.cityinfo;
	}

	public void setCityinfo(Cityinfo cityinfo) {
		this.cityinfo = cityinfo;
	}

	public Hotelinfo getHotelinfo() {
		return this.hotelinfo;
	}

	public void setHotelinfo(Hotelinfo hotelinfo) {
		this.hotelinfo = hotelinfo;
	}

}