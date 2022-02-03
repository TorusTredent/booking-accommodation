package by.bookingaccommodation.repository;

import by.bookingaccommodation.entity.hotel.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Optional<Hotel> findHotelById(long id);
}
