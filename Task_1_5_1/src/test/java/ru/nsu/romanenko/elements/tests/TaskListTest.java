package ru.nsu.romanenko.elements.tests;

import elements.task.TaskItem;
import elements.task.TaskList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
                .addTask("Zero indent", false)
                .build();

        assertEquals("- [ ] Zero indent\n", list.render());
    }
}