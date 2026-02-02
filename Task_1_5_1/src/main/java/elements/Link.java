package elements;

import elements.text.Text;


public class Link extends MarkdownElement {
    private final String realLink;

    public Link(Text content, String realLink) {
        super(content.render());
        this.realLink = realLink;
    }

    public Link(String content, String realLink) {
        super(content);
        this.realLink = realLink;
    }

    public String getRealLink()
    {
        return realLink;
    }

    @Override
    public String render() {
        return "[" + content + "]" + "(" + realLink + ")";
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        if(!super.equals(o)) return false;

        Link link = (Link) o;
        return realLink.equals(link.realLink);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + realLink.hashCode();
        return result;
    }
}
