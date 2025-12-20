package elements.task;

import elements.MarkdownElement;

import java.util.Objects;

public class TaskItem extends MarkdownElement {
    private boolean completed;
    private final int indentLevel;

    public TaskItem(String content, boolean completed, int indentLevel) {
        super(content);
        this.completed = completed;
        this.indentLevel = indentLevel;
    }

    public TaskItem(MarkdownElement element, boolean completed, int indentLevel) {
        super(element.render());
        this.completed = completed;
        this.indentLevel = indentLevel;
    }

    @Override
    public String render() {
        String checkbox = completed ? "[x]" : "[ ]";
        String indent = "   ".repeat(indentLevel);
        return indent + "- " + checkbox + " " + content;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        if(!super.equals(o)) return false;

        TaskItem taskItem = (TaskItem) o;
        return (completed == taskItem.completed && indentLevel == taskItem.indentLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), completed, indentLevel);
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getIndentLevel() {
        return indentLevel;
    }
}