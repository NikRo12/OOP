package elements.text;

import elements.MarkdownElement;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO Exceptions
 */

public class Text extends MarkdownElement {
    Map<Styles, Boolean> styles = new HashMap<>();


    public Text(String content)
    {
        super(content);
        for (Styles curr : Styles.values()) {
            styles.put(curr, false);
        }
    }

    public Text(Text other) {
        super.content = other.content;
        this.styles = new HashMap<>(other.styles);
    }

    public void applyStyleProtect(Styles style) {
        if(!styles.get(style) &&
                !(styles.get(Styles.CODE)))
        {
            this.styles.put(style, true);
        }
    }

    public Map<Styles, Boolean> getStyles()
    {
        return new HashMap<Styles, Boolean>(styles);
    }

    @Override
    public String render() {
        StringBuilder result = new StringBuilder();
        for(Map.Entry<Styles, Boolean> curr : this.styles.entrySet())
        {
            if(curr.getValue())
            {
                result.append(curr.getKey().getWrite()).append(this.content).append(curr.getKey().getWrite());
            }
        }

        return !result.isEmpty() ? result.toString() : this.content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Text text = (Text) o;
        return styles.equals(text.styles);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + styles.hashCode();
        return result;
    }
}
