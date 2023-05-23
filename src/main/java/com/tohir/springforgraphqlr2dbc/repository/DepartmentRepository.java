package com.tohir.springforgraphqlr2dbc.repository;

import com.tohir.springforgraphqlr2dbc.entity.Department;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface DepartmentRepository extends ReactiveCrudRepository<Department,Integer> {
}
