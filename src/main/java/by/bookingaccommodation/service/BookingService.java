package by.bookingaccommodation.service;

import by.bookingaccommodation.entity.hotel.Booking;
import by.bookingaccommodation.repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking reserve(Booking booking) {
        log.info(String.format("Booking {} save", booking));
        return bookingRepository.save(booking).orElse(null);
    }
}
