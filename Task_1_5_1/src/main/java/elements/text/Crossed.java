package elements.text;

public class Crossed extends Text {
    public Crossed(String content) {
        super(content);
        applyStyleProtect(Styles.CROSSED);
    }

    public Crossed(Text content)
    {
        super(content);
        applyStyleProtect(Styles.CROSSED);
    }
}
