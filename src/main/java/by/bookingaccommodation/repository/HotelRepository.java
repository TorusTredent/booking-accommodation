package by.bookingaccommodation.repository;

import by.bookingaccommodation.entity.hotel.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Optional<Hotel> findHotelById(long id);

    Optional<List<Hotel>> findAllByCountry(String country);

    @Query(value = "from Hotel where lower (name) like lower(:name)")
    Optional<List<Hotel>> findByName(@Param("name") String name);
}
