package elements.text;

public class Code extends Text {
    public Code(String content) {
        super(content);
        applyStyleProtect(Styles.CODE);
    }

    public Code(Text content)
    {
        super(content);
        applyStyleProtect(Styles.CODE);
    }
}
