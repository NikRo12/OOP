package elements;

import java.util.ArrayList;
import java.util.List;

public abstract class MarkdownElement {
    protected String content;
    protected List<MarkdownElement> children;

    public MarkdownElement() {
        this("");
    }

    public MarkdownElement(String content) {
        this.content = content != null ? content : "";
        this.children = new ArrayList<>();
    }

    /**
     * Преобразует элемент в строку Markdown
     */
    public abstract String render();

    /**
     * Добавляет дочерний элемент
     */
    public void addChild(MarkdownElement child) {
        if (child != null) {
            children.add(child);
        }
    }

    /**
     * Добавляет несколько дочерних элементов
     */
    public void addChildren(List<MarkdownElement> children) {
        if (children != null) {
            this.children.addAll(children);
        }
    }

    /**
     * Возвращает список дочерних элементов (копию)
     */
    public List<MarkdownElement> getChildren() {
        return new ArrayList<>(children);
    }

    /**
     * Удаляет дочерний элемент
     */
    public boolean removeChild(MarkdownElement child) {
        return children.remove(child);
    }

    /**
     * Очищает все дочерние элементы
     */
    public void clearChildren() {
        children.clear();
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return render();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarkdownElement that = (MarkdownElement) o;

        if (!content.equals(that.content)) return false;
        return children.equals(that.children);
    }

    @Override
    public int hashCode() {
        int result = content.hashCode();
        result = 31 * result + children.hashCode();
        return result;
    }

    /**
     * Возвращает имя класса для отладки
     */
    public String getElementName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Проверяет, есть ли дочерние элементы
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    /**
     * Возвращает количество дочерних элементов
     */
    public int getChildrenCount() {
        return children.size();
    }
}