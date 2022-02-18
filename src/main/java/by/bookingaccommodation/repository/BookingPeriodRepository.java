package by.bookingaccommodation.repository;

import by.bookingaccommodation.entity.hotel.BookingPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingPeriodRepository extends JpaRepository<BookingPeriod, Long> {

}
