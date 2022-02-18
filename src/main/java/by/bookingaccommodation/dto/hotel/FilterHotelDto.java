package by.bookingaccommodation.dto.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FilterHotelDto {

    private double cost;
    private double rating;
    private String type;
    private int numberOfBeds;
    private boolean conditioner;
    private boolean TV;
    private boolean internet;
    private boolean miniBar;
}
