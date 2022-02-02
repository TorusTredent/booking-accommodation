package by.bookingaccommodation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateUserDto {

    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String dateOfBirth;
    private String nationality;
    private String address;
}
