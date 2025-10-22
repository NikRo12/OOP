package ru.nsu.romanenko.elements.tests;

import elements.CodeBlock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CodeBlockTest {

    @Nested
    class ConstructorTests {

        @Test
        void createCodeBlockWithCodeAndLanguage() {
            String code = "public class Test {\n    public void method() {}}";
            String language = "java";

            CodeBlock codeBlock = new CodeBlock(code, language);

            assertEquals("java", codeBlock.getLanguage());
            assertEquals(2, codeBlock.getLineCount());
            assertEquals(Arrays.asList("public class Test {", "    public void method() {}}"), codeBlock.getLines());
        }

        @Test
        void createCodeBlockWithLinesAndLanguage() {
            ArrayList<String> lines = new ArrayList<>(Arrays.asList("line1", "line2", "line3"));
            String language = "python";

            CodeBlock codeBlock = new CodeBlock(lines, language);

            assertEquals("python", codeBlock.getLanguage());
            assertEquals(3, codeBlock.getLineCount());
            assertEquals(lines, codeBlock.getLines());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void handleNullAndEmptyCode(String code) {
            CodeBlock codeBlock = new CodeBlock(code, "java");

            assertEquals(0, codeBlock.getLineCount());
            assertTrue(codeBlock.getLines().isEmpty());
        }
    }

    @Nested
    class BuilderTests {

        @Test
        void buildCodeBlockWithBuilder() {
            CodeBlock codeBlock = new CodeBlock.Builder()
                    .setLanguage("java")
                    .addLine("public class Test {")
                    .addLine("}")
                    .build();

            assertEquals("java", codeBlock.getLanguage());
            assertEquals(2, codeBlock.getLineCount());
        }

        @Test
        void buildCodeBlockWithAddLines() {
            CodeBlock codeBlock = new CodeBlock.Builder()
                    .setLanguage("python")
                    .addLines("def test():", "    pass", "return True")
                    .build();

            assertEquals("python", codeBlock.getLanguage());
            assertEquals(3, codeBlock.getLineCount());
        }

        @Test
        void buildCodeBlockWithAddCode() {
            String code = "function test() {\n    console.log('hello');\n}";

            CodeBlock codeBlock = new CodeBlock.Builder()
                    .setLanguage("javascript")
                    .addCode(code)
                    .build();

            assertEquals("javascript", codeBlock.getLanguage());
            assertEquals(3, codeBlock.getLineCount());
        }

        @Test
        void handleNullInBuilderMethods() {
            CodeBlock codeBlock = new CodeBlock.Builder()
                    .setLanguage(null)
                    .addLine(null)
                    .addLines(null, "valid")
                    .addCode(null)
                    .build();

            assertEquals("", codeBlock.getLanguage());
            assertEquals(1, codeBlock.getLineCount());
            assertEquals("valid", codeBlock.getLines().get(0));
        }
    }

    @Nested
    class RenderTests {

        @Test
        void renderCodeBlockWithLanguage() {
            CodeBlock codeBlock = new CodeBlock.Builder()
                    .setLanguage("java")
                    .addLine("public class Test {}")
                    .build();

            String expected = "```java\npublic class Test {}\n```";
            assertEquals(expected, codeBlock.render());
        }

        @Test
        void renderCodeBlockWithoutLanguage() {
            CodeBlock codeBlock = new CodeBlock.Builder()
                    .addLine("plain text")
                    .build();

            String expected = "```\nplain text\n```";
            assertEquals(expected, codeBlock.render());
        }

        @Test
        void renderMultiLineCodeBlock() {
            CodeBlock codeBlock = new CodeBlock.Builder()
                    .setLanguage("python")
                    .addLine("def hello():")
                    .addLine("    print('world')")
                    .build();

            String expected = "```python\ndef hello():\n    print('world')\n```";
            assertEquals(expected, codeBlock.render());
        }

        @Test
        void renderEmptyCodeBlock() {
            CodeBlock codeBlock = new CodeBlock("", "");

            String expected = "```\n```";
            assertEquals(expected, codeBlock.render());
        }
    }

    @Nested
    class GetterTests {

        @Test
        void getLinesReturnsCopy() {
            ArrayList<String> originalLines = new ArrayList<>(Arrays.asList("line1", "line2"));
            CodeBlock codeBlock = new CodeBlock(originalLines, "java");

            List<String> returnedLines = codeBlock.getLines();
            returnedLines.add("line3");

            assertEquals(2, codeBlock.getLineCount());
            assertEquals(Arrays.asList("line1", "line2"), codeBlock.getLines());
        }

        @Test
        void getLineCountForEmptyBlock() {
            CodeBlock codeBlock = new CodeBlock("", "java");

            assertEquals(0, codeBlock.getLineCount());
        }

        @Test
        void getLineCountForMultiLineBlock() {
            String code = "line1\nline2\nline3";
            CodeBlock codeBlock = new CodeBlock(code, "text");

            assertEquals(3, codeBlock.getLineCount());
        }
    }

    @Nested
    class EqualsAndHashCodeTests {

        @Test
        void equalsWithSameObject() {
            CodeBlock codeBlock = new CodeBlock("test", "java");

            assertEquals(codeBlock, codeBlock);
        }

        @Test
        void equalsWithDifferentClass() {
            CodeBlock codeBlock = new CodeBlock("test", "java");

            assertNotEquals("not a codeblock", codeBlock);
        }

        @Test
        void equalsWithNull() {
            CodeBlock codeBlock = new CodeBlock("test", "java");

            assertNotEquals(null, codeBlock);
        }

        @Test
        void equalsWithSameContent() {
            CodeBlock codeBlock1 = new CodeBlock("test", "java");
            CodeBlock codeBlock2 = new CodeBlock("test", "java");

            assertEquals(codeBlock1, codeBlock2);
            assertEquals(codeBlock1.hashCode(), codeBlock2.hashCode());
        }

        @Test
        void equalsWithDifferentLanguage() {
            CodeBlock codeBlock1 = new CodeBlock("test", "java");
            CodeBlock codeBlock2 = new CodeBlock("test", "python");

            assertNotEquals(codeBlock1, codeBlock2);
        }

        @Test
        void equalsWithDifferentContent() {
            CodeBlock codeBlock1 = new CodeBlock("test1", "java");
            CodeBlock codeBlock2 = new CodeBlock("test2", "java");

            assertNotEquals(codeBlock1, codeBlock2);
        }
    }

    @Nested
    class EdgeCaseTests {

        @Test
        void codeWithTrailingNewline() {
            String code = "line1\nline2\n";
            CodeBlock codeBlock = new CodeBlock(code, "text");

            assertEquals(3, codeBlock.getLineCount());
            assertEquals(Arrays.asList("line1", "line2", ""), codeBlock.getLines());
        }

        @Test
        void codeWithOnlyNewlines() {
            String code = "\n\n";
            CodeBlock codeBlock = new CodeBlock(code, "text");

            assertEquals(3, codeBlock.getLineCount());
            assertEquals(Arrays.asList("", "", ""), codeBlock.getLines());
        }

        @Test
        void veryLongLine() {
            String longLine = "a".repeat(1000);
            CodeBlock codeBlock = new CodeBlock(longLine, "text");

            assertEquals(1, codeBlock.getLineCount());
            assertEquals(longLine, codeBlock.getLines().get(0));
        }
    }
}