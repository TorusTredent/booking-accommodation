package by.bookingaccommodation.service;

import by.bookingaccommodation.entity.hotel.Favor;
import by.bookingaccommodation.repository.FavorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FavorService {

    @Autowired
    private FavorRepository favorRepository;

    public List<Favor> findFavorsByHotelId(long hotelId) {
        log.info(String.format("Request list Favors by hotelId %s", hotelId));
        return favorRepository.findAllByHotelId(hotelId).orElse(null);
    }

}
