package by.bookingaccommodation.service;

import by.bookingaccommodation.entity.hotel.Booking;
import by.bookingaccommodation.entity.hotel.BookingPeriod;
import by.bookingaccommodation.repository.BookingPeriodRepository;
import by.bookingaccommodation.repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking save(Booking booking) {
        log.info(String.format("Booking {} save", booking));
        return bookingRepository.save(booking);
    }

    public boolean deleteByNumber(long bookingNumber) {
        log.info(String.format("Request delete booking by number {}", bookingNumber));
        if (bookingRepository.findByNumber(bookingNumber).isPresent()) {
            bookingRepository.deleteById(bookingNumber);
            return true;
        } else {
            log.warn(String.format("Booking with number %s is not exist", bookingNumber));
            return false;
        }
    }

    public long findLastBookingNumber() {
        log.info(String.format("Request top number exist"));
        Optional<Long> lastBookingNumber = bookingRepository.findTopByOrderByIdDesc();
        return lastBookingNumber != null ? lastBookingNumber.get() + 1 : 1;
    }
}
