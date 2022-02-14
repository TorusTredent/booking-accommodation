package by.bookingaccommodation.controller;

import by.bookingaccommodation.dto.hotel.FilterHotelDto;
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
@RequestMapping("/hotel")
public class HomeHotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ModelMapper mapper;

    private static final int HOTELS_PER_PAGE = 5;
    private boolean operation;

    @GetMapping(value = {"/home/{page}", "/home"})
    public String home(@PathVariable Optional<Integer> page,
                       Model model, HttpSession session) {
        int currentPage = page.orElse(1);
        String country = (String) session.getAttribute("country");
        List<Hotel> hotels = (List<Hotel>) session.getAttribute("hotels");
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
        operation = false;
        return "homeHotel";
    }

    @PostMapping("/search")
    public String search(@RequestParam("search") String search, HttpSession session) {
        String country = (String) session.getAttribute("country");
        List<Hotel> hotels = hotelService.searchByNameAndCountry(search, country);
        session.setAttribute("hotels", hotels);
        return "redirect: /hotel/home";
    }

    @PostMapping("/filter")
    public String filtering(@ModelAttribute("filterHotels") FilterHotelDto filterHotelDto, BindingResult bindingResult,
                          Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("filterHotels", filterHotelDto);
            return "/homeHotel";
        }
        String country = (String) session.getAttribute("country");
        List<Hotel> hotels = findHotelsBySortAndCountry(filterHotelDto, country);
        session.setAttribute("hotels", hotels);
        operation = true;
        return "redirect:/hotel/home";
    }

    private List<Hotel> findHotelsBySortAndCountry(FilterHotelDto filterHotelDto, String country) {
        List<Hotel> hotelsByCountry = hotelService.findHotelsByCountry(country);
        List<Room> roomsByHotelId = roomService.findRoomsByHotelId(hotelsByCountry);
        List<Room> roomsBySort = roomService.findRoomsBySort(roomsByHotelId, mapper.map(filterHotelDto, Room.class));
        List<Hotel> sortedHotelsByRooms = hotelService.findHotelsBySort(roomsBySort);
        return hotelService.sortedHotelsByRating(sortedHotelsByRooms, filterHotelDto.getRating());
    }


}
