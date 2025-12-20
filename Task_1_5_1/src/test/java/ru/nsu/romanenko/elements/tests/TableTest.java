package ru.nsu.romanenko.elements.tests;

import elements.Table;
import elements.text.Bold;
import elements.text.Code;
import elements.text.Italics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    @Test
    @DisplayName("Сложное выравнивание: LEFT, CENTER, RIGHT одновременно")
    void testFullAlignments() {
        Table table = new Table.Builder()
                .withAlignments(Table.Alignment.LEFT, Table.Alignment.CENTER, Table.Alignment.RIGHT)
                .addRow("L", "C", "R")
                .addRow("long left", "mid", "s")
                .build();

        String output = table.render();
        String[] lines = output.split("\n");
        String separator = lines[1];

        assertTrue(separator.contains(":--------"));
        assertTrue(separator.contains(":-:"));
        assertTrue(separator.contains("--:"));
    }

    @Test
    @DisplayName("Проверка разной ширины колонок (Padding)")
    void testColumnPadding() {
        Table table = new Table.Builder()
                .addRow("A", "Long Content")
                .addRow("Longer Header", "B")
                .build();

        String output = table.render();
        String[] lines = output.split("\n");

        assertTrue(lines[2].contains("| B            |"));
    }

    @Test
    @DisplayName("Проверка работы с null и пустыми ячейками")
    void testNullAndEmptyCells() {
        Table table = new Table.Builder()
                .addRow(null, "")
                .addRow("Data", 123)
                .build();

        String output = table.render();

        assertTrue(output.contains("|      |     |"));
    }

    @Test
    @DisplayName("Проверка RowLimit (граничные значения)")
    void testRowLimitEdgeCases() {
        Table tableOnlyHeader = new Table.Builder()
                .withRowLimit(1)
                .addRow("H1")
                .addRow("D1")
                .build();

        String output = tableOnlyHeader.render();

        assertTrue(output.contains("H1"));
        assertFalse(output.contains("D1"));
    }

    @Test
    @DisplayName("Таблица с неодинаковым количеством ячеек в строках")
    void testUnevenRows() {
        Table table = new Table.Builder()
                .addRow("H1", "H2", "H3")
                .addRow("D1")
                .build();

        assertDoesNotThrow(table::render);
        String output = table.render();

        assertTrue(output.contains("| D1  |     |     |"));
    }

    @Test
    @DisplayName("Стиль внутри таблицы (вложенные элементы)")
    void testNestedElementsInTable() {
        Table table = new Table.Builder()
                .addRow(new Bold("Header"), new Code("Status"))
                .addRow(new Italics("Value"), "100%")
                .build();

        String output = table.render();
        assertTrue(output.contains("**Header**"));
        assertTrue(output.contains("`Status`"));
        assertTrue(output.contains("_Value_"));
    }

    @Test
    @DisplayName("Проверка минимальной ширины разделителя (3 символа)")
    void testMinSeparatorWidth() {
        Table table = new Table.Builder()
                .addRow("a", "b")
                .build();

        String output = table.render();

        assertTrue(output.contains("| :-- | :-- |"));
    }

    @Test
    @DisplayName("Проверка выравнивания CENTER (баланс пробелов)")
    void testCenterAlignmentPadding() {
        Table table = new Table.Builder()
                .withAlignments(Table.Alignment.CENTER)
                .addRow("Header")
                .addRow("x")
                .build();

        String output = table.render();

        assertTrue(output.contains("|   x    |"));
    }
}