package springbootjpaswagger2.springbootswagger2.controller;

import java.util.Map;
import java.util.HashMap;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springbootjpaswagger2.springbootswagger2.model.Employee;
import springbootjpaswagger2.springbootswagger2.repository.EmployeeRepository;
import springbootjpaswagger2.springbootswagger2.exception.ResourceNotFoundException;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(value = "Employee Management System", description = "operations pertaining to employee management system")
@RequestMapping("/api/v1")
public class EmployeeController{

    @Autowired
    private EmployeeRepository employeeRepository;

    @ApiOperation(value = "view a list of available employees", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succesfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accesing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    @ApiOperation(value = "Get employee by id")
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @ApiParam(value = "Employee id from which employee object will retrieve", required = true) @PathVariable(value = "id")Long employeeId)
        throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
        return ResponseEntity.ok().body(employee);
    }

    @ApiOperation(value = "Add an employee")
    @PostMapping("/employees")
    public Employee createEmployee(
        @ApiParam(value = "Employee object store in database table", required = true) @Valid @RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

    @ApiOperation(value = "Update an employee")
    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @ApiParam(value = "Employee Id to update employee object", required = true)@PathVariable(value = "id")Long employeeId,
            @ApiParam(value = "update  employee object", required = true)@Valid @RequestBody Employee employeeDetails) throws  ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id ::" + employeeId));

        employee.setEmailId(employeeDetails.getEmailId());
        employee.setLastName(employeeDetails.getLastName());
        employee.setFirstName(employeeDetails.getFirstName());
        final Employee updatedEmployee = employeeRepository.save(employee);
                return ResponseEntity.ok(updatedEmployee);
    }

    @ApiOperation(value = "Delete an employee")
    @DeleteMapping("/employees/{id}")
    public Map<String,Boolean> deleteEmployee(
            @ApiParam(value =  "Employee id from which employee object will delete from database table", required = true)
            @PathVariable(value = "id") Long employeeId) throws ResourceNotFoundException{
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id:: "+employeeId));
        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}