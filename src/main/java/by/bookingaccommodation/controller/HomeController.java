package by.bookingaccommodation.controller;

import by.bookingaccommodation.dto.hotel.FilterHotelDto;
import by.bookingaccommodation.entity.hotel.BookingPeriod;
import by.bookingaccommodation.entity.hotel.Hotel;
import by.bookingaccommodation.entity.hotel.Room;
import by.bookingaccommodation.service.HotelService;
import by.bookingaccommodation.service.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ModelMapper mapper;

    private static final int HOTELS_PER_PAGE = 5;
    private boolean operation;

    @GetMapping(value = {"/{page}", "/", ""})
    public String home(@PathVariable Optional<Integer> page,
                       Model model, HttpSession session) {
        int currentPage = page.orElse(1);
        String country = (String) session.getAttribute("country");
        if (country == null) {
            country = "Belarus";
        }
        List<Hotel> hotels = (List<Hotel>) session.getAttribute("hotels");
        BookingPeriod bookingPeriod = (BookingPeriod) session.getAttribute("datePeriod");
        if (operation && hotels == null) {
            model.addAttribute("hotelsNotFound", "Hotels not found");
        }
        if (hotels == null || hotels.isEmpty()) {
            hotels = hotelService.findHotelsByCountry(country);
            session.setAttribute("hotels", hotels);
        }
        Page<Hotel> hotelPage = hotelService.findPaginated(HOTELS_PER_PAGE, currentPage, hotels);
        List<Hotel> hotelList = hotelPage.getContent();
        Map<Long, Double> roomsCostByHotelId = roomService.getRoomsCostByHotelId(hotels);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("filterHotels", new FilterHotelDto());
        model.addAttribute("totalPages", hotelPage.getTotalPages());
        model.addAttribute("totalItems", hotelPage.getTotalElements());
        model.addAttribute("hotelList", hotelList);
        model.addAttribute("costs", roomsCostByHotelId);
        model.addAttribute("datePeriod", bookingPeriod == null ? new BookingPeriod() : bookingPeriod);
        operation = false;
        return "home";
    }

    @PostMapping("/search")
    public String search(@RequestParam("search") String search, HttpSession session) {
        String country = (String) session.getAttribute("country");
        List<Hotel> hotels = hotelService.searchByNameAndCountry(search, country);
        session.setAttribute("hotels", hotels);
        return "redirect:/home";
    }

    @PostMapping("/setCountry")
    public String setCountry(@RequestParam("country") String country, HttpSession session) {
        List<Hotel> hotels = hotelService.findHotelsByCountry(country);
        session.setAttribute("country", country);
        session.setAttribute("hotels", hotels);
        return "redirect:/home";
    }

    @PostMapping("/setDate")
    public String setDatePeriod(@ModelAttribute("datePeriod") BookingPeriod date, HttpSession session, Model model) {
        List<Hotel> hotels = (List<Hotel>) session.getAttribute("hotels");
        if (hotels == null || hotels.isEmpty()) {
            String country = (String) session.getAttribute("country");
            hotels = hotelService.findHotelsByCountry(country);
        }
        List<Room> rooms = roomService.findRoomsByHotelId(hotels);
        rooms = roomService.findRoomsByDatePeriod(mapper.map(date, BookingPeriod.class), rooms);
        session.setAttribute("hotels", hotelService.findHotelsBySort(rooms));
        session.setAttribute("datePeriod", mapper.map(date, BookingPeriod.class));
        operation = true;
        return "redirect:/home";
    }

    @PostMapping("/filter")
    public String filtering(@ModelAttribute("filterHotels") FilterHotelDto filterHotelDto, BindingResult bindingResult,
                            Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("filterHotels", filterHotelDto);
            return "home";
        }
        String country = (String) session.getAttribute("country");
        List<Hotel> hotels = findHotelsBySortAndCountry(filterHotelDto, country);
        session.setAttribute("hotels", hotels);
        operation = true;
        return "redirect:/home";
    }


    private List<Hotel> findHotelsBySortAndCountry(FilterHotelDto filterHotelDto, String country) {
        List<Hotel> hotelsByCountry = hotelService.findHotelsByCountry(country);
        List<Room> roomsByHotelId = roomService.findRoomsByHotelId(hotelsByCountry);
        List<Room> roomsBySort = roomService.findRoomsBySort(roomsByHotelId, mapper.map(filterHotelDto, Room.class));
        List<Hotel> sortedHotelsByRooms = hotelService.findHotelsBySort(roomsBySort);
        return hotelService.sortedHotelsByRating(sortedHotelsByRooms, filterHotelDto.getRating());
    }
}
