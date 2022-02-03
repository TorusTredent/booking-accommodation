package by.bookingaccommodation.service;

import by.bookingaccommodation.entity.User;
import by.bookingaccommodation.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email) {
        log.info(String.format("Request email {} exist", email));
        return userRepository.findByEmail(email).orElse(null);
    }

    public User save (User user) {
        log.info(String.format("User {} save", user.getEmail()));
        return userRepository.save(user);
    }

    public User update(User user, User updateUser) {
        log.info("Request update {}", user.getEmail());
        updateUser.setId(user.getId());
        updateUser.setUserRole(user.getUserRole());
        updateUser.setUserStatus(user.getUserStatus());
        return userRepository.save(updateUser);
    }

    public void delete(User user) {
        log.info("Request delete {}", user.getEmail());
        userRepository.delete(user);
    }
}
