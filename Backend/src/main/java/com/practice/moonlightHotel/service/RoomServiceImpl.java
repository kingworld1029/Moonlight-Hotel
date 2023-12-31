package com.practice.moonlightHotel.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.practice.moonlightHotel.exception.InternalServerException;
import com.practice.moonlightHotel.exception.ResourceNotFoundException;
import com.practice.moonlightHotel.model.Room;
import com.practice.moonlightHotel.repository.RoomRepository;

@Service
public class RoomServiceImpl implements IRoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Override
	public Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice)
			throws SerialException, SQLException, IOException {
		Room room = new Room();
		room.setRoomType(roomType);
		room.setRoomPrice(roomPrice);
		if (!photo.isEmpty()) {
			String folderPath = "D:\\Practice\\Hotel Booking Project\\Backend\\Image\\";
			String filePath = folderPath + "_" + photo.getOriginalFilename();
			
			// Create the directory if it doesn't exist
			Path directoryPath = Paths.get(folderPath);
			if (!Files.exists(directoryPath)) {
				Files.createDirectories(directoryPath);
			}
			room.setPhotoPath(filePath);
			Files.write(Paths.get(filePath), photo.getBytes());
		}
		return roomRepository.save(room);
	}

	@Override
	public List<String> getAllRoomTypes() {
		return roomRepository.findDistictRoomTypes();
	}

	@Override
	public List<Room> getAllRooms() {
		return roomRepository.findAll();
	}

	@Override
	public byte[] getRoomPhotoByRoomId(Long roomId) throws IOException {
		Optional<Room> theRoom = roomRepository.findById(roomId);
		if (theRoom.isEmpty()) {
			throw new ResourceNotFoundException("No Room Found");
		}
		String filePath = theRoom.get().getPhotoPath();
		if (filePath != null) {
			return Files.readAllBytes(Paths.get(filePath));
		}
		return null;
	}

	@Override
	public void deleteRoom(Long roomId) {
		Optional<Room> theRoom = roomRepository.findById(roomId);
		if (theRoom.isPresent()) {
			roomRepository.deleteById(roomId);
		}

	}

	@Override
	public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, MultipartFile photo) throws IOException {
		Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found"));

		if (roomType != null)
			room.setRoomType(roomType);
		if (roomPrice != null)
			room.setRoomPrice(roomPrice);
		if (photo != null && !photo.isEmpty()) {
			String filePath = "D:\\Practice\\Hotel Booking Project\\Backend\\Image\\" + "_"
					+ photo.getOriginalFilename();
			room.setPhotoPath(filePath);
			Files.write(Paths.get(filePath), photo.getBytes());
		}

		return roomRepository.save(room);
	}

	@Override
	public Optional<Room> getRoomById(Long roomId) {
		return Optional.of(roomRepository.findById(roomId).get());
	}

	@Override
	public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
		return roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, roomType);
	}

}
