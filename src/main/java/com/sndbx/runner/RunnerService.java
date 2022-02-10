package com.sndbx.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RunnerService {

    @Value("${application.workspacePath}")
    private String mainWorkspacePath;

    @Value("${application.dockerImage}")
    private String dockerImage;

    public String execute(ProgramDTO program) {
        var uuid = UUID.randomUUID().toString();
        var workspacePath = mainWorkspacePath + uuid + "/";
        initializeWorkspace(program, workspacePath);

        var command = mainWorkspacePath + "runner.sh 5s -dit -v " + workspacePath + ":/workspace/usercode/ java-runner:1 sh script.sh " + program.getFileName().split("\\.")[0];

        try {
            var runtime = Runtime.getRuntime();
            var process = runtime.exec(command);
            process.waitFor();
            var stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            log.info("STDOUT: \n-----------------------------\n" + stdout.lines().collect(Collectors.joining("\n")) + "\n-----------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }

        var outputFile = new File(workspacePath + "out/output.txt");
        if (outputFile.exists()) {
            try (var lines = Files.lines(Path.of(outputFile.getAbsolutePath()))){
                return lines.collect(Collectors.joining("\n"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "No output";

    }

    private void initializeWorkspace(ProgramDTO program, String workspacePath) {
        var workspace = new File(workspacePath);
        workspace.mkdirs();

        var inFolder = new File(workspace.getAbsolutePath() + "/in");
        inFolder.mkdirs();
        var sourceFile = new File(inFolder.getAbsolutePath() + "/" +program.getFileName());
        try (var writer = new FileOutputStream(sourceFile)) {
            writer.write(program.getProgram().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        new File(workspace.getAbsolutePath() + "/out").mkdirs();
    }

}
