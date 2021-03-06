package by.bookingaccommodation.entity.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int floor;
    private String type;
    private double cost;
    private int numberOfBeds;
    private boolean conditioner;
    private boolean TV;
    private boolean internet;
    private boolean miniBar;
    private long hotelId;

    @ManyToMany()
    private List<BookingPeriod> periods;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> imageUrls;

    public Room(long hotelId, double cost) {
        this.hotelId = hotelId;
        this.cost = cost;
    }
}
