package com.practice.moonlightHotel.model;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author arif.shaikh
 *
 */
@Entity
@Table(name = "TB_ROOM")
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "ROOM_TYPE")
	private String roomType;

	@Column(name = "ROOM_PRICE")
	private BigDecimal roomPrice;

	@Column(name = "IS_BOOKED")
	private boolean isBooked = false;

	 @Column(name = "PHOTO_PATH")
	 private String photoPath;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "room")
	@Column(name = "ROOM_ID")
	private List<BookedRoom> bookings;

	public Room() {
		this.bookings = new ArrayList<>();
	}

	public Room(Long id, String roomType, BigDecimal roomPrice, boolean isBooked, String photoPath,
			List<BookedRoom> bookings) {
		super();
		this.id = id;
		this.roomType = roomType;
		this.roomPrice = roomPrice;
		this.isBooked = isBooked;
		this.photoPath = photoPath;
		this.bookings = bookings;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public BigDecimal getRoomPrice() {
		return roomPrice;
	}

	public void setRoomPrice(BigDecimal roomPrice) {
		this.roomPrice = roomPrice;
	}

	public boolean isBooked() {
		return isBooked;
	}

	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}

	public List<BookedRoom> getBookings() {
		return bookings;
	}

	public void setBookings(List<BookedRoom> bookings) {
		this.bookings = bookings;
	}

	

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public void addBooking(BookedRoom booking) {
		if (bookings == null) {
			bookings = new ArrayList<>();
		}
		bookings.add(booking);
		booking.setRoom(this);
		isBooked = true;
		String bookingCode = RandomStringUtils.randomAlphabetic(4) + RandomStringUtils.randomNumeric(6);
		booking.setBookingConfirmationCode(bookingCode.toUpperCase());
	}

}
