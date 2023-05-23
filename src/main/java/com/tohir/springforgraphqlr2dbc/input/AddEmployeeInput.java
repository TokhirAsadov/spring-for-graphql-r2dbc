package com.tohir.springforgraphqlr2dbc.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddEmployeeInput {
    private String name,salary;
    private Integer departmentId;
}
