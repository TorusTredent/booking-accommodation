package by.bookingaccommodation.service;

import by.bookingaccommodation.entity.hotel.Hotel;
import by.bookingaccommodation.entity.hotel.Room;
import by.bookingaccommodation.repository.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public Hotel save(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

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

    public List<Hotel> findHotelsByCountry(String country) {
        return hotelRepository.findAllByCountry(country).orElse(null);
    }

    public List<Hotel> findHotelsBySort(List<Room> rooms) {
        List<Long> hotelIdList = rooms.stream().map(Room::getHotelId).collect(Collectors.toList());
        return !hotelIdList.isEmpty() ? hotelRepository.findAllById(hotelIdList) : null;
    }

    public List<Hotel> search(String search) {
        return hotelRepository.findByName("%" + search + "%").orElse(null);
    }

    public List<Hotel> sortedByRating(List<Hotel> hotels, double rating) {
        List<Hotel> finalHotels = new ArrayList<>();
        for (Hotel hotel : hotels) {
            if (hotel.getRating() >= rating) {
                finalHotels.add(hotel);
            }
        }
        return finalHotels;
    }
}
