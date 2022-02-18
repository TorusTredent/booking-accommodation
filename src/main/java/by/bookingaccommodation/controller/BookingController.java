package by.bookingaccommodation.controller;

import by.bookingaccommodation.entity.User;
import by.bookingaccommodation.entity.hotel.Booking;
import by.bookingaccommodation.entity.hotel.BookingPeriod;
import by.bookingaccommodation.entity.hotel.Hotel;
import by.bookingaccommodation.entity.hotel.Room;
import by.bookingaccommodation.service.BookingService;
import by.bookingaccommodation.service.HotelService;
import by.bookingaccommodation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;


    @GetMapping("/{hotelId}/{roomId}")
    public String reserve(@PathVariable("hotelId") long hotelId, @PathVariable("roomId") long roomId,
                          HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        BookingPeriod period = (BookingPeriod) session.getAttribute("datePeriod");
        Hotel hotel = hotelService.findHotelById(hotelId);
        Room room = roomService.findRoomById(roomId);
        model.addAttribute("datePeriod", period);
        model.addAttribute("room", room);
        model.addAttribute("hotel", hotel);
        model.addAttribute("user", user);
        return "booking/reserve";
    }

    @PostMapping("/reserve")
    public String reserve(@RequestBody Booking booking, Model model, HttpSession session) {
        BookingPeriod datePeriod = (BookingPeriod) session.getAttribute("datePeriod");
        Room room = roomService.findRoomById(booking.getRoomId());
        room.getPeriods().add(datePeriod);
        roomService.save(room);
        bookingService.save(booking);
        return "redirect:/home";
    }

    @DeleteMapping("/{bookingNumber}")
    public String delete(@PathVariable long bookingNumber, Model model) {
        boolean delete = bookingService.deleteByNumber(bookingNumber);
        if (!delete) {
            model.addAttribute("errorMessage", true);
        }
        return "redirect:/";

    }
}
