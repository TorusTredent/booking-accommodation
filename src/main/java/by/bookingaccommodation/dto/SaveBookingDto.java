package by.bookingaccommodation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SaveBookingDto {

    private long offerId;
    private long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
