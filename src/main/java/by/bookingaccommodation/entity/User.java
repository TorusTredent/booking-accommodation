package by.bookingaccommodation.entity;

import by.bookingaccommodation.entity.roles.UserRole;
import by.bookingaccommodation.entity.statuses.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String dateOfBirth;
    private String nationality;
    private String address;
    private String imageUrl;
    private long employeeId;

    @Enumerated(value = EnumType.STRING)
    private UserRole userRole = UserRole.USER;

    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus = UserStatus.ACTIVE;
}
