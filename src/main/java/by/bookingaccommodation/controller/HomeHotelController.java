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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hotel")
public class HomeHotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ModelMapper mapper;

    private List<Hotel> hotels;

    @GetMapping("/home")
    public String home(@RequestParam(defaultValue = "1") int page, Model model, HttpSession session) {
        String country = (String) session.getAttribute("country");
        if (session.getAttribute("hotels") == null) {
            hotels = hotelService.findHotelsByCountry(country);
            session.setAttribute("hotels", hotels);
        } else {

            hotels = (List<Hotel>) session.getAttribute("hotels");
        }
        model.addAttribute("hotelImages", getFirstHotelImage(hotels));
        model.addAttribute("hotelList", hotels);
        return "home";
    }

    @PostMapping("/search")
    public String search(@RequestParam("search") String search, HttpSession session) {
        String country = (String) session.getAttribute("country");
        hotels = hotelService.searchByNameAndCountry(search, country);
        session.setAttribute("hotels", hotels);
        return "redirect: /hotel/home";
    }

    @PostMapping("/sort")
    public String sorting(@RequestBody SortHotelDto sortHotelDto, HttpSession session) {
        String country = (String) session.getAttribute("country");
        hotels = findHotelsBySortAndCountry(sortHotelDto, country);
        session.setAttribute("hotels", hotels);
        return "redirect: /hotel/home";
    }

    @GetMapping()
    public String preview() {
        return "preview";
    }


    private List<Hotel> findHotelsBySortAndCountry(SortHotelDto sortHotelDto, String country) {
        List<Hotel> hotelsByCountry = hotelService.findHotelsByCountry(country);
        List<Room> roomsByHotelId = roomService.findRoomsByHotelId(hotelsByCountry);
        List<Room> roomsBySort = roomService.findRoomsBySort(roomsByHotelId, mapper.map(sortHotelDto, Room.class));
        List<Hotel> sortedHotelsByRooms = hotelService.findHotelsBySort(roomsBySort);
        return hotelService.sortedHotelsByRating(sortedHotelsByRooms, sortHotelDto.getRating());
    }

    private List<String> getFirstHotelImage(List<Hotel> hotels) {
        return hotels.stream().map(hotel -> hotel.getImageUrls().get(1)).collect(Collectors.toList());
    }
}
