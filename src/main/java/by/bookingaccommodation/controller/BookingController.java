package by.bookingaccommodation.controller;

import by.bookingaccommodation.dto.SaveBookingDto;
import by.bookingaccommodation.entity.User;
import by.bookingaccommodation.entity.hotel.Booking;
import by.bookingaccommodation.service.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/reserve")
    public String reserve(@RequestBody SaveBookingDto bookingDto,
                              BindingResult bindingResult, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect: /hotel/personal";
        }
        User user = (User) session.getAttribute("user");
        Booking booking = mapper.map(bookingDto, Booking.class);
        booking.setUserId(user.getId());
        model.addAttribute("booking", booking);
        model.addAttribute("user", user);
        return "/booking/reserve";
    }

    @PostMapping("/reserve")
    public String reserve(@RequestBody Booking booking, BindingResult bindingResult,
                          Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "/booking/reserve";
        }
        Booking save = bookingService.reserve(booking);
        if (save == null) {
            model.addAttribute("errorMessage", true);
            return "/booking/reserve";
        } else {
            model.addAttribute("booking", save);
            return "redirect: /";
            //////////доделать редирект добавления в корзину
        }
    }

    @DeleteMapping("/{bookingNumber}")
    public String delete(@PathVariable long bookingNumber, Model model) {
        boolean delete = bookingService.deleteByNumber(bookingNumber);
        if (!delete) {
            model.addAttribute("errorMessage", true);
        }
        return "redirect: /";

    }
}
