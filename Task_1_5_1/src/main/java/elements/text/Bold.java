package elements.text;

public class Bold extends Text {
    public Bold(String content)
    {
        super(content);
        applyStyleProtect(Styles.BOLD);
    }

    public Bold(Text content)
    {
        super(content);
        applyStyleProtect(Styles.BOLD);
    }
}