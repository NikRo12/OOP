package elements;

import java.util.*;

public class CodeBlock extends MarkdownElement {
    private final String language;
    private final ArrayList<String> lines;

    public CodeBlock(String code, String language) {
        super("");
        this.language = language != null ? language : "";
        this.lines = parseCodeLines(code);
    }

    public CodeBlock(ArrayList<String> codeLines, String language) {
        super("");
        this.language = language != null ? language : "";
        this.lines = new ArrayList<>(codeLines);
    }

    private ArrayList<String> parseCodeLines(String code) {
        ArrayList<String> result = new ArrayList<>();
        if (code != null && !code.isEmpty()) {
            String[] splitLines = code.split("\n", -1);
            Collections.addAll(result, splitLines);
        }
        return result;
    }

    public static class Builder {
        private String language = "";
        private final ArrayList<String> lines = new ArrayList<>();

        public Builder setLanguage(String language) {
            this.language = language != null ? language : "";
            return this;
        }

        public Builder addLine(String line) {
            if (line != null) {
                this.lines.add(line);
            }
            return this;
        }

        public Builder addLines(String... lines) {
            for (String line : lines) {
                if (line != null) {
                    this.lines.add(line);
                }
            }
            return this;
        }

        public Builder addCode(String code) {
            if (code != null) {
                String[] codeLines = code.split("\n", -1);
                this.lines.addAll(Arrays.asList(codeLines));
            }
            return this;
        }

        public CodeBlock build() {
            return new CodeBlock(lines, language);
        }
    }

    public String getLanguage() {
        return language;
    }

    public ArrayList<String> getLines() {
        return new ArrayList<>(lines);
    }

    public int getLineCount() {
        return lines.size();
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder();

        sb.append("```").append(language).append("\n");

        for (String line : lines) {
            sb.append(line).append("\n");
        }

        sb.append("```");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CodeBlock codeBlock = (CodeBlock) o;
        return Objects.equals(language, codeBlock.language) &&
                Objects.equals(lines, codeBlock.lines);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(language);
        result = 31 * result + lines.hashCode();
        return result;
    }
}