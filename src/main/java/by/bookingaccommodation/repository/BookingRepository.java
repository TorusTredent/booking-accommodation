package by.bookingaccommodation.repository;

import by.bookingaccommodation.entity.hotel.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> save(Booking booking);
    Optional<Booking> find(Booking booking);
    Optional<Long> findTopByOrderByIdDesc();

    Optional<Booking> findByNumber(long bookingNumber);
}
