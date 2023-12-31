package com.practice.moonlightHotel.dto;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

public class RoomResponseDTO {

	private Long id;

	private String roomType;

	private BigDecimal roomPrice;

	private boolean isBooked = false;

	private String photoPath;

	private List<BookingResponseDTO> bookings;

	public RoomResponseDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RoomResponseDTO(Long id, String roomType, BigDecimal roomPrice) {
		super();
		this.id = id;
		this.roomType = roomType;
		this.roomPrice = roomPrice;
	}

	public RoomResponseDTO(Long id, String roomType, BigDecimal roomPrice, boolean isBooked, String photoPath,
			List<BookingResponseDTO> bookings) {
		super();
		this.id = id;
		this.roomType = roomType;
		this.roomPrice = roomPrice;
		this.isBooked = isBooked;
		this.photoPath =photoPath;
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

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public List<BookingResponseDTO> getBookings() {
		return bookings;
	}

	public void setBookings(List<BookingResponseDTO> bookings) {
		this.bookings = bookings;
	}

}
