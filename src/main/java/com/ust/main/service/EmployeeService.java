package com.ust.main.service;

import com.ust.main.model.Employee;
import com.ust.main.model.Leaves;
import com.ust.main.repository.EmployeeRepository;
import com.ust.main.response.EmployeeResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Value("${leaves.privilege}")
    private int privilegeLeaves;

    @Value("${leaves.sick.leave}")
    private int sickLeaves;

    @Value("${leaves.bereavment}")
    private int bereavmentLeaves;


    public boolean employeeExistsById(long employeeId) {
        if (employeeRepository.existsById(employeeId))
            return true;
        else
            return false;
    }

    public boolean employeeExistsByEmailId(String emailId) {
        if (employeeRepository.existsByEmailId(emailId)) {
            return true;
        } else
            return false;
    }

    public boolean isListEmpty() {
        if (employeeRepository.findAll().isEmpty()) {
            return true;
        } else
            return false;
    }

    public ResponseEntity<EmployeeResponse> getAllEmployees() {
        EmployeeResponse getAllEmployeeResponse = new EmployeeResponse();

        if (isListEmpty()) {
//            getAllEmployeeResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
            getAllEmployeeResponse.setResponseMessage("Employee List is Empty!");
            return new ResponseEntity<>(getAllEmployeeResponse, HttpStatus.BAD_REQUEST);
        } else {
            getAllEmployeeResponse.setEmployeeResponseList(employeeRepository.findAll());
//            getAllEmployeeResponse.setHttpStatus(HttpStatus.OK);
            getAllEmployeeResponse.setResponseMessage("Employees Found!");
            return new ResponseEntity<>(getAllEmployeeResponse, HttpStatus.OK);
        }
    }

    public ResponseEntity<EmployeeResponse> getActiveEmployees() {
        EmployeeResponse getActiveEmployeeResponse = new EmployeeResponse();

        if (isListEmpty()) {
//            getActiveEmployeeResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
            getActiveEmployeeResponse.setResponseMessage("Employee List is Empty!");
            return new ResponseEntity<>(getActiveEmployeeResponse, HttpStatus.BAD_REQUEST);
        } else {
            List<Employee> employeeList = employeeRepository.findAll().stream().filter(Employee::isCurrentEmployee).collect(Collectors.toList());
            if (employeeList.isEmpty()) {
                getActiveEmployeeResponse.setResponseMessage("There are no active Employees!");
                return new ResponseEntity<>(getActiveEmployeeResponse, HttpStatus.BAD_REQUEST);
            } else {
                getActiveEmployeeResponse.setEmployeeResponseList(employeeList);
                getActiveEmployeeResponse.setResponseMessage("List of Active Employees");
                return new ResponseEntity<>(getActiveEmployeeResponse, HttpStatus.OK);
            }
        }
    }

    public ResponseEntity<EmployeeResponse> getEmployeeById(long employeeId) {
        EmployeeResponse getByIdEmployeeResponse = new EmployeeResponse();
        if (employeeExistsById(employeeId)) {
            getByIdEmployeeResponse.setResponseMessage("Employee Details Found!!");
            getByIdEmployeeResponse.setEmployeeResponse(employeeRepository.findById(employeeId).get());
//            getByIdEmployeeResponse.setHttpStatus(HttpStatus.OK);
            return new ResponseEntity<>(getByIdEmployeeResponse, HttpStatus.OK);
        } else {
            getByIdEmployeeResponse.setResponseMessage("Employee does not exist");
            getByIdEmployeeResponse.setEmployeeResponse(null);
//            getByIdEmployeeResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(getByIdEmployeeResponse, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<EmployeeResponse> saveEmployee(Employee employee) {
        EmployeeResponse saveEmployeeResponse = new EmployeeResponse();

        System.out.println("Exists By Email Id: " + employeeExistsByEmailId(employee.getEmailId()));

        if (employeeExistsByEmailId(employee.getEmailId())) {
//          saveEmployeeResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
            saveEmployeeResponse.setResponseMessage("Employee Already Exists!");
            return new ResponseEntity<>(saveEmployeeResponse, HttpStatus.CONFLICT);
        } else {
            employee.setCurrentEmployee(true);
            employee.setLastWorkingDay(null);
            Leaves leaves = new Leaves();
            leaves.setBereavementLeaves(this.bereavmentLeaves);
            leaves.setSickLeaves(this.sickLeaves);
            leaves.setEarnedLeaves(this.privilegeLeaves);
            employee.setLeaves(leaves);
            employeeRepository.save(employee);
            saveEmployeeResponse.setResponseMessage("Employee Details Saved Successfully.");
            saveEmployeeResponse.setEmployeeResponse(employeeRepository.findByEmailId(employee.getEmailId()));
            return new ResponseEntity<>(saveEmployeeResponse, HttpStatus.OK);
        }
    }

    public ResponseEntity<String> deleteEmployee(long employeeId, LocalDate LWD) {

        if (employeeRepository.existsById(employeeId) && employeeRepository.findById(employeeId).get().isCurrentEmployee()) {

            Employee tempEmployee = employeeRepository.findById(employeeId).get();
            tempEmployee.setCurrentEmployee(false);
            tempEmployee.setLastWorkingDay(LWD);
            tempEmployee.setEmployeeId(employeeId);
            tempEmployee.setDateOfBirth(tempEmployee.getDateOfBirth());
            tempEmployee.setCurrentEmployee(tempEmployee.isCurrentEmployee());
            tempEmployee.setDateOfJoining(tempEmployee.getDateOfJoining());
            tempEmployee.setEmailId(tempEmployee.getEmailId());
            tempEmployee.setFirstName(tempEmployee.getFirstName());
            tempEmployee.setLastName(tempEmployee.getLastName());

            employeeRepository.save(tempEmployee);
            return ResponseEntity.status(HttpStatus.OK).body("Employee Deleted Successfully");

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee does not exist.");
        }

    }

    public ResponseEntity<Object> updateEmployee(Employee updateEmployee, long id) {

        if (employeeRepository.existsById(id)) {
            Employee tempEmployee = employeeRepository.findById(id).get();
            tempEmployee.setDateOfBirth(updateEmployee.getDateOfBirth());
            tempEmployee.setCurrentEmployee(updateEmployee.isCurrentEmployee());
            tempEmployee.setDateOfJoining(updateEmployee.getDateOfJoining());
            tempEmployee.setEmailId(updateEmployee.getEmailId());
            tempEmployee.setFirstName(updateEmployee.getFirstName());
            tempEmployee.setLastName(updateEmployee.getLastName());
            tempEmployee.setEmployeeId(id);
            employeeRepository.save(tempEmployee);


            return ResponseEntity.status(HttpStatus.OK).body(employeeRepository.findById(id).get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Employee does not exist.");
        }
    }

}
