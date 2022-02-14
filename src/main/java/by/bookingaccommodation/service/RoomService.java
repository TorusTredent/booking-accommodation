package by.bookingaccommodation.service;

import by.bookingaccommodation.entity.hotel.Hotel;
import by.bookingaccommodation.entity.hotel.Room;
import by.bookingaccommodation.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        roomRepository.save(room);
    }

    public List<Room> findRoomsByHotelId(long hotelId) {
        log.info(String.format("Request list Rooms by hotelId %s", hotelId));
        return roomRepository.findAllByHotelId(hotelId).orElse(null);
    }

    public List<Room> findRoomsByHotelId(List<Hotel> hotels) {
        List<Long> hotelIds = hotels.stream().map(Hotel::getId).collect(Collectors.toList());
        return roomRepository.findByHotelIdIn(hotelIds).orElse(null);
    }

    public Map<Long, Double> getRoomsCostByHotelId(List<Hotel> hotels) {
        List<Long> hotelIds = hotels.stream().map(Hotel::getId).collect(Collectors.toList());
        List<Room> rooms = roomRepository.findByHotelIdIn(hotelIds).orElse(null);
        return getRoomsCost(rooms, hotelIds);
    }

    public double getRoomCostByHotelId(long hotelId) {
        Room room = roomRepository.findByHotelId(hotelId).orElse(null);
        return room != null ? room.getCost() : 0;
    }

    private Map<Long, Double> getRoomsCost(List<Room> rooms, List<Long> hotelIds) {
        Map<Long, Double> cost = new HashMap<>();
        double minCost = rooms.get(0).getCost();
        int index = 0;
//        for (Long hotelId : hotelIds) {
//            for (int i = index; i < rooms.size(); i++) {
//                if (hotelId == rooms.get(i).getHotelId()) {
//                    if (minCost > rooms.get(i).getCost()) {
//                        minCost = rooms.get(i).getCost();
//                    }
//                } else {
//                    minCost = rooms.get(i).getCost();
//                    if (minCost > rooms.get(i).getCost()) {
//                        minCost = rooms.get(i).getCost();
//                    }
//                    if (index != 0) {
//                        index = --i;
//                    }
//                    cost.put(rooms.get(--i).getHotelId(), minCost);
//                    break;
//                }
//                index++;
//            }
//        }
        for (Room room : rooms) {
            cost.put(hotelIds.get(0), room.getCost());
        }
        return cost;
    }

    public List<Room> findRoomsBySort(List<Room> finalRooms, Room room) {
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
