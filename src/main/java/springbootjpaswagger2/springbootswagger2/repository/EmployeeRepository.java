package springbootjpaswagger2.springbootswagger2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springbootjpaswagger2.springbootswagger2.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
