/**
 * 
 */
package com.practice.moonlightHotel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.practice.moonlightHotel.model.BookedRoom;

/**
 * @author arif.shaikh
 *
 */
public interface BookingRepository extends JpaRepository<BookedRoom, Long> {

	List<BookedRoom> findByRoomId(Long roomId);

	Optional<BookedRoom> findByBookingConfirmationCode(String confirmationCode);

	List<BookedRoom> findByGuestEmail(String email);
}