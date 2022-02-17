package com.sndbx.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("execute")
    public ExecutionOutput executeInstant(@RequestBody InstantTaskDTO task) {
        return taskService.execute(TaskMapper.toEntity(task));
    }

}
