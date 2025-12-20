package elements;

import elements.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Table extends MarkdownElement {
    private final List<Alignment> alignments;
    private final int rowLimit;
    private final List<List<MarkdownElement>> rows;

    public Table(List<Alignment> alignments, int rowLimit, List<List<MarkdownElement>> rows) {
        this.alignments = new ArrayList<>(alignments);
        this.rowLimit = rowLimit;
        this.rows = new ArrayList<>(rows);
    }

    public static class Builder {
        private List<Alignment> alignments = new ArrayList<>();
        private int rowLimit = -1;
        private final List<List<MarkdownElement>> rows = new ArrayList<>();

        public Builder withAlignments(Alignment... alignments) {
            this.alignments = List.of(alignments);
            return this;
        }

        public Builder withRowLimit(int rowLimit) {
            this.rowLimit = rowLimit;
            return this;
        }

        public Builder addRow(Object... cells) {
            List<MarkdownElement> row = new ArrayList<>();
            for (Object cell : cells) {
                if (cell instanceof MarkdownElement) {
                    row.add((MarkdownElement) cell);
                } else {
                    row.add(new Text(cell != null ? cell.toString() : ""));
                }
            }
            rows.add(row);
            return this;
        }

        public Table build() {
            List<List<MarkdownElement>> finalRows = rowLimit > 0 && rows.size() > rowLimit
                    ? rows.subList(0, rowLimit)
                    : new ArrayList<>(rows);

            return new Table(alignments, rowLimit, finalRows);
        }
    }

    public List<List<MarkdownElement>> getRows() {
        return rows;
    }

    public int getRowLimit() {
        return rowLimit;
    }

    public List<Alignment> getAlignments() {
        return alignments;
    }

    public enum Alignment {
        LEFT, CENTER, RIGHT
    }

    public static final Alignment ALIGN_LEFT = Alignment.LEFT;
    public static final Alignment ALIGN_CENTER = Alignment.CENTER;
    public static final Alignment ALIGN_RIGHT = Alignment.RIGHT;

    @Override
    public String render() {
        if (rows.isEmpty()) return "";

        List<List<String>> renderedRows = rows.stream()
                .map(row -> row.stream()
                        .map(MarkdownElement::render)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        int columnCount = getColumnCount(renderedRows);

        int[] colWidths = calculateColumnWidths(renderedRows, columnCount);

        StringBuilder sb = new StringBuilder();

        renderRow(sb, renderedRows.get(0), colWidths);

        renderSeparator(sb, colWidths);

        for (int i = 1; i < renderedRows.size(); i++) {
            renderRow(sb, renderedRows.get(i), colWidths);
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Table table = (Table) o;
        return rowLimit == table.rowLimit &&
                alignments.equals(table.alignments) &&
                rows.equals(table.rows);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + alignments.hashCode();
        result = 31 * result + rowLimit;
        result = 31 * result + rows.hashCode();
        return result;
    }

    private int getColumnCount(List<List<String>> renderedRows) {
        int max = 0;
        for (List<String> row : renderedRows) {
            max = Math.max(max, row.size());
        }
        return max;
    }

    private int[] calculateColumnWidths(List<List<String>> renderedRows, int columnCount) {
        int[] widths = new int[columnCount];

        Arrays.fill(widths, 3);

        for (List<String> row : renderedRows) {
            for (int i = 0; i < row.size(); i++) {
                if (row.get(i) != null) {
                    widths[i] = Math.max(widths[i], row.get(i).length());
                }
            }
        }
        return widths;
    }

    private void renderRow(StringBuilder sb, List<String> row, int[] colWidths) {
        sb.append("|");
        for (int i = 0; i < colWidths.length; i++) {
            String content = i < row.size() ? row.get(i) : "";
            Alignment align = getAlignmentForColumn(i);
            int width = colWidths[i];

            sb.append(" ");
            sb.append(padString(content, width, align));
            sb.append(" |");
        }
        sb.append("\n");
    }

    private void renderSeparator(StringBuilder sb, int[] colWidths) {
        sb.append("|");
        for (int i = 0; i < colWidths.length; i++) {
            Alignment align = getAlignmentForColumn(i);
            int width = colWidths[i];

            sb.append(" ");
            sb.append(generateSeparatorLine(width, align));
            sb.append(" |");
        }
        sb.append("\n");
    }

    private String padString(String content, int width, Alignment align) {
        int length = content.length();
        int padding = width - length;
        if (padding <= 0) return content;

        return switch (align) {
            case RIGHT -> " ".repeat(padding) + content;
            case CENTER -> {
                int leftPad = padding / 2;
                int rightPad = padding - leftPad;
                yield " ".repeat(leftPad) + content + " ".repeat(rightPad);
            }
            default -> content + " ".repeat(padding);
        };
    }

    private String generateSeparatorLine(int width, Alignment align) {
        int effectiveWidth = Math.max(width, 3);

        return switch (align) {
            case LEFT -> ":" + "-".repeat(effectiveWidth - 1);
            case RIGHT -> "-".repeat(effectiveWidth - 1) + ":";
            case CENTER -> ":" + "-".repeat(effectiveWidth - 2) + ":";
        };
    }

    private Alignment getAlignmentForColumn(int index) {
        if (index < alignments.size()) {
            return alignments.get(index);
        }
        return Alignment.LEFT;
    }
}