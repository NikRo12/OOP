package ru.nsu.romanenko.elements.tests;

import elements.List;
import elements.task.TaskItem;
import elements.text.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class ListTest {

    @Test
    @DisplayName("Проверка нумерованного и маркированного списков")
    void testListRendering() {
        List numericList = new List.Builder()
                .setType(List.Types.NUMERIC)
                .addElement(new Text("First"))
                .addElement(new Text("Second"))
                .build();

        String expectedNumeric = "1. First\n2. Second\n";
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
        List subSubList = new List.Builder()
                .setType(List.Types.NUMERIC)
                .addElement(new Text("Deep"))
                .build();

        List subList = new List.Builder()
                .setType(List.Types.MARKED)
                .addElement(new Text("SubItem"))
                .addElement(subSubList)
                .build();

        List rootList = new List.Builder()
                .setType(List.Types.NUMERIC)
                .addElement(new Text("RootItem"))
                .addElement(subList)
                .build();

        String expected =
                "1. RootItem\n" +
                        "   * SubItem\n" +
                        "      1. Deep\n";
        assertEquals(expected, rootList.render());
    }

    @Test
    @DisplayName("Проверка комбинирования стилей текста")
    void testTextStyleCombination() {
        Text styledText = new Italics(new Bold(new Crossed("Hello")));

        String rendered = styledText.render();
        assertTrue(rendered.contains("**"));
        assertTrue(rendered.contains("_"));
        assertTrue(rendered.contains("~~"));
        assertTrue(rendered.contains("Hello"));
    }

    @Test
    @DisplayName("Проверка защиты стиля CODE")
    void testCodeStyleProtection() {
        Text codeText = new Code("System.out");
        codeText.applyStyleProtect(Styles.BOLD);
        codeText.applyStyleProtect(Styles.ITALICS);

        assertEquals("`System.out`", codeText.render());
        assertTrue(codeText.getStyles().get(Styles.CODE));
        assertFalse(codeText.getStyles().get(Styles.BOLD));
    }

    @Test
    @DisplayName("Проверка TaskItem (чекбоксы и отступы)")
    void testTaskItem() {
        TaskItem task = new TaskItem("To Do", false, 1);
        assertEquals("   - [ ] To Do", task.render());

        task.setCompleted(true);
        assertEquals("   - [x] To Do", task.render());

        TaskItem taskFromElement = new TaskItem(new Bold("Task"), false, 0);
        assertEquals("- [ ] **Task**", taskFromElement.render());
    }

    @Test
    @DisplayName("Проверка равенства списков")
    void testListEquality() {
        List list1 = new List.Builder().setType(List.Types.MARKED).addElement(new Text("A")).build();
        List list2 = new List.Builder().setType(List.Types.MARKED).addElement(new Text("A")).build();
        List list3 = new List.Builder().setType(List.Types.NUMERIC).addElement(new Text("A")).build();

        assertEquals(list1, list2);
        assertNotEquals(list1, list3);
        assertEquals(list1.hashCode(), list2.hashCode());
    }

    @Test
    @DisplayName("Проверка типов стилей")
    void testStylesEnum() {
        assertEquals("bold", Styles.BOLD.getStyle());
        assertEquals("**", Styles.BOLD.getWrite());
        assertEquals("_", Styles.ITALICS.getWrite());
        assertEquals("~~", Styles.CROSSED.getWrite());
        assertEquals("`", Styles.CODE.getWrite());
    }

    @Test
    @DisplayName("Проверка конструктора копирования Text")
    void testTextCopyConstructor() {
        Bold original = new Bold("Double");
        Text copy = new Text(original);

        assertEquals(original.render(), copy.render());
        assertTrue(copy.getStyles().get(Styles.BOLD));
    }

    @Test
    @DisplayName("Проверка hashCode и equals для TaskItem")
    void testTaskItemEquality() {
        TaskItem t1 = new TaskItem("Job", true, 2);
        TaskItem t2 = new TaskItem("Job", true, 2);
        TaskItem t3 = new TaskItem("Job", false, 2);

        assertEquals(t1, t2);
        assertNotEquals(t1, t3);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    @DisplayName("Проверка пустого списка")
    void testEmptyList() {
        List emptyList = new List.Builder().build();
        assertEquals("", emptyList.render());
    }
}