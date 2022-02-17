package com.sndbx.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskService {

    @Value("${application.workspacePath}")
    private String mainWorkspacePath;

    @Value("${application.dockerImage}")
    private String dockerImage;

    public ExecutionOutput execute(Task task) {
        if (!task.getLanguage().equals(Language.JAVA)) {
            throw new IllegalStateException(String.format("Language %s not supported!", task.getLanguage()));
        }

        var uuid = UUID.randomUUID().toString();
        var workspacePath = new File(mainWorkspacePath, uuid);
        initializeWorkspace(task, workspacePath);

//        var command = mainWorkspacePath + "runner.sh 5s -dit -v " + workspacePath + ":/workspace/usercode/ java-runner:1 sh script.sh " + program.getFileName().split("\\.")[0];
        var command = "docker run --rm -i " +
                "-v " + workspacePath.getAbsolutePath() + "/src/:/workspace/src/ " +
                "-v " + workspacePath.getAbsolutePath() + "/test-results/:/workspace/build/test-results/ " +
                "java-gradle-runner:1";
        String[] shellCommand = {"/bin/bash", "-c", command};

        var output = new ExecutionOutput();

        var stdout = "";
        var startTime = System.currentTimeMillis();
        try {
            var runtime = Runtime.getRuntime();
            var process = runtime.exec(shellCommand);
            process.waitFor();

            output.setStatus(process.exitValue() == 0 ? Status.SUCCESS : Status.FAILURE);

            stdout = new BufferedReader(new InputStreamReader(process.getInputStream())).lines().collect(Collectors.joining("\n"));
            log.info("STDOUT: \n-----------------------------\n" + stdout + "\n-----------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }

        output.setExecutionTime(System.currentTimeMillis() - startTime);
        output.setStdout(stdout);
//        output.setCompilerOutput(getFileContentsIfExists(new File(workspacePath + "out/compiler_output.txt")));
//        output.setProgramOutput(getFileContentsIfExists(new File(workspacePath + "out/program_output.txt")));

        //TODO: parse test result XMLs and make sth out of them

        return output;
    }

    private void initializeWorkspace(Task task, File workspacePath) {
        var mainSrcPath = new File(workspacePath, "src/main/java/");
        var testSrcPath = new File(workspacePath, "src/test/java/");
        mainSrcPath.mkdirs();
        testSrcPath.mkdirs();

        // Get main class filename
        var pattern = Pattern.compile("public class [A-Za-z][A-Za-z0-9]+");

        var solutionMatcher = pattern.matcher(task.getValidSolutionCode());
        solutionMatcher.find();
        var solutionFilename = solutionMatcher.group().split(" ")[2] + ".java";

        var solutionSourceFile = new File(mainSrcPath, solutionFilename);
        try (var writer = new FileOutputStream(solutionSourceFile)) {
            writer.write(task.getValidSolutionCode().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (task.getBoilerplateCode() != null && task.getBoilerplateCode().length() > 0) {
            var biolerplateMatcher = pattern.matcher(task.getBoilerplateCode());
            biolerplateMatcher.find();
            var boilerplateFilename = biolerplateMatcher.group().split(" ")[2] + ".java";

            var boilerplateSourceFile = new File(mainSrcPath, boilerplateFilename);
            try (var writer = new FileOutputStream(boilerplateSourceFile)) {
                writer.write(task.getBoilerplateCode().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        var validationTestsSourceFile = new File(testSrcPath, "Tests.java");
        try (var writer = new FileOutputStream(validationTestsSourceFile)) {
            writer.write(task.getValidationTestCode().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Optional<String> getFileContentsIfExists(File file) {
        if (file.exists()) {
            try (var lines = Files.lines(Path.of(file.getAbsolutePath()))) {
                return Optional.of(lines.collect(Collectors.joining("\n")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

}
