package by.bookingaccommodation.controller;

import by.bookingaccommodation.dto.user.UpdateUserDto;
import by.bookingaccommodation.dto.user.UserDto;
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
        model.addAttribute("authUser", new UserDto());
        return "user/authorization";
    }

    @PostMapping("/authorization")
    public String auth(@ModelAttribute("authUser") UserDto userDto, BindingResult bindingResult,
                       Model model, HttpSession session){
        if(bindingResult.hasErrors()) {
            return "user/authorization";
        }
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
                return "redirect: /";
            } else {
                model.addAttribute("passwordMessage", true);
            }
        }
        return "user/authorization";
    }

    @GetMapping("/reg")
    public String reg(Model model) {
        model.addAttribute("regUser", new UserDto());
        return "user/registration";
    }

    @PostMapping("/reg")
    public String reg(@ModelAttribute("regUser") UserDto userDto, BindingResult bindingResult,
                      Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "/user/registration";
        }
        User user = userService.findByEmail(mapper.map(userDto, User.class).getEmail());
        if (user == null) {
            User save = userService.save(mapper.map(userDto, User.class));
            session.setAttribute("user", save);
            return "redirect: /";
        } else {
            model.addAttribute("userMessage", true);
        }
        return "/user/registration";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        model.addAttribute("user", sessionUser);
        return "/user/profile";
    }

    @PutMapping("/update")
    public String update(@RequestBody UpdateUserDto userDto, BindingResult bindingResult, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            return "/user/profile";
        }
        User sessionUser = (User) session.getAttribute("user");
        User update = userService.update(sessionUser, mapper.map(userDto, User.class));
        session.setAttribute("user", update);
        model.addAttribute("acceptMessage", true);
        return "user/profile";
    }

    @DeleteMapping()
    public String delete(HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        userService.delete(sessionUser);
        session.invalidate();
        return "redirect: /";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect: /";
    }
}
