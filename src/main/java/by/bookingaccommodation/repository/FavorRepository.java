package by.bookingaccommodation.repository;

import by.bookingaccommodation.entity.hotel.Favor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavorRepository extends JpaRepository<Favor, Long> {

    Optional<List<Favor>> findAllByHotelId(long hotelId);
}
