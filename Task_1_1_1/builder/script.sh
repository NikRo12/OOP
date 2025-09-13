#!/bin/bash

javac -d build src/main/java/ru/nsu/romanenko/Sample.java

javac -d build -cp build src/test/java/ru/nsu/romanenko/SampleTests.java
javadoc -d docs build src/test/java/ru/nsu/romanenko/SampleTests.java

echo "Main-Class: ru.nsu.romanenko.Sample" > manifest.txt
jar cvfm app.jar manifest.txt -C build .

java -jar app.jar

java -cp build org.junit.runner.JUnitCore ru.nsu.romanenko.SampleTests