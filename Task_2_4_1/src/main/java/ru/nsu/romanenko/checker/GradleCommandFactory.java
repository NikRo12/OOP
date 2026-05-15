package ru.nsu.romanenko.checker;

import java.nio.file.*;

public class GradleCommandFactory implements BuildCommandFactory {

    @Override
    public String[] compileCmd(Path taskDir)    { return cmd(taskDir, "compileJava"); }

    @Override
    public String[] javadocCmd(Path taskDir)    { return cmd(taskDir, "javadoc"); }

    @Override
    public String[] checkstyleCmd(Path taskDir) { return cmd(taskDir, "checkstyleMain"); }

    @Override
    public String[] testCmd(Path taskDir)       { return cmd(taskDir, "test"); }

    private String[] cmd(Path taskDir, String task) {
        Path gradlew = taskDir.resolve(
            System.getProperty("os.name", "").toLowerCase().contains("win") ? "gradlew.bat" : "gradlew");
        String exe = Files.isExecutable(gradlew) ? gradlew.toAbsolutePath().toString() : "gradle";
        return new String[]{exe, task, "--no-daemon", "-q", "-p", taskDir.toAbsolutePath().toString()};
    }
}
