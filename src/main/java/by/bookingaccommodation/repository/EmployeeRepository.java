package by.bookingaccommodation.repository;

import by.bookingaccommodation.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
