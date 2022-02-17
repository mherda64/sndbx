package com.sndbx.task;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Data
public class Task {

    @Id
    private Long id;

    private String name;

    private Language language;

    @Lob
    private String description;

    @Lob
    private String validationTestCode;

    @Lob
    private String exampleTestCode;

    @Lob
    private String validSolutionCode;

    @Lob
    private String initialSolution;

    @Lob
    private String boilerplateCode;

}
