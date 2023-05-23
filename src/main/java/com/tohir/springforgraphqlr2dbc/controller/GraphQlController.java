package com.tohir.springforgraphqlr2dbc.controller;

import com.tohir.springforgraphqlr2dbc.entity.Department;
import com.tohir.springforgraphqlr2dbc.entity.Employee;
import com.tohir.springforgraphqlr2dbc.input.AddEmployeeInput;
import com.tohir.springforgraphqlr2dbc.input.UpdateSalaryInput;
import com.tohir.springforgraphqlr2dbc.repository.DepartmentRepository;
import com.tohir.springforgraphqlr2dbc.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@RestController
//@Controller
//@RequiredArgsConstructor
public class GraphQlController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final HttpGraphQlClient httpGraphQlClient;

    public GraphQlController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, HttpGraphQlClient httpGraphQlClient) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.httpGraphQlClient = httpGraphQlClient;
    }

    Function<AddEmployeeInput,Employee> mapping = aei -> {
        var employee = new Employee();
        employee.setName(aei.getName());
        employee.setSalary(aei.getSalary());
        employee.setDepartmentId(aei.getDepartmentId());
        return employee;
    };

    @GetMapping("/client/employeeByName")
    public Mono<List<Employee>> employeeByName() {
        var document = "query {\n" +
                "  employeeByName(employeeName: \"tohir\"){\n" +
                "    id\n" +
                "    name\n" +
                "    salary\n" +
                "  }\n" +
                "}";
        return this.httpGraphQlClient.document(document)
                .retrieve("employeeByName")
                .toEntityList(Employee.class);
    }

//    @SchemaMapping(typeName = "Mutation",field = "addEmployee") // old version
    @MutationMapping
    public Mono<Employee> addEmployee(@Argument AddEmployeeInput addEmployeeInput){
        return this.employeeRepository.save(mapping.apply(addEmployeeInput));
    }


    @QueryMapping
    public Flux<Employee> employeeByName(@Argument String employeeName){
        return this.employeeRepository.getEmployeeByName(employeeName);
    }

    @MutationMapping
    public Mono<Employee> updateSalary(@Argument UpdateSalaryInput updateSalaryInput){
        return this.employeeRepository.findById(updateSalaryInput.getEmployeeId())
                .flatMap(employee -> {
                    employee.setSalary(updateSalaryInput.getSalary());
                    return this.employeeRepository.save(employee);
                });
    }

    @QueryMapping
    public Flux<Department> allDepartment(){
        return this.departmentRepository.findAll();
    }

//    @SchemaMapping(typeName = "Department", field = "employees") // department ning employees field iga employees larni connect qilyapmiz
//    public Flux<Employee> employees(Department department){
//        log.info("department id: {}",department.getId());
//        return this.employeeRepository.getAllEmployeeByDepartmentId(department.getId());
//    }

    @BatchMapping
    public Mono<Map<Department, Collection<Employee>>> employees(List<Department> departments){
        return Flux.fromIterable(departments)
                .flatMap(department -> this.employeeRepository.getAllEmployeeByDepartmentId(department.getId()))
                .collectMultimap(employee -> departments.stream().filter(department -> department.getId().equals(employee.getDepartmentId())).findFirst().get());
    }


    @SubscriptionMapping
    public Flux<Employee> allEmployee(){
        return this.employeeRepository.findAll().delayElements(Duration.ofSeconds(3));
    }

}
