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
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String country;
    private String city;
    private String street;
    private String home;
    private String phoneNumber;
    private double rating;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> imageUrls;

    public String getFirstPhoto() {
        return imageUrls.get(0);
    }

    public Hotel(String name, String country, String city, String street, String home, String phoneNumber, double rating, List<String> imageUrls) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.street = street;
        this.home = home;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.imageUrls = imageUrls;
    }
}
