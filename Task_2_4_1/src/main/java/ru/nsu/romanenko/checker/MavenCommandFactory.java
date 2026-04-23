package ru.nsu.romanenko.checker;

import java.nio.file.Path;

public class MavenCommandFactory implements BuildCommandFactory {

    @Override
    public String[] compileCmd(Path taskDir)    { return cmd(taskDir, "compile"); }

    @Override
    public String[] javadocCmd(Path taskDir)    { return cmd(taskDir, "javadoc:javadoc"); }

    @Override
    public String[] checkstyleCmd(Path taskDir) { return cmd(taskDir, "checkstyle:check"); }

    @Override
    public String[] testCmd(Path taskDir)       { return cmd(taskDir, "test"); }

    private String[] cmd(Path taskDir, String goal) {
        return new String[]{"mvn", "-q", goal, "-f", taskDir.resolve("pom.xml").toString()};
    }
}
