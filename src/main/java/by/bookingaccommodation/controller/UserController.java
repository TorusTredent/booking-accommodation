package by.bookingaccommodation.controller;

import by.bookingaccommodation.dto.user.AuthUserDto;
import by.bookingaccommodation.dto.user.RegUserDto;
import by.bookingaccommodation.dto.user.UpdateUserDto;
import by.bookingaccommodation.entity.Employee;
import by.bookingaccommodation.entity.User;
import by.bookingaccommodation.entity.hotel.Hotel;
import by.bookingaccommodation.service.EmployeeService;
import by.bookingaccommodation.service.HotelService;
import by.bookingaccommodation.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private HotelService hotelService;

    @GetMapping("/authorization")
    public String auth(Model model) {
        model.addAttribute("authUser", new AuthUserDto());
        return "user/authorization";
    }

    @PostMapping("/authorization")
    public String auth(@Valid @ModelAttribute("authUser") AuthUserDto userDto, BindingResult bindingResult,
                       Model model, HttpSession session) {
        try {
            if (!bindingResult.hasErrors()) {
                User user = userService.findByEmail(mapper.map(userDto, User.class).getEmail());
                if (user == null) {
                    model.addAttribute("userMessage", true);
                } else {
                    if (user.getPassword().equals(userDto.getPassword())) {
                        if (user.getEmployeeId() == 0) {
                            session.setAttribute("user", user);
                        } else {
                            Employee employee = employeeService.findByEmployeeId(user.getEmployeeId());
                            Hotel hotel = hotelService.findHotelById(employee.getId());
                            session.setAttribute("hotel", hotel);
                            session.setAttribute("employee", employee);
                            session.setAttribute("employeeUser", user);
                        }
                        return "redirect:/";
                    } else {
                        model.addAttribute("passwordMessage", true);
                    }
                }
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "/user/authorization";
    }

    @GetMapping("/registration")
    public String reg(Model model) {
        model.addAttribute("regUser", new RegUserDto());
        return "user/registration";
    }

    @PostMapping("/registration")
    public String reg(@ModelAttribute("regUser") RegUserDto userDto, BindingResult bindingResult,
                      Model model, HttpSession session) {
        try {
            if (!bindingResult.hasErrors()) {

                User user = userService.findByEmail(mapper.map(userDto, User.class).getEmail());
                if (user == null) {
                    User save = userService.save(mapper.map(userDto, User.class));
                    session.setAttribute("user", save);
                    return "redirect:/";
                } else {
                    model.addAttribute("userMessage", true);
                }
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "/user/authorization";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        model.addAttribute("user", mapper.map(sessionUser, UpdateUserDto.class));
        return "user/profile";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") UpdateUserDto userDto, BindingResult bindingResult, HttpSession session, Model model) {
        try {
            if (!bindingResult.hasErrors()) {
                User sessionUser = (User) session.getAttribute("user");
                User update = userService.update(sessionUser, mapper.map(userDto, User.class));
                session.setAttribute("user", update);
                model.addAttribute("completeMessage", true);
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "/user/profile";
    }

    @GetMapping("/order/{hotelId}/{roomId}")
    public String order(@PathVariable("hotelId") long hotelId, @PathVariable("roomId") long roomId, Model model, HttpSession session) {
        return "/user/order";
    }

    @DeleteMapping()
    public String delete(HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        userService.delete(sessionUser);
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
