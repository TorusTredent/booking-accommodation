package by.bookingaccommodation.service;

import by.bookingaccommodation.entity.hotel.Room;
import by.bookingaccommodation.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<Room> findRoomsByHotelId(long hotelId) {
        log.info(String.format("Request list Rooms by hotelId %s", hotelId));
        return roomRepository.findAllByHotelId(hotelId).orElse(null);
    }
}
