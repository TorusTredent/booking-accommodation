package by.bookingaccommodation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateHotelDto {

    private String name;
    private String country;
    private String city;
    private String street;
    private String home;
    private String phoneNumber;

    private List<String> imageUrls;
}
