package by.bookingaccommodation.controller;

import by.bookingaccommodation.dto.hotel.SortHotelDto;
import by.bookingaccommodation.entity.hotel.Hotel;
import by.bookingaccommodation.entity.hotel.Room;
import by.bookingaccommodation.service.HotelService;
import by.bookingaccommodation.service.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeHotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ModelMapper mapper;

    private List<Hotel> hotels;

    @GetMapping("/home")
    public String home(@RequestParam(defaultValue = "1") int page,
                       @RequestParam String search,
                       @RequestParam SortHotelDto sortHotelDto, Model model, HttpSession session) {
        if (search == null) {
            String country = (String) session.getAttribute("country");
            hotels = hotelService.findHotelsByCountry(country);
            if (sortHotelDto != null) {
                hotels = findHotelsBySort(sortHotelDto);
            }
        } else {
            hotels = hotelService.search(search);
        }
        model.addAttribute("hotels", hotels);
        return "home";
    }

    @GetMapping()
    public String preview() {
        return "preview";
    }


    private List<Hotel> findHotelsBySort(SortHotelDto sortHotelDto) {
        List<Room> rooms = roomService.findRoomsBySort(mapper.map(sortHotelDto, Room.class));
        List<Hotel> sortedHotelsByRooms = hotelService.findHotelsBySort(rooms);
        return hotelService.sortedByRating(sortedHotelsByRooms, sortHotelDto.getRating());
    }
}
