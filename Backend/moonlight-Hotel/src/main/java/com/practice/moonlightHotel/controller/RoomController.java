package com.practice.moonlightHotel.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.practice.moonlightHotel.dto.BookingResponseDTO;
import com.practice.moonlightHotel.dto.RoomResponseDTO;
import com.practice.moonlightHotel.exception.PhotoRetrievalException;
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
@RequestMapping("/rooms")
public class RoomController {

	@Autowired
	private IRoomService roomService;

	@Autowired
	private IBookingService bookingService;

	@PostMapping("/addNewRoom")
	 @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<RoomResponseDTO> addNewRoom(@RequestParam("photo") MultipartFile photo,
			@RequestParam("roomType") String roomType, @RequestParam("roomPrice") BigDecimal roomPrice)
			throws SerialException, SQLException, IOException {
		Room saveRoom = roomService.addNewRoom(photo, roomType, roomPrice);
		RoomResponseDTO responseDTO = new RoomResponseDTO(saveRoom.getId(), saveRoom.getRoomType(),
				saveRoom.getRoomPrice());
		return ResponseEntity.ok(responseDTO);

	}

	@GetMapping("/getAllRoomTypes")
	public List<String> getAllRoomTypes() {
		return roomService.getAllRoomTypes();
	}

	@GetMapping("/getAllRooms")
	public ResponseEntity<List<RoomResponseDTO>> getAllRooms() throws SQLException {
		List<Room> rooms = roomService.getAllRooms();
		List<RoomResponseDTO> roomResponseDTOs = new ArrayList<>();
		for (Room room : rooms) {
			try {
				RoomResponseDTO roomResponseDTO = getRoomResponse(room);
				roomResponseDTOs.add(roomResponseDTO);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return ResponseEntity.ok(roomResponseDTOs);

	}

	@DeleteMapping("/deleteRoom/{roomId}")
	 @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
		roomService.deleteRoom(roomId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/updateRoom/{roomId}")
	 @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<RoomResponseDTO> updateRoom(@PathVariable Long roomId,
			@RequestParam(required = false) String roomType, @RequestParam(required = false) BigDecimal roomPrice,
			@RequestParam(required = false) MultipartFile photo) throws SQLException, IOException {
		Room theRoom = roomService.updateRoom(roomId, roomType, roomPrice, photo);
		RoomResponseDTO roomResponse = getRoomResponse(theRoom);
		return ResponseEntity.ok(roomResponse);
	}

	@GetMapping("/getRoom/{roomId}")
	public ResponseEntity<Optional<RoomResponseDTO>> getRoomById(@PathVariable Long roomId) {
		Optional<Room> theRoom = roomService.getRoomById(roomId);
		return theRoom.map(room -> {
			RoomResponseDTO roomResponse = getRoomResponse(room);
			return ResponseEntity.ok(Optional.of(roomResponse));
		}).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
	}

	@GetMapping("/availableRooms")
	public ResponseEntity<List<RoomResponseDTO>> getAvailableRooms(
			@RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
			@RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
			@RequestParam("roomType") String roomType) throws SQLException {
		List<Room> availableRooms = roomService.getAvailableRooms(checkInDate, checkOutDate, roomType);
		List<RoomResponseDTO> roomResponses = new ArrayList<>();
		for (Room room : availableRooms) {
			RoomResponseDTO roomResponse = getRoomResponse(room);
			roomResponses.add(roomResponse);
		}
		if (roomResponses.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(roomResponses);
		}
	}

	private RoomResponseDTO getRoomResponse(Room room) {
		List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId());
		List<BookingResponseDTO> bookingResponseDTOs = bookings.stream()
				.map(booking -> new BookingResponseDTO(booking.getId(), booking.getCheckInDate(),
						booking.getCheckOutDate(), booking.getBookingConfirmationCode()))
				.collect(Collectors.toList());
		String[] parts = room.getPhotoPath().split("_");
		room.setPhotoPath(parts[1]);
		return new RoomResponseDTO(room.getId(), room.getRoomType(), room.getRoomPrice(), room.isBooked(),
				room.getPhotoPath(), bookingResponseDTOs);
	}

	private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
		return bookingService.getAllBookingByRoomId(roomId);
	}
}
