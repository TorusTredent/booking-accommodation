package by.bookingaccommodation.controller;

import by.bookingaccommodation.dto.DatePeriodDto;
import by.bookingaccommodation.dto.hotel.UpdateHotelDto;
import by.bookingaccommodation.entity.User;
import by.bookingaccommodation.entity.hotel.BookingPeriod;
import by.bookingaccommodation.entity.hotel.Hotel;
import by.bookingaccommodation.entity.hotel.Favor;
import by.bookingaccommodation.entity.hotel.Room;
import by.bookingaccommodation.service.FavorService;
import by.bookingaccommodation.service.HotelService;
import by.bookingaccommodation.service.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private FavorService favorService;

    @Autowired
    private RoomService roomService;

    private boolean operation;

    @GetMapping("/{hotelId}")
    public String personal(@PathVariable long hotelId, Model model, HttpSession session) {
        Hotel hotel = hotelService.findHotelById(hotelId);
        if (hotel == null) {
            return "redirect:/";
        } else {
            BookingPeriod period = (BookingPeriod) session.getAttribute("period");
            List<Room> rooms = (List<Room>) session.getAttribute("rooms");
            List<Favor> favors = favorService.findFavorsByHotelId(hotelId);
            model.addAttribute("rooms", rooms);
            model.addAttribute("datePeriod", period == null ? new BookingPeriod() : period);
            model.addAttribute("favors", favors);
            model.addAttribute("hotel", hotel);
            return "hotel/personal";
        }
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        Hotel hotel = (Hotel) session.getAttribute("hotel");
        model.addAttribute("hotel", hotel);
        model.addAttribute("user", sessionUser);
        return "hotel/profile";
    }

    @PostMapping("/{hotelId}/setDate")
    public String setDatePeriod(@PathVariable long hotelId,
                                @ModelAttribute("datePeriod") BookingPeriod date, HttpSession session, Model model) {
        List<Room> rooms = roomService.findRoomsByHotelId(hotelId);
        rooms = roomService.findRoomsByDatePeriod(mapper.map(date, BookingPeriod.class), rooms);
        session.setAttribute("rooms", rooms);
        session.setAttribute("datePeriod", mapper.map(date, BookingPeriod.class));
        operation = true;
        return "redirect:/hotel/" + hotelId;
    }

    @PutMapping
    public String update(@RequestBody UpdateHotelDto hotelDto, BindingResult bindingResult, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            return "hotel/profile";
        }
        Hotel hotel = (Hotel) session.getAttribute("hotel");
        Hotel update = hotelService.update(hotel, mapper.map(hotelDto, Hotel.class));
        session.setAttribute("hotel", update);
        model.addAttribute("acceptMessage", true);
        return "hotel/profile";
    }

    @DeleteMapping
    public String delete(HttpSession session) {
        Hotel hotel = (Hotel) session.getAttribute("hotel");
        hotelService.delete(hotel);
        session.invalidate();
        return "redirect: /";
    }
}
