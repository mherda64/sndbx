package com.sndbx.task;

public class TaskMapper {

    public static Task toEntity(InstantTaskDTO instantTaskDTO) {
        var task = new Task();
        task.setLanguage(instantTaskDTO.getLanguage());
        task.setValidationTestCode(instantTaskDTO.getValidationTestCode());
        task.setValidSolutionCode(instantTaskDTO.getValidSolutionCode());
        task.setBoilerplateCode(instantTaskDTO.getBoilerplateCode());
        return task;
    }

}
