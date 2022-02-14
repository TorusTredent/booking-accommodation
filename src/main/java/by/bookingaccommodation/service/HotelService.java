package by.bookingaccommodation.service;

import by.bookingaccommodation.entity.hotel.Hotel;
import by.bookingaccommodation.entity.hotel.Room;
import by.bookingaccommodation.repository.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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

    public List<Hotel> sortedByCountry(List<Hotel> hotels, String country) {
        return hotels.stream().filter(hotel -> hotel.getCountry().equals(country)).collect(Collectors.toList());
    }

    public List<Hotel> findHotelsBySort(List<Room> rooms) {
        if (rooms == null) {
            return null;
        } else {
            List<Long> hotelIdList = rooms.stream().map(Room::getHotelId).collect(Collectors.toList());
            return hotelIdList.isEmpty() ? null : hotelRepository.findAllById(hotelIdList);
        }
    }

    public List<Hotel> searchByNameAndCountry(String search, String country) {
        return hotelRepository.findByNameAndCountry("%" + search + "%", country).orElse(null);
    }

    public Page<Hotel> findPaginated(int pageSize, int currentPage, List<Hotel> hotels) {
        int startItem = (currentPage - 1) * pageSize;
        List<Hotel> list;

        if (hotels.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, hotels.size());
            list = hotels.subList(startItem, toIndex);
        }
        Page<Hotel> hotelPage = new PageImpl<>(list,  PageRequest.of(currentPage - 1, pageSize), hotels.size());
        return hotelPage;
    }

    public List<Hotel> sortedHotelsByRating(List<Hotel> hotels, double rating) {
        List<Hotel> finalHotels = new ArrayList<>();
        if (hotels == null) {
            return null;
        }
        for (Hotel hotel : hotels) {
            if (hotel.getRating() >= rating) {
                finalHotels.add(hotel);
            }
        }
        return finalHotels.isEmpty() ? null : finalHotels;
    }

}
