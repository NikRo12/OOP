package ru.nsu.romanenko.elements.tests;

import elements.MarkdownElement;
import elements.Table;
import elements.text.Styles;
import elements.text.Text;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Table Tests")
public class TableTest {

    @Nested
    @DisplayName("Table Constructor Tests")
    class ConstructorTest {
        @Test
        @DisplayName("Should create table with alignments and rows")
        void shouldCreateTableWithAlignmentsAndRows() {
            List<Table.Alignment> alignments = List.of(Table.ALIGN_LEFT, Table.ALIGN_RIGHT);
            List<List<MarkdownElement>> rows = new ArrayList<>();
            rows.add(List.of(new Text("Header 1"), new Text("Header 2")));

            Table table = new Table(alignments, 10, rows);

            assertEquals(2, table.getAlignments().size());
            assertEquals(10, table.getRowLimit());
            assertEquals(1, table.getRows().size());
        }

        @Test
        @DisplayName("Should create defensive copies of collections")
        void shouldCreateDefensiveCopiesOfCollections() {
            List<Table.Alignment> originalAlignments = new ArrayList<>();
            originalAlignments.add(Table.ALIGN_LEFT);
            List<List<MarkdownElement>> originalRows = new ArrayList<>();
            originalRows.add(new ArrayList<>());

            Table table = new Table(originalAlignments, 5, originalRows);

            originalAlignments.clear();
            originalRows.clear();

            assertEquals(1, table.getAlignments().size());
            assertEquals(1, table.getRows().size());
        }
    }

    @Nested
    @DisplayName("Table Builder Tests")
    class BuilderTest {
        @Test
        @DisplayName("Should build table with alignments")
        void shouldBuildTableWithAlignments() {
            Table table = new Table.Builder()
                    .withAlignments(Table.ALIGN_LEFT, Table.ALIGN_CENTER, Table.ALIGN_RIGHT)
                    .addRow("Name", "Age", "Score")
                    .build();

            assertEquals(3, table.getAlignments().size());
            assertEquals(1, table.getRows().size());
        }

        @Test
        @DisplayName("Should build table with row limit")
        void shouldBuildTableWithRowLimit() {
            Table table = new Table.Builder()
                    .withRowLimit(2)
                    .addRow("A", "B")
                    .addRow("C", "D")
                    .addRow("E", "F")
                    .build();

            assertEquals(2, table.getRows().size());
            assertEquals(2, table.getRowLimit());
        }

        @Test
        @DisplayName("Should convert objects to Text elements")
        void shouldConvertObjectsToTextElements() {
            Table table = new Table.Builder()
                    .addRow("String", 123, 45.67, true)
                    .build();

            List<MarkdownElement> firstRow = table.getRows().get(0);
            assertEquals("String", firstRow.get(0).render());
            assertEquals("123", firstRow.get(1).render());
            assertEquals("45.67", firstRow.get(2).render());
            assertEquals("true", firstRow.get(3).render());
        }

        @Test
        @DisplayName("Should handle MarkdownElement cells directly")
        void shouldHandleMarkdownElementCellsDirectly() {
            Text boldText = new Text("Important");
            boldText.applyStyleProtect(Styles.BOLD);

            Table table = new Table.Builder()
                    .addRow(boldText, new Text("Normal"))
                    .build();

            List<MarkdownElement> firstRow = table.getRows().get(0);
            assertEquals("**Important**", firstRow.get(0).render());
            assertEquals("Normal", firstRow.get(1).render());
        }

        @Test
        @DisplayName("Should build empty table")
        void shouldBuildEmptyTable() {
            Table table = new Table.Builder().build();
            assertEquals(0, table.getRows().size());
            assertEquals(-1, table.getRowLimit());
        }
    }

    @Nested
    @DisplayName("Table Render Tests")
    class RenderTest {
        @Test
        @DisplayName("Should render table with formatted content")
        void shouldRenderTableWithFormattedContent() {
            Text boldText = new Text("Important");
            boldText.applyStyleProtect(Styles.BOLD);

            Table table = new Table.Builder()
                    .addRow(boldText, "Normal")
                    .build();

            String result = table.render();
            assertTrue(result.contains("| **Important** | Normal |"));
        }

        @Test
        @DisplayName("Should render empty table as empty string")
        void shouldRenderEmptyTableAsEmptyString() {
            Table table = new Table.Builder().build();
            String result = table.render();
            assertEquals("", result);
        }

        @Test
        @DisplayName("Should apply row limit when rendering")
        void shouldApplyRowLimitWhenRendering() {
            Table table = new Table.Builder()
                    .withRowLimit(2)
                    .addRow("A")
                    .addRow("B")
                    .addRow("C")
                    .addRow("D")
                    .build();

            String result = table.render();
            assertTrue(result.contains("A"));
            assertTrue(result.contains("B"));
            assertFalse(result.contains("C"));
            assertFalse(result.contains("D"));
        }
    }

    @Nested
    @DisplayName("Table Alignment Tests")
    class AlignmentTest {
        @Test
        @DisplayName("Should have correct alignment constants")
        void shouldHaveCorrectAlignmentConstants() {
            assertEquals(Table.Alignment.LEFT, Table.ALIGN_LEFT);
            assertEquals(Table.Alignment.CENTER, Table.ALIGN_CENTER);
            assertEquals(Table.Alignment.RIGHT, Table.ALIGN_RIGHT);
        }

        @Test
        @DisplayName("Should have all alignment values")
        void shouldHaveAllAlignmentValues() {
            Table.Alignment[] values = Table.Alignment.values();
            assertEquals(3, values.length);
            assertArrayEquals(new Table.Alignment[]{
                    Table.Alignment.LEFT, Table.Alignment.CENTER, Table.Alignment.RIGHT
            }, values);
        }

    @Nested
    @DisplayName("Table Equality Tests")
    class EqualityTest {
        @Test
        @DisplayName("Should be equal when same structure")
        void shouldBeEqualWhenSameStructure() {
            Table table1 = new Table.Builder()
                    .withAlignments(Table.ALIGN_LEFT)
                    .withRowLimit(5)
                    .addRow("Test")
                    .build();

            Table table2 = new Table.Builder()
                    .withAlignments(Table.ALIGN_LEFT)
                    .withRowLimit(5)
                    .addRow("Test")
                    .build();

            assertEquals(table1, table2);
            assertEquals(table1.hashCode(), table2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when different alignments")
        void shouldNotBeEqualWhenDifferentAlignments() {
            Table table1 = new Table.Builder()
                    .withAlignments(Table.ALIGN_LEFT)
                    .addRow("Test")
                    .build();

            Table table2 = new Table.Builder()
                    .withAlignments(Table.ALIGN_RIGHT)
                    .addRow("Test")
                    .build();

            assertNotEquals(table1, table2);
        }

        @Test
        @DisplayName("Should not be equal when different row limit")
        void shouldNotBeEqualWhenDifferentRowLimit() {
            Table table1 = new Table.Builder()
                    .withRowLimit(5)
                    .addRow("Test")
                    .build();

            Table table2 = new Table.Builder()
                    .withRowLimit(10)
                    .addRow("Test")
                    .build();

            assertNotEquals(table1, table2);
        }

        @Test
        @DisplayName("Should not be equal when different rows")
        void shouldNotBeEqualWhenDifferentRows() {
            Table table1 = new Table.Builder()
                    .addRow("A")
                    .build();

            Table table2 = new Table.Builder()
                    .addRow("B")
                    .build();

            assertNotEquals(table1, table2);
        }

        @Test
        @DisplayName("Should not be equal to null or different class")
        void shouldNotBeEqualToNullOrDifferentClass() {
            Table table = new Table.Builder().build();
            assertNotEquals(null, table);
            assertNotEquals("string", table);
        }
    }

    @Nested
    @DisplayName("Table Edge Cases Tests")
    class EdgeCasesTest {
        @Test
        @DisplayName("Should handle very long content")
        void shouldHandleVeryLongContent() {
            String longText = "This is a very long text that might exceed normal cell width";
            Table table = new Table.Builder()
                    .addRow(longText)
                    .build();

            String result = table.render();
            assertTrue(result.contains(longText));
            }
        }
    }
}