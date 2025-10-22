package ru.nsu.romanenko.elements.tests;

import elements.MarkdownElement;
import elements.task.TaskItem;
import elements.task.TaskList;
import elements.text.Styles;
import elements.text.Text;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskList and TaskItem Tests")
public class TaskListTest {

    @Nested
    @DisplayName("TaskItem Tests")
    class TaskItemTest {
        @Test
        @DisplayName("Should create task item with string content")
        void shouldCreateTaskItemWithStringContent() {
            TaskItem task = new TaskItem("Buy milk", false, 0);
            assertEquals("Buy milk", task.getContent());
            assertFalse(task.isCompleted());
            assertEquals(0, task.getIndentLevel());
        }

        @Test
        @DisplayName("Should create task item with MarkdownElement")
        void shouldCreateTaskItemWithMarkdownElement() {
            Text text = new Text("Important task");
            text.applyStyleProtect(Styles.BOLD);
            TaskItem task = new TaskItem(text, true, 1);
            assertEquals("**Important task**", task.getContent());
            assertTrue(task.isCompleted());
            assertEquals(1, task.getIndentLevel());
        }

        @Test
        @DisplayName("Should render completed task correctly")
        void shouldRenderCompletedTaskCorrectly() {
            TaskItem task = new TaskItem("Done task", true, 0);
            String result = task.render();
            assertEquals("- [x] Done task", result);
        }

        @Test
        @DisplayName("Should render incomplete task correctly")
        void shouldRenderIncompleteTaskCorrectly() {
            TaskItem task = new TaskItem("Todo task", false, 0);
            String result = task.render();
            assertEquals("- [ ] Todo task", result);
        }

        @Test
        @DisplayName("Should render indented task correctly")
        void shouldRenderIndentedTaskCorrectly() {
            TaskItem task = new TaskItem("Subtask", false, 2);
            String result = task.render();
            assertEquals("      - [ ] Subtask", result);
        }

        @Test
        @DisplayName("Should update completion status")
        void shouldUpdateCompletionStatus() {
            TaskItem task = new TaskItem("Task", false, 0);
            task.setCompleted(true);
            assertTrue(task.isCompleted());
            assertEquals("- [x] Task", task.render());
        }

        @Test
        @DisplayName("Should implement equals and hashCode correctly")
        void shouldImplementEqualsAndHashCodeCorrectly() {
            TaskItem task1 = new TaskItem("Task", true, 1);
            TaskItem task2 = new TaskItem("Task", true, 1);
            TaskItem task3 = new TaskItem("Different", true, 1);
            TaskItem task4 = new TaskItem("Task", false, 1);
            TaskItem task5 = new TaskItem("Task", true, 2);

            assertEquals(task1, task2);
            assertEquals(task1.hashCode(), task2.hashCode());
            assertNotEquals(task1, task3);
            assertNotEquals(task1, task4);
            assertNotEquals(task1, task5);
        }

        @Test
        @DisplayName("Should not be equal to null or different class")
        void shouldNotBeEqualToNullOrDifferentClass() {
            TaskItem task = new TaskItem("Test", false, 0);
            assertNotEquals(null, task);
            assertNotEquals("string", task);
        }
    }

    @Nested
    @DisplayName("TaskList Builder Tests")
    class TaskListBuilderTest {
        @Test
        @DisplayName("Should build task list with multiple tasks")
        void shouldBuildTaskListWithMultipleTasks() {
            TaskList taskList = new TaskList.Builder()
                    .addTask("Task 1", false)
                    .addTask("Task 2", true)
                    .build();

            assertEquals(2, taskList.getChildrenCount());
        }

        @Test
        @DisplayName("Should build task list with formatted elements")
        void shouldBuildTaskListWithFormattedElements() {
            Text text = new Text("Important");
            text.applyStyleProtect(Styles.BOLD);
            TaskList taskList = new TaskList.Builder()
                    .addTask(text, true)
                    .build();

            assertEquals(1, taskList.getChildrenCount());
        }

        @Test
        @DisplayName("Should handle indentation correctly")
        void shouldHandleIndentationCorrectly() {
            TaskList taskList = new TaskList.Builder()
                    .addTask("Main task", false)
                    .indent()
                    .addTask("Subtask 1", false)
                    .addTask("Subtask 2", true)
                    .unindent()
                    .addTask("Another main", false)
                    .build();

            assertEquals(4, taskList.getChildrenCount());
        }

        @Test
        @DisplayName("Should not allow negative indentation")
        void shouldNotAllowNegativeIndentation() {
            TaskList taskList = new TaskList.Builder()
                    .unindent()
                    .unindent()
                    .addTask("Task", false)
                    .build();

            TaskItem task = (TaskItem) taskList.getChildren().get(0);
            assertEquals(0, task.getIndentLevel());
        }

        @Test
        @DisplayName("Should build empty task list")
        void shouldBuildEmptyTaskList() {
            TaskList taskList = new TaskList.Builder().build();
            assertEquals(0, taskList.getChildrenCount());
        }
    }

    @Nested
    @DisplayName("TaskList Render Tests")
    class TaskListRenderTest {
        @Test
        @DisplayName("Should render task list correctly")
        void shouldRenderTaskListCorrectly() {
            TaskList taskList = new TaskList.Builder()
                    .addTask("First", false)
                    .addTask("Second", true)
                    .build();

            String result = taskList.render();
            assertTrue(result.contains("- [ ] First"));
            assertTrue(result.contains("- [x] Second"));
        }

        @Test
        @DisplayName("Should render nested tasks with indentation")
        void shouldRenderNestedTasksWithIndentation() {
            TaskList taskList = new TaskList.Builder()
                    .addTask("Main", false)
                    .indent()
                    .addTask("Sub 1", false)
                    .addTask("Sub 2", true)
                    .build();

            String result = taskList.render();
            assertTrue(result.contains("- [ ] Main"));
            assertTrue(result.contains("   - [ ] Sub 1"));
            assertTrue(result.contains("   - [x] Sub 2"));
        }

        @Test
        @DisplayName("Should render deeply nested tasks")
        void shouldRenderDeeplyNestedTasks() {
            TaskList taskList = new TaskList.Builder()
                    .addTask("Level 0", false)
                    .indent()
                    .addTask("Level 1", false)
                    .indent()
                    .addTask("Level 2", true)
                    .build();

            String result = taskList.render();
            assertTrue(result.contains("- [ ] Level 0"));
            assertTrue(result.contains("   - [ ] Level 1"));
            assertTrue(result.contains("      - [x] Level 2"));
        }

        @Test
        @DisplayName("Should render task list with formatted content")
        void shouldRenderTaskListWithFormattedContent() {
            Text text = new Text("Urgent");
            text.applyStyleProtect(Styles.BOLD);
            TaskList taskList = new TaskList.Builder()
                    .addTask(text, false)
                    .build();

            String result = taskList.render();
            assertTrue(result.contains("- [ ] **Urgent**"));
        }

        @Test
        @DisplayName("Should render empty task list as empty string")
        void shouldRenderEmptyTaskListAsEmptyString() {
            TaskList taskList = new TaskList.Builder().build();
            String result = taskList.render();
            assertEquals("", result);
        }
    }

    @Nested
    @DisplayName("TaskList Equality Tests")
    class TaskListEqualityTest {
        @Test
        @DisplayName("Should be equal when same tasks")
        void shouldBeEqualWhenSameTasks() {
            TaskList list1 = new TaskList.Builder()
                    .addTask("Task", false)
                    .build();
            TaskList list2 = new TaskList.Builder()
                    .addTask("Task", false)
                    .build();

            assertEquals(list1, list2);
            assertEquals(list1.hashCode(), list2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when different tasks")
        void shouldNotBeEqualWhenDifferentTasks() {
            TaskList list1 = new TaskList.Builder()
                    .addTask("Task 1", false)
                    .build();
            TaskList list2 = new TaskList.Builder()
                    .addTask("Task 2", false)
                    .build();

            assertNotEquals(list1, list2);
        }

        @Test
        @DisplayName("Should not be equal to null or different class")
        void shouldNotBeEqualToNullOrDifferentClass() {
            TaskList taskList = new TaskList.Builder().build();
            assertNotEquals(null, taskList);
            assertNotEquals("string", taskList);
        }
    }

    @Nested
    @DisplayName("TaskList Integration Tests")
    class TaskListIntegrationTest {
        @Test
        @DisplayName("Should handle complex nested structure")
        void shouldHandleComplexNestedStructure() {
            TaskList taskList = new TaskList.Builder()
                    .addTask("Project", false)
                    .indent()
                    .addTask("Phase 1", true)
                    .indent()
                    .addTask("Task 1.1", true)
                    .addTask("Task 1.2", false)
                    .unindent()
                    .addTask("Phase 2", false)
                    .unindent()
                    .addTask("Final", false)
                    .build();

            assertEquals(6, taskList.getChildrenCount());
            String result = taskList.render();
            assertTrue(result.contains("- [ ] Project"));
            assertTrue(result.contains("   - [x] Phase 1"));
            assertTrue(result.contains("      - [x] Task 1.1"));
            assertTrue(result.contains("      - [ ] Task 1.2"));
            assertTrue(result.contains("   - [ ] Phase 2"));
            assertTrue(result.contains("- [ ] Final"));
        }

        @Test
        @DisplayName("Should handle mixed content types")
        void shouldHandleMixedContentTypes() {
            Text boldText = new Text("Bold task");
            boldText.applyStyleProtect(Styles.BOLD);
            TaskList taskList = new TaskList.Builder()
                    .addTask("Plain task", false)
                    .addTask(boldText, true)
                    .addTask("Another plain", false)
                    .build();

            String result = taskList.render();
            assertTrue(result.contains("- [ ] Plain task"));
            assertTrue(result.contains("- [x] **Bold task**"));
            assertTrue(result.contains("- [ ] Another plain"));
        }
    }
}