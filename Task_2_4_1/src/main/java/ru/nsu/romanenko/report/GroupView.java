package ru.nsu.romanenko.report;

import java.util.List;

public record GroupView(String name, List<StudentView> students) {
}
