package elements;

import elements.text.Text;

public class Image extends Link {
    public Image(Text content, String realLink) {
        super(content, realLink);
    }

    public Image(String content, String realLink) {
        super(content, realLink);
    }

    @Override
    public String render() {
        return "![" + content + "]" + "(" + super.getRealLink() + ")";
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
