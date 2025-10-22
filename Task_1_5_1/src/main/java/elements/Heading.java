package elements;

import elements.text.Text;

public class Heading extends MarkdownElement {
    private final int level;

    public Heading(String content, int level) {
        super(content + "\n");
        this.level = level;
    }

    public Heading(int level, Text element) {
        super(element.render() + "\n");
        this.level = level;
    }

    public static class Builder {
        private String content = "";
        private int level = 1;

        public Builder setContent(Text content) {
            this.content = content.render();
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setLevel(int level) {
            this.level = level;
            return this;
        }

        public Heading build() {
            return new Heading(content, level);
        }
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String render() {
        if(level == 1)
        {
            return this.content + "=".repeat(this.content.length()) + "\n";
        }

        else if(level == 2)
        {
            return this.content + "-".repeat(this.content.length()) + "\n";
        }

        return "#".repeat(level) + " " + content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Heading heading = (Heading) o;
        return level == heading.level;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + level;
        return result;
    }
}