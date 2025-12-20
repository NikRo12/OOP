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

    public abstract String render();

    public void addChild(MarkdownElement child) {
        if (child != null) {
            children.add(child);
        }
    }

    public void addChildren(List<MarkdownElement> children) {
        if (children != null) {
            this.children.addAll(children);
        }
    }

    public List<MarkdownElement> getChildren() {
        return new ArrayList<>(children);
    }

    public boolean removeChild(MarkdownElement child) {
        return children.remove(child);
    }

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

    public String getElementName() {
        return this.getClass().getSimpleName();
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public int getChildrenCount() {
        return children.size();
    }
}