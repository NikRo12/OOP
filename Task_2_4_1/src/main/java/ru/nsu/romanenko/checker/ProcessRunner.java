package ru.nsu.romanenko.checker;

import java.io.File;
import java.io.IOException;

@FunctionalInterface
public interface ProcessRunner {
    ProcessResult run(File workDir, String... command) throws IOException;
}
