package com.practice.moonlightHotel.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practice.moonlightHotel.dto.BookingResponseDTO;
import com.practice.moonlightHotel.dto.RoomResponseDTO;
import com.practice.moonlightHotel.exception.InvalidBookingRequestException;
import com.practice.moonlightHotel.exception.ResourceNotFoundException;
import com.practice.moonlightHotel.model.BookedRoom;
import com.practice.moonlightHotel.model.Room;
import com.practice.moonlightHotel.service.IBookingService;
import com.practice.moonlightHotel.service.IRoomService;

/**
 * @author arif.shaikh
 *
 */
@RestController
@RequestMapping("/bookings")
public class BookingController {
	
	@Autowired
	private IBookingService bookingService;
	
	@Autowired
	private IRoomService roomService;

	@GetMapping("/getAllBookings")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<BookingResponseDTO>> getAllBookings(){
		List<BookedRoom> bookings = bookingService.getAllBookings();
		List<BookingResponseDTO> bookingResponseDTOs = new ArrayList<BookingResponseDTO>();
		for(BookedRoom booking :bookings) {
			BookingResponseDTO bookingResponseDTO = getBookingResponse(booking);
			bookingResponseDTOs.add(bookingResponseDTO);
			
		}
		return ResponseEntity.ok(bookingResponseDTOs);
		
	}

	@GetMapping("/getBookingByConfirmationCode/{confirmationCode}")
	public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
		try {
			BookedRoom booking= bookingService.findByBookingConfirmationCode(confirmationCode);
			BookingResponseDTO bookingResponseDTO = getBookingResponse(booking);
			return ResponseEntity.ok(bookingResponseDTO);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@PostMapping("/saveBooking/{roomId}")
	public ResponseEntity<?> saveBooking(@PathVariable Long roomId,@RequestBody BookedRoom bookingRequest){
		try {
			String ConfirmationCode = bookingService.saveBooking(roomId,bookingRequest);
			return ResponseEntity.ok("Room booked Successfully! Your Confirmation code is:-"+ConfirmationCode);
		} catch (InvalidBookingRequestException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("/user/{email}/bookings")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByUserEmail(@PathVariable String email) {
        List<BookedRoom> bookings = bookingService.getBookingsByUserEmail(email);
        List<BookingResponseDTO> bookingResponses = new ArrayList<>();
        for (BookedRoom booking : bookings) {
            BookingResponseDTO bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }
	
	@DeleteMapping("/cancelBooking/{bookingId}")
	public void cancelBooking(@PathVariable Long bookingId) {
		bookingService.cancelBooking(bookingId);
		
	}
	

	private BookingResponseDTO getBookingResponse(BookedRoom booking) {
		Room theRoom = roomService.getRoomById(booking.getRoom().getId()).get();
		RoomResponseDTO roomResponseDTO = new  RoomResponseDTO(theRoom.getId(), theRoom.getRoomType(), theRoom.getRoomPrice());
		return new BookingResponseDTO(booking.getId(),booking.getCheckInDate(),booking.getCheckOutDate(),booking.getGuestFullName(),
		booking.getGuestEmail(),booking.getNumOfAdults(),booking.getNumOfChildren(),booking.getTotalNumOfGuest(),booking.getBookingConfirmationCode(), roomResponseDTO);
	}
	
	
}
