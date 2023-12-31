/**
 * 
 */
package com.practice.moonlightHotel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.moonlightHotel.exception.InvalidBookingRequestException;
import com.practice.moonlightHotel.exception.ResourceNotFoundException;
import com.practice.moonlightHotel.model.BookedRoom;
import com.practice.moonlightHotel.model.Room;
import com.practice.moonlightHotel.repository.BookingRepository;

/**
 * @author arif.shaikh
 *
 */
@Service
public class BookingServiceImpl implements IBookingService {
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private IRoomService roomService;
	
	@Override
	public List<BookedRoom> getAllBookings() {
		return bookingRepository.findAll();
	}
	
	@Override
	public List<BookedRoom> getAllBookingByRoomId(Long roomId) {
		return bookingRepository.findByRoomId(roomId);
	}



	@Override
	public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
		 return bookingRepository.findByBookingConfirmationCode(confirmationCode)
	                .orElseThrow(() -> new ResourceNotFoundException("No booking found with booking code :"+confirmationCode));

	}

	@Override
	public String saveBooking(Long roomId, BookedRoom bookingRequest) {
		if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
			throw new InvalidBookingRequestException("Check In date cannot be greater than checkout date");
		}
		Room room = roomService.getRoomById(roomId).get();
		List<BookedRoom> existingBookings = room.getBookings();
		boolean roomIsAvailable = isRoomAvailable(bookingRequest,existingBookings);
		if (roomIsAvailable){
            room.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        }else{
            throw  new InvalidBookingRequestException("Sorry, This room is not available for the selected dates;");
        }
        return bookingRequest.getBookingConfirmationCode();
	}

	private boolean isRoomAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
		return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
	}

	@Override
	public void cancelBooking(Long bookingId) {
		bookingRepository.deleteById(bookingId);
		
	}

	@Override
	public List<BookedRoom> getBookingsByUserEmail(String email) {
		return bookingRepository.findByGuestEmail(email);
	}

}
