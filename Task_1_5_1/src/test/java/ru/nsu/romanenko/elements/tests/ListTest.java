package ru.nsu.romanenko.elements.tests;

import elements.List;
import elements.text.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListTest {

    @Test
    @DisplayName("Проверка нумерованного и маркированного списков")
    void testListRendering() {
        List numericList = new List.Builder()
                .setType(List.Types.NUMERIC)
                .addElement(new Text("First"))
                .addElement(new Text("Second"))
                .build();

        String expectedNumeric =
                "1. First\n" +
                        "2. Second\n";
        assertEquals(expectedNumeric, numericList.render());

        List markedList = new List.Builder()
                .setType(List.Types.MARKED)
                .addElement(new Text("Item"))
                .build();

        assertEquals("* Item\n", markedList.render());
    }

    @Test
    @DisplayName("Проверка вложенных списков (рекурсия)")
    void testNestedLists() {
        List subList = new List.Builder()
                .setType(List.Types.MARKED)
                .addElement(new Text("SubItem"))
                .build();

        List rootList = new List.Builder()
                .setType(List.Types.NUMERIC)
                .addElement(new Text("RootItem"))
                .addElement(subList)
                .build();

        String expected =
                "1. RootItem\n" +
                        "   * SubItem\n";
        assertEquals(expected, rootList.render());
    }

    @Test
    @DisplayName("Проверка комбинирования стилей текста")
    void testTextStyleCombination() {
        Text styledText = new Italics(new Bold("Hello"));

        String rendered = styledText.render();
        assert(rendered.contains("**"));
        assert(rendered.contains("_"));
        assert(rendered.contains("Hello"));
    }

    @Test
    @DisplayName("Проверка защиты стиля CODE")
    void testCodeStyleProtection() {
        Text codeText = new Code("System.out");
        codeText.applyStyleProtect(Styles.BOLD);

        assertEquals("`System.out`", codeText.render());
    }

    @Test
    @DisplayName("Проверка TaskItem (чекбоксы и отступы)")
    void testTaskItem() {
        elements.task.TaskItem task = new elements.task.TaskItem("To Do", false, 1);
        assertEquals("   - [ ] To Do", task.render());

        task.setCompleted(true);
        assertEquals("   - [x] To Do", task.render());
    }
}