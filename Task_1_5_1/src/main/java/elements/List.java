package elements;

import java.util.ArrayList;

public class List extends MarkdownElement {
    private final Types type;

    public List(Types type, ArrayList<MarkdownElement> elements)
    {
        this.type = type;
        addChildren(elements);
    }

    public static class Builder
    {
        private Types type = Types.NUMERIC;
        private final ArrayList<MarkdownElement> list_elements = new ArrayList<>();

        public Builder setType(Types type)
        {
            this.type = type;
            return this;
        }

        public Builder addElement(MarkdownElement element)
        {
            this.list_elements.add(element);
            return this;
        }

        public List build()
        {
            return new List(type, list_elements);
        }
    }

    public Types getType()
    {
        return type;
    }

    @Override
    public String render() {
        return renderRecursive(this, 0);
    }

    private String renderRecursive(List list, int indentLevel) {
        StringBuilder stringBuilder = new StringBuilder();
        String indent = "   ".repeat(indentLevel);

        if (list.type == Types.NUMERIC) {
            int i = 1;
            for (MarkdownElement curr : list.getChildren()) {
                if (curr instanceof List) {
                    stringBuilder.append(renderRecursive((List) curr, indentLevel + 1));
                } else {
                    stringBuilder.append(indent)
                            .append(i++).append(". ")
                            .append(curr.render()).append("\n");
                }
            }
        } else {
            for (MarkdownElement curr : list.getChildren()) {
                if (curr instanceof List) {
                    stringBuilder.append(renderRecursive((List) curr, indentLevel + 1));
                } else {
                    stringBuilder.append(indent)
                            .append(list.type.mark).append(" ")
                            .append(curr.render()).append("\n");
                }
            }
        }

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        List list = (List) o;
        return type == list.type;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    public enum Types {
        NUMERIC,
        MARKED("*");

        private final String mark;

        Types() {
            this.mark = "";
        }

        Types(String mark) {
            this.mark = mark;
        }

        public String getMark() {
            return mark;
        }
    }
}

