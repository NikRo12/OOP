package elements.text;

public class Italics extends Text {
    public Italics(String content) {
        super(content);
        applyStyleProtect(Styles.ITALICS);
    }

    public Italics(Text content)
    {
        super(content);
        applyStyleProtect(Styles.ITALICS);
    }
}
