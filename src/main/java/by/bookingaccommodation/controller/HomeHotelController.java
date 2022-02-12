package by.bookingaccommodation.controller;

import by.bookingaccommodation.dto.hotel.SortHotelDto;
import by.bookingaccommodation.entity.hotel.Hotel;
import by.bookingaccommodation.entity.hotel.Room;
import by.bookingaccommodation.service.HotelService;
import by.bookingaccommodation.service.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    private static final int HOTELS_PER_PAGE = 5;

    @GetMapping("/home/{currentPage}")
    public String home(@PathVariable() int currentPage,
                       Model model, HttpSession session) {
        String country = (String) session.getAttribute("country");
        hotels = (List<Hotel>) session.getAttribute("hotels");
        if (hotels == null || hotels.isEmpty()) {
            hotels = hotelService.findHotelsByCountry(country);
            session.setAttribute("hotels", hotels);
        }
        Page<Hotel> hotelPage = hotelService.findPaginated(HOTELS_PER_PAGE, currentPage, hotels);
        List<Hotel> hotelList = hotelPage.getContent();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", hotelPage.getTotalPages());
        model.addAttribute("totalItems", hotelPage.getTotalElements());
        model.addAttribute("hotelList", hotelList);
        return "homeHotel";
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

    private List<Hotel> findHotelsBySortAndCountry(SortHotelDto sortHotelDto, String country) {
        List<Hotel> hotelsByCountry = hotelService.findHotelsByCountry(country);
        List<Room> roomsByHotelId = roomService.findRoomsByHotelId(hotelsByCountry);
        List<Room> roomsBySort = roomService.findRoomsBySort(roomsByHotelId, mapper.map(sortHotelDto, Room.class));
        List<Hotel> sortedHotelsByRooms = hotelService.findHotelsBySort(roomsBySort);
        return hotelService.sortedHotelsByRating(sortedHotelsByRooms, sortHotelDto.getRating());
    }


}
