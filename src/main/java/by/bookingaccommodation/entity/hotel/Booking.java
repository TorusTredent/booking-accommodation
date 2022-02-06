package by.bookingaccommodation.entity.hotel;

import by.bookingaccommodation.repository.BookingRepository;
import by.bookingaccommodation.service.BookingService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Booking {

    @Autowired
    private transient BookingService bookingService;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long number;
    private long userId;
    private long offerId;
    private long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    @PrePersist
    protected void onCreate() {
        this.number = bookingService.findLastBookingNumber();
    }
}
