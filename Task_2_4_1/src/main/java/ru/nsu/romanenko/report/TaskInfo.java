package ru.nsu.romanenko.report;

import java.time.LocalDate;

public record TaskInfo(String id, String title, int maxScore,
                       LocalDate softDeadline, LocalDate hardDeadline) {
}
