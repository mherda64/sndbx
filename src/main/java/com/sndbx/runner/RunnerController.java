package com.sndbx.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RunnerController {

    @Autowired
    private RunnerService runnerService;

    @PostMapping("execute")
    public String executeProgram(@RequestBody ProgramDTO program) {

        return runnerService.execute(program);
    }

}
