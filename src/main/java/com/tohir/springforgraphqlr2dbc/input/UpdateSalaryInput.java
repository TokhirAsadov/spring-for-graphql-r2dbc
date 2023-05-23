package com.tohir.springforgraphqlr2dbc.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSalaryInput {
    private Integer employeeId;
    private String salary;
}
