package com.employeedirectory.Employee.Service;

import com.employeedirectory.Employee.Definition.Employee;
import com.employeedirectory.Employee.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository)
    {
        this.employeeRepository = employeeRepository;
    }

    public void addNewEntry(Employee employee) {
        Optional<Employee> entryByEmail = employeeRepository.findEmployeeByEmail(employee.getEmail());
        Optional<Employee> entryByName = employeeRepository.findEmployeeByName(employee.getName());

        System.out.println("*************Inside addEntry Method**********");

            if(entryByEmail.isPresent()){
            System.out.println("Email Id is already present. Use Another Email or go to Login.");
            throw new IllegalStateException("Email Already present");
            }

            else if (entryByName.isPresent()){
            System.out.println("Name provided is already present and cannot be reused. Go to Login or use different Name.");
            throw new IllegalStateException("Name Already present");
            }

            else{
            employeeRepository.save(employee);
            System.out.println("Saving Employee Details to Database Successful!");
            }

        System.out.println("**********Exiting AddEntry Method***********");
    }

    public List<Employee> getEmployees(){
        System.out.println("*************Inside getEmployees Method**********");
            List<Employee> employeeList = employeeRepository.findAll();
            System.out.println("Successfully fetched the list : " + employeeList);
            System.out.println("*************Exiting getEmployees Method**********");
        return employeeList;
    }

    public String deleteEntry(Long employeeId) {
    System.out.println("************* Inside deleteEntry Method **********");
    boolean exists = employeeRepository.existsById(employeeId);
        if (!exists){
        System.out.println("Employee cannot be found for the given ID");
        System.out.println("************* Exiting deleteEntry Method **********");
            return "404 NOT FOUND";
        }
        else {
        System.out.println("Deleting Employee Details for the ID : " + employeeId);
        employeeRepository.deleteById(employeeId);
        System.out.println("************* Exiting deleteEntry Method **********");
        return "200 DATA DELETED";
        }

    }

    @Transactional
    public Employee updateEntry(Long employeeId, String name, String email, String dob) {
        Employee updatedEmployee = new Employee();

        System.out.println("************* Inside updateEntry Method **********");
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()-> new IllegalStateException("Employee ID not found."));

            if (name != null && name.length()>0 && !employee.getName().equals(name)) {
                employee.setName(name);
                updatedEmployee.setName(name);
                System.out.println("Name updated from " + employee.getName() + " to " + name);
            }

            LocalDate dateofBirth = LocalDate.parse(dob);
            if (dateofBirth != null && !employee.getDob().equals(dateofBirth)){
                employee.setDob(dateofBirth);
                updatedEmployee.setDob(dateofBirth);
                System.out.println("Age updated from " + employee.getDob() + " to " + dateofBirth);
            }

            if (email != null && email.length()>0 && !employee.getEmail().equals(email)){
                Optional<Employee> optionalEmployeeEmail = employeeRepository.findEmployeeByEmail(email);
                    if (optionalEmployeeEmail.isPresent()){
                        throw new IllegalStateException("Email already present.");
                    }
                employee.setEmail(email);
                updatedEmployee.setEmail(email);
                System.out.println("Email updated from " + employee.getEmail() + " to " + email);
        }
        System.out.println("************* Exiting updateEntry Method **********");
            return updatedEmployee;
    }

    public Employee getEmployeeById(Long employeeId) {
        System.out.println("************* Inside getEmployeesById Method **********");
        try {
            Employee employeeById = employeeRepository.findEmployeeById(employeeId);
            if (employeeById.getId() == 0) {
                System.out.println("Could not find Employee by the given Id");
                throw new NullPointerException("Couldn't find the provided ID");
            }
            else{
                System.out.println("Found following Details for Employee by Id " + employeeId + " : " + employeeById);
                System.out.println("************* Exiting getEmployeesById Method **********");
                return employeeById;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
