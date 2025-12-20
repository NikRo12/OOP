package ru.nsu.romanenko.elements.tests;

import elements.task.TaskItem;
import elements.task.TaskList;
import elements.text.Bold;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskListTest {

    @Test
    @DisplayName("Проверка рендеринга одиночного TaskItem")
    void testTaskItemRender() {
        TaskItem undone = new TaskItem("Task 1", false, 0);
        assertEquals("- [ ] Task 1", undone.render());

        TaskItem done = new TaskItem("Task 2", true, 1);
        assertEquals("   - [x] Task 2", done.render());
    }

    @Test
    @DisplayName("Проверка сборки TaskList через Builder")
    void testTaskListBuilder() {
        TaskList list = new TaskList.Builder()
                .addTask("Root task", false)
                .indent()
                .addTask("Sub task done", true)
                .addTask("Sub task undone", false)
                .unindent()
                .addTask("Final task", true)
                .build();

        String expected =
                "- [ ] Root task\n" +
                        "   - [x] Sub task done\n" +
                        "   - [ ] Sub task undone\n" +
                        "- [x] Final task\n";

        assertEquals(expected, list.render());
    }

    @Test
    @DisplayName("Проверка защиты от отрицательного отступа")
    void testUnindentLimit() {
        TaskList list = new TaskList.Builder()
                .unindent()
                .unindent()
                .addTask("Zero indent", false)
                .build();

        assertEquals("- [ ] Zero indent\n", list.render());
    }

    @Test
    @DisplayName("Проверка добавления MarkdownElement в TaskList")
    void testAddTaskWithElement() {
        Bold boldTask = new Bold("Bold Task");
        TaskList list = new TaskList.Builder()
                .addTask(boldTask, true)
                .build();

        assertEquals("- [x] **Bold Task**\n", list.render());
    }

    @Test
    @DisplayName("Проверка равенства TaskItem")
    void testTaskItemEquality() {
        TaskItem item1 = new TaskItem("Task", true, 2);
        TaskItem item2 = new TaskItem("Task", true, 2);
        TaskItem item3 = new TaskItem("Task", false, 2);

        assertEquals(item1, item2);
        assertNotEquals(item1, item3);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    @DisplayName("Проверка равенства TaskList")
    void testTaskListEquality() {
        TaskList list1 = new TaskList.Builder().addTask("T", true).build();
        TaskList list2 = new TaskList.Builder().addTask("T", true).build();

        assertEquals(list1, list2);
        assertEquals(list1.hashCode(), list2.hashCode());
    }

    @Test
    @DisplayName("Проверка изменения состояния TaskItem")
    void testTaskItemStateChange() {
        TaskItem task = new TaskItem("Edit", false, 0);
        assertFalse(task.render().contains("[x]"));

        task.setCompleted(true);
        assertTrue(task.render().contains("[x]"));
    }

    @Test
    @DisplayName("Проверка глубокого отступа")
    void testDeepIndentation() {
        TaskList list = new TaskList.Builder()
                .indent()
                .indent()
                .indent()
                .addTask("Deep", false)
                .build();

        assertEquals("         - [ ] Deep\n", list.render());
    }
}