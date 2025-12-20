package elements.text;

public enum Styles
{
    BOLD("bold", "**"),
    ITALICS("italics", "_"),
    CROSSED("crossed", "~~"),
    CODE("code", "`");


    Styles(String style, String write)
    {
        this.style = style;
        this.write = write;
    }

    private final String style;
    private final String write;

    public String getStyle() {
        return style;
    }

    public String getWrite() {
        return write;
    }
}