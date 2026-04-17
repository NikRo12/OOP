package ru.nsu.romanenko.dsl;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {

    public static final String DEFAULT_SCRIPT_NAME = "oop_checker.groovy";
    public OopCheckerConfig load(File scriptFile) throws IOException {
        OopCheckerConfig config = new OopCheckerConfig();
        OopCheckerDslDelegate delegate = new OopCheckerDslDelegate(config, scriptFile.getParentFile());

        CompilerConfiguration compilerConfig = new CompilerConfiguration();
        compilerConfig.setScriptBaseClass("groovy.util.DelegatingScript");

        GroovyShell shell = new GroovyShell(
                getClass().getClassLoader(), new Binding(), compilerConfig);

        try {
            DelegatingScript script = (DelegatingScript) shell.parse(scriptFile);
            script.setDelegate(delegate);
            script.run();
        } catch (Exception e) {
            throw new IOException("Failed to parse configuration script: " + scriptFile.getPath(), e);
        }

        return config;
    }
}
