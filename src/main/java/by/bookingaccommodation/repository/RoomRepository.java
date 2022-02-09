package by.bookingaccommodation.repository;

import by.bookingaccommodation.entity.hotel.Favor;
import by.bookingaccommodation.entity.hotel.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<List<Room>> findAllByHotelId(long hotelId);

    Optional<List<Room>> findAllByCostAfter(double cost);
}
