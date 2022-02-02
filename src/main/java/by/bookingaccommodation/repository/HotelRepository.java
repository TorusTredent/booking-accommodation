package by.bookingaccommodation.repository;

import by.bookingaccommodation.entity.hotel.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
