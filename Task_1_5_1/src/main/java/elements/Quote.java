package elements;

import elements.text.Text;


public class Quote extends MarkdownElement{
    public Quote(String content) {
        super(content);
    }

    public Quote(Text content)
    {
        super(content.render());
    }

    @Override
    public String render() {
        return "> " + content.replace("\n", "\n> ");
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
