package elements.task;

import elements.MarkdownElement;

import java.util.ArrayList;

public class TaskList extends MarkdownElement {

    public static class Builder {
        private final ArrayList<MarkdownElement> tasks = new ArrayList<>();
        private int currentIndent = 0;

        public Builder addTask(String content, boolean completed) {
            tasks.add(new TaskItem(content, completed, currentIndent));
            return this;
        }

        public Builder addTask(MarkdownElement element, boolean completed) {
            tasks.add(new TaskItem(element, completed, currentIndent));
            return this;
        }

        public Builder indent() {
            currentIndent++;
            return this;
        }

        public Builder unindent() {
            currentIndent = Math.max(0, currentIndent - 1);
            return this;
        }

        public TaskList build() {
            TaskList taskList = new TaskList();
            taskList.addChildren(tasks);
            return taskList;
        }
    }

    @Override
    public String render() {
        StringBuilder stringBuilder = new StringBuilder();

        for (MarkdownElement task : super.getChildren()) {
            stringBuilder.append(task.render()).append("\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}