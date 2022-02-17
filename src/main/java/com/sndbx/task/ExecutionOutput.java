package com.sndbx.task;

import lombok.Data;

@Data
public class ExecutionOutput {

    private Long executionTime;
    private String stdout;
    private Boolean timedOut;
    private Status status;

}
