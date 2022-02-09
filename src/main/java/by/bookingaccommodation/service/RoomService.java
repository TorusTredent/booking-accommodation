package by.bookingaccommodation.service;

import by.bookingaccommodation.entity.hotel.Hotel;
import by.bookingaccommodation.entity.hotel.Room;
import by.bookingaccommodation.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    private List<Room> finalRooms;

    public List<Room> findRoomsByHotelId(long hotelId) {
        log.info(String.format("Request list Rooms by hotelId %s", hotelId));
        return roomRepository.findAllByHotelId(hotelId).orElse(null);
    }

    public List<Room> findRoomsBySort(Room room) {
        finalRooms = roomRepository.findAllByCostAfter(room.getCost()).orElse(null);

        if (room.getType() != null) {
            finalRooms = useFilterParameter(finalRooms, room, "type");
        }
        if (room.getNumberOfBeds() >= 1 && finalRooms != null) {
            finalRooms = useFilterParameter(finalRooms, room, "numberOfBeds");

        }
        if (room.isConditioner() && finalRooms != null) {
            finalRooms = useFilterParameter(finalRooms, room, "conditioner");
        }
        if (room.isTV() && finalRooms != null) {
            finalRooms = useFilterParameter(finalRooms, room, "TV");
        }
        if (room.isInternet() && finalRooms != null) {
            finalRooms = useFilterParameter(finalRooms, room, "internet");
        }
        if (room.isMiniBar() && finalRooms != null) {
            finalRooms = useFilterParameter(finalRooms, room, "miniBar");
        }
        if (room.isAvailabilityOfABathroom() && finalRooms != null) {
            finalRooms = useFilterParameter(finalRooms, room, "availabilityOfABathroom");
        }
        return finalRooms;
    }


    private List<Room> useFilterParameter(List<Room> rooms, Room room, String parameter) {
        List<Room> finalRooms = new ArrayList<>();
        boolean filter = false;
        for (Room roo : rooms) {
            switch (parameter) {
                case "type": {
                    if (roo.getType().equals(room.getType())) {
                        finalRooms.add(roo);
                        filter = true;
                    }
                }
                case "numberOfBeds": {
                    if (roo.getNumberOfBeds() == room.getNumberOfBeds()) {
                        finalRooms.add(roo);
                        filter = true;
                    }
                }
                case "conditioner": {
                    if (roo.isConditioner()) {
                        finalRooms.add(roo);
                        filter = true;
                    }
                }
                case "TV": {
                    if (roo.isTV()) {
                        finalRooms.add(roo);
                        filter = true;
                    }
                }
                case "internet": {
                    if (roo.isInternet()) {
                        finalRooms.add(roo);
                        filter = true;
                    }
                }
                case "miniBar": {
                    if (roo.isMiniBar()) {
                        finalRooms.add(roo);
                        filter = true;
                    }
                }
                case "availabilityOfABathroom": {
                    if (roo.isAvailabilityOfABathroom()) {
                        finalRooms.add(roo);
                        filter = true;
                    }
                }
            }
        }
        return filter ? finalRooms : null;
    }
}
