package com.sndbx.task;

import lombok.Data;


@Data
public class InstantTaskDTO {
    private Language language;
    private String validationTestCode;
    private String validSolutionCode;
    private String boilerplateCode;
}
