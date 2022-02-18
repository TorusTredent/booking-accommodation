package by.bookingaccommodation.service;

import by.bookingaccommodation.entity.hotel.BookingPeriod;
import by.bookingaccommodation.entity.hotel.Hotel;
import by.bookingaccommodation.entity.hotel.Room;
import by.bookingaccommodation.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;


    public void save(Room room) {
        log.info(String.format("Room {} save", room));
        roomRepository.save(room);
    }

    public Room findRoomById(long id) {
        log.info(String.format("Request room by id {} exist", id));
        return roomRepository.findById(id).orElse(null);
    }

    public List<Room> findRoomsByHotelId(long hotelId) {
        log.info(String.format("Request list Rooms by hotelId %s", hotelId));
        return roomRepository.findAllByHotelId(hotelId).orElse(null);
    }

    public List<Room> findRoomsByHotelId(List<Hotel> hotels) {
        List<Long> hotelIds = hotels.stream().map(Hotel::getId).collect(Collectors.toList());
        log.info(String.format("Request list Rooms by hotelIds %s", hotelIds));
        return roomRepository.findByHotelIdIn(hotelIds).orElse(null);
    }

    public List<Room> findRoomsByDatePeriod(BookingPeriod period, List<Room> rooms) {
        log.info(String.format("Find list Rooms by date period %s", period));
        boolean flag = true;
        List<Room> finalRooms = new ArrayList<>();
        for (Room room : rooms) {
            for (int j = 0; j < room.getPeriods().size(); j++) {
                if (!period.getCheckOutDate().isBefore(room.getPeriods().get(j).getCheckInDate()) ||
                        !period.getCheckInDate().isAfter(room.getPeriods().get(j).getCheckOutDate())) {
                    flag = false;
                }
            }
            if (flag) {
                finalRooms.add(room);
                flag = true;
            }
        }
        return finalRooms;
    }

    public Map<Long, Double> getRoomsCostByHotelId(List<Hotel> hotels) {
        List<Long> hotelIds = hotels.stream().map(Hotel::getId).collect(Collectors.toList());
        log.info(String.format("Request map Cost by hotels %s", hotelIds));
        List<Room> rooms = roomRepository.findByHotelIdIn(hotelIds).orElse(null);
        return getRoomsCost(rooms, hotelIds);
    }

    public List<Room> findRoomsBySort(List<Room> finalRooms, Room room) {
        log.info(String.format("Sorting rooms by all parameters"));
        if (room.getCost() > 0 && finalRooms != null) {
            finalRooms = useFilterParameter(finalRooms, room, "cost");
        }
        if (room.getType() != null && finalRooms != null) {
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

        return finalRooms;
    }


    private Map<Long, Double> getRoomsCost(List<Room> rooms, List<Long> hotelIds) {
        Map<Long, Double> cost = new HashMap<>();
        double minCost = rooms.get(0).getCost();
        int index = 0;
        for (Long hotelId : hotelIds) {
            for (int i = index; i < rooms.size(); i++) {
                if (hotelId == rooms.get(i).getHotelId()) {
                    if (minCost > rooms.get(i).getCost()) {
                        minCost = rooms.get(i).getCost();
                    }
                } else {
                    index = i;
                    cost.put(hotelId, minCost);
                    minCost = rooms.get(i).getCost();
                    break;
                }
            }
        }
        return cost;
    }

    private List<Room> useFilterParameter(List<Room> rooms, Room room, String parameter) {
        List<Room> finalRooms = new ArrayList<>();
        boolean filter = false;
        for (Room roo : rooms) {
            switch (parameter) {
                case "cost": {
                    if (roo.getCost() >= room.getCost()) {
                        finalRooms.add(roo);
                        filter = true;
                    }
                    break;
                }
                case "type": {
                    if (roo.getType().equals(room.getType())) {
                        finalRooms.add(roo);
                        filter = true;
                    }
                    break;
                }
                case "numberOfBeds": {
                    if (roo.getNumberOfBeds() == room.getNumberOfBeds()) {
                        finalRooms.add(roo);
                        filter = true;
                    }
                    break;
                }
                case "conditioner": {
                    if (roo.isConditioner()) {
                        finalRooms.add(roo);
                        filter = true;
                    }
                    break;
                }
                case "TV": {
                    if (roo.isTV()) {
                        finalRooms.add(roo);
                        filter = true;
                    }
                    break;
                }
                case "internet": {
                    if (roo.isInternet()) {
                        finalRooms.add(roo);
                        filter = true;
                    }
                    break;
                }
                case "miniBar": {
                    if (roo.isMiniBar()) {
                        finalRooms.add(roo);
                        filter = true;
                    }
                    break;
                }
            }
        }
        return filter ? finalRooms : null;
    }
}
