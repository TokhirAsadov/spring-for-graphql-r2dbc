package com.tohir.springforgraphqlr2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    private Integer id;
    private String name,salary;
    @Column("department_id")
    private Integer departmentId;
}
