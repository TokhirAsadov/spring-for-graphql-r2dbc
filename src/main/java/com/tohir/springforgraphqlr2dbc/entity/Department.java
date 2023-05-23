package com.tohir.springforgraphqlr2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    @Id
    private Integer id;
    private String name;
    private List<Employee> employees = new ArrayList<>();
}
