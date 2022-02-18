package by.bookingaccommodation.service;

import by.bookingaccommodation.entity.Employee;
import by.bookingaccommodation.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee findByEmployeeId(long employeeId) {
        log.info(String.format("Request employeeId {} exist", employeeId));
        return employeeRepository.findById(employeeId).orElse(null);
    }

}
