package com.employeedirectory.Employee.Controller;

import com.employeedirectory.Employee.Definition.Employee;
import com.employeedirectory.Employee.Service.EmployeeService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.List;


@RestController
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "User Controller", description = "Rest Api related to User Controller")
public class UserController {

    private final EmployeeService employeeService;

    @Autowired
    public UserController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public String BasePage(){
        return "Welcome to the Base Home Page of the Application";
    }

    @ApiOperation(value = "Get All Employees",
            notes = "This method gets all Employees")
    @GetMapping(path = "/api/v2/getAllEmployees")
    public List<Employee> getEmployees()
    {
        try {
            List<Employee> employeeList;
            employeeList = employeeService.getEmployees();
            if(employeeList != null){
                return employeeList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    return null;
    }

    @ApiOperation(value = "Fetch Employee By ID",
            notes = "This method gets a Employee by ID")
    @GetMapping(path = "api/v1/getOneEmployee/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@ApiParam(name ="EmployeeId", value = "ID of Employee", example = "1", required = true)
            @PathVariable ("employeeId") Long employeeId){
          try{
              Employee employeeById;
              employeeById = employeeService.getEmployeeById(employeeId);
              return ResponseEntity.ok(employeeById);
          }
          catch (Exception e) {
              e.printStackTrace();
          }
           return null;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not found - The product was not found")
    })
    @ApiOperation(value = "Add New Employee",
            notes = "This method Adds New Employee Records")
    @PostMapping(path = "api/v2/addNewEmployee/register")
    public ResponseEntity<Employee> register(@RequestBody Employee employee){
       try{
           employeeService.addNewEntry(employee);
           return ResponseEntity.ok(employee);
       }
       catch (Exception e){
           e.printStackTrace();
       }
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(employee);
    }

    @ApiOperation(value = "Update Employee Details By ID",
            notes = "This method Updates Employee Records by ID")
    @PutMapping(path = "api/v1/updateEmployee/{EmployeeId}")
    public ResponseEntity<String> updateEmployee(@PathVariable ("EmployeeId") Long employeeId,
                                                 @RequestParam(required = false) String name,
                                                 @RequestParam(required = false) String email,
                                                 @RequestParam(required = false) LocalDate dob){
        try{
            employeeService.updateEntry(employeeId,name,email,dob);
            return ResponseEntity.ok("Updated!");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating the records!");
    }

    @ApiOperation(value = "Delete Employee Details By ID",
            notes = "This method deletes Employee Records by ID")
    @DeleteMapping(path = "api/v2/deleteEmployee/{EmployeeId}")
    public void removeEntry(@PathVariable ("EmployeeId") Long EmployeeId) {
        try {
            employeeService.deleteEntry(EmployeeId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
