package by.bookingaccommodation.entity;


import by.bookingaccommodation.entity.roles.HotelRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private long hotelId;

    @OneToOne
    private User user;

    @Enumerated(value = EnumType.STRING)
    private HotelRole hotelRole = HotelRole.EMPLOYEE;
}
