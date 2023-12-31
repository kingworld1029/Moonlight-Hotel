/**
 * 
 */
package com.practice.moonlightHotel.service;

import java.util.List;

import com.practice.moonlightHotel.model.BookedRoom;

/**
 * @author arif.shaikh
 *
 */
public interface IBookingService {

	List<BookedRoom> getAllBookingByRoomId(Long roomId);

	List<BookedRoom> getAllBookings();

	BookedRoom findByBookingConfirmationCode(String confirmationCode);

	String saveBooking(Long roomId, BookedRoom bookingRequest);

	void cancelBooking(Long bookingId);
	
	 List<BookedRoom> getBookingsByUserEmail(String email);

}
