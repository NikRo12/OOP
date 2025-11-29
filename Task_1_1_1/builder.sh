#!/bin/bash
javac -d build src/main/java/ru/nsu/romanenko/HeapSort.java
javac -d build -cp build src/main/java/ru/nsu/romanenko/Sample.java

javadoc -d docs -sourcepath src/main/java ru.nsu.romanenko

echo "Main-Class: ru.nsu.romanenko.Sample" > manifest.txt
jar cvfm app.jar manifest.txt -C build .

# Запуск приложения
java -jar app.jar