package elements;

import elements.text.Text;

import java.util.ArrayList;
import java.util.List;

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
                } else{
                    row.add(new Text(cell.toString()));
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

    public List<Alignment> getAlignments() {
        return alignments;
    }

    public int getRowLimit() {
        return rowLimit;
    }

    public List<List<MarkdownElement>> getRows() {
        return rows;
    }

    private void renderRow(StringBuilder sb, List<MarkdownElement> row) {
        sb.append("|");
        for (int i = 0; i < getColumnCount(); i++) {
            String content = i < row.size() ? row.get(i).render() : "";
            sb.append(" ").append(content).append(" |");
        }
        sb.append("\n");
    }

    private int getColumnCount() {
        int maxColumns = 0;
        for (List<MarkdownElement> row : rows) {
            if (row.size() > maxColumns) {
                maxColumns = row.size();
            }
        }
        return maxColumns;
    }

    public enum Alignment {
        LEFT,
        CENTER,
        RIGHT
    }

    public static final Alignment ALIGN_LEFT = Alignment.LEFT;
    public static final Alignment ALIGN_CENTER = Alignment.CENTER;
    public static final Alignment ALIGN_RIGHT = Alignment.RIGHT;


    @Override
    public String render() {
        if (rows.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        renderRow(sb, rows.get(0));

        sb.append("|");
        for (int i = 0; i < getColumnCount(); i++) {
            Alignment align = i < alignments.size() ? alignments.get(i) : Alignment.LEFT;
            switch (align) {
                case LEFT -> sb.append(" :------ |");
                case RIGHT -> sb.append(" ------: |");
                case CENTER -> sb.append(" :-----: |");
                default -> sb.append(" ------ |");
            }
        }
        sb.append("\n");

        for (int i = 1; i < rows.size(); i++) {
            renderRow(sb, rows.get(i));
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
}