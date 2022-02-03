package by.bookingaccommodation.service;

import by.bookingaccommodation.entity.hotel.Booking;
import by.bookingaccommodation.entity.hotel.Hotel;
import by.bookingaccommodation.repository.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public Hotel findHotelById(long id) {
        log.info(String.format("Request hotelId {} exist", id));
        return hotelRepository.findHotelById(id).orElse(null);
    }

    public Hotel update(Hotel hotel, Hotel hotelDto) {
        log.info(String.format("Request update {}", hotel.getName()));
        hotelDto.setId(hotel.getId());
        hotelDto.setRating(hotel.getRating());
        return hotelRepository.save(hotelDto);
    }

    public void delete(Hotel hotel) {
        log.info(String.format("Request delete {}", hotel.getName()));
        hotelRepository.delete(hotel);
    }
}
