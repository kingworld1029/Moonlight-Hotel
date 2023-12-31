/**
 * 
 */
package com.practice.moonlightHotel.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialException;

import org.springframework.web.multipart.MultipartFile;

import com.practice.moonlightHotel.model.Room;

/**
 * @author arif.shaikh
 *
 */
public interface IRoomService {

	Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice) throws SerialException, SQLException, IOException;

	List<String> getAllRoomTypes();

	List<Room> getAllRooms();

	byte[] getRoomPhotoByRoomId(Long id) throws  IOException;

	void deleteRoom(Long roomId);

	Optional<Room> getRoomById(Long roomId);

	Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, MultipartFile photo) throws IOException;
	
	 List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

}
