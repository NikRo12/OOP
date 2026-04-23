package ru.nsu.romanenko.checker;

import java.nio.file.Path;

public interface BuildCommandFactory {

    String[] compileCmd(Path taskDir);
    String[] javadocCmd(Path taskDir);
    String[] checkstyleCmd(Path taskDir);
    String[] testCmd(Path taskDir);

    static BuildCommandFactory forTool(BuildTool tool) {
        return switch (tool) {
            case GRADLE -> new GradleCommandFactory();
            case MAVEN  -> new MavenCommandFactory();
        };
    }
}
