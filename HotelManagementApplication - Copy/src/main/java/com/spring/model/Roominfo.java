package com.spring.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the roominfo database table.
 * 
 */
@Entity
@NamedQuery(name="Roominfo.findAll", query="SELECT r FROM Roominfo r")
public class Roominfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="room_number")
	private int roomNumber;

	@Column(name="room_cost")
	private int roomCost;

	//bi-directional many-to-one association to Hotelinfo
	@ManyToOne
	@JoinColumn(name="hotel_id")
	private Hotelinfo hotelinfo;

	public Roominfo() {
	}

	public int getRoomNumber() {
		return this.roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	public int getRoomCost() {
		return this.roomCost;
	}

	public void setRoomCost(int roomCost) {
		this.roomCost = roomCost;
	}

	public Hotelinfo getHotelinfo() {
		return this.hotelinfo;
	}

	public void setHotelinfo(Hotelinfo hotelinfo) {
		this.hotelinfo = hotelinfo;
	}

}