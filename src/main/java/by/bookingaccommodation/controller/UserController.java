package by.bookingaccommodation.controller;

import by.bookingaccommodation.dto.UpdateUserDto;
import by.bookingaccommodation.dto.UserDto;
import by.bookingaccommodation.entity.User;
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
                session.setAttribute("user", user);
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
    public String update(@RequestBody UpdateUserDto userDto, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "/user/update";
        }
        User sessionUser = (User) session.getAttribute("user");
        User update = userService.update(sessionUser.getEmail(), mapper.map(userDto, User.class));
        session.setAttribute("user", update);
        return "/user/update";
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
