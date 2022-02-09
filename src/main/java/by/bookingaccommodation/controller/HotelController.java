package by.bookingaccommodation.controller;

import by.bookingaccommodation.dto.hotel.UpdateHotelDto;
import by.bookingaccommodation.entity.User;
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

    @GetMapping("/{hotelId}")
    public String personal(@PathVariable long hotelId, BindingResult bindingResult, Model model, HttpSession session) {
        if(bindingResult.hasErrors()) {
            return "redirect: /";
        }
        Hotel hotel = hotelService.findHotelById(hotelId);
        if (hotel == null) {
            return "redirect: /";
        } else {
            List<Favor> favors = favorService.findFavorsByHotelId(hotelId);
            List<Room> rooms = roomService.findRoomsByHotelId(hotelId);
            model.addAttribute("favors", favors);
            model.addAttribute("rooms", rooms);
            model.addAttribute("hotel", hotel);
            return "/hotel/personal";
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

    @PutMapping
    public String update(@RequestBody UpdateHotelDto hotelDto, BindingResult bindingResult, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            return "/hotel/profile";
        }
        Hotel hotel = (Hotel) session.getAttribute("hotel");
        Hotel update = hotelService.update(hotel, mapper.map(hotelDto, Hotel.class));
        session.setAttribute("hotel", update);
        model.addAttribute("acceptMessage", true);
        return "/hotel/profile";
    }

    @DeleteMapping
    public String delete(HttpSession session) {
        Hotel hotel = (Hotel) session.getAttribute("hotel");
        hotelService.delete(hotel);
        session.invalidate();
        return "redirect: /";
    }
}
