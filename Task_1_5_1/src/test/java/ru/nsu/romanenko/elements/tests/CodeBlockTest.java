package ru.nsu.romanenko.elements.tests;

import elements.CodeBlock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodeBlockTest {

    @Test
    @DisplayName("Проверка базового рендеринга с языком")
    void testBasicRender() {
        CodeBlock cb = new CodeBlock("System.out.println(\"Hello\");", "java");

        String expected =
                "```java\n" +
                        "System.out.println(\"Hello\");\n" +
                        "```";

        assertEquals(expected, cb.render());
    }

    @Test
    @DisplayName("Проверка Builder и многострочного кода")
    void testBuilderMultiLine() {
        CodeBlock cb = new CodeBlock.Builder()
                .setLanguage("python")
                .addLine("def main():")
                .addLine("    print('test')")
                .build();

        String expected =
                "```python\n" +
                        "def main():\n" +
                        "    print('test')\n" +
                        "```";

        assertEquals(expected, cb.render());
    }

    @Test
    @DisplayName("Проверка корректного разбиения строк (split)")
    void testLineParsing() {
        CodeBlock cb = new CodeBlock("line1\n\nline3", "txt");

        assertEquals(3, cb.getLineCount());

        String expected =
                "```txt\n" +
                        "line1\n" +
                        "\n" +
                        "line3\n" +
                        "```";
        assertEquals(expected, cb.render());
    }

    @Test
    @DisplayName("Проверка рендеринга без указания языка")
    void testNoLanguage() {
        CodeBlock cb = new CodeBlock("plain text", null);
        String expected = "```\nplain text\n```";
        assertEquals(expected, cb.render());

        CodeBlock cbEmpty = new CodeBlock.Builder().addLine("test").build();
        assertEquals("```\ntest\n```", cbEmpty.render());
    }

    @Test
    @DisplayName("Проверка равенства CodeBlock")
    void testCodeBlockEquality() {
        CodeBlock cb1 = new CodeBlock("code", "java");
        CodeBlock cb2 = new CodeBlock("code", "java");
        CodeBlock cb3 = new CodeBlock("code", "cpp");
        CodeBlock cb4 = new CodeBlock("diff", "java");

        assertEquals(cb1, cb2);
        assertNotEquals(cb1, cb3);
        assertNotEquals(cb1, cb4);
        assertEquals(cb1.hashCode(), cb2.hashCode());
    }

    @Test
    @DisplayName("Проверка пустых строк и null контента")
    void testEmptyAndNullContent() {
        CodeBlock cbEmpty = new CodeBlock("", "java");
        assertEquals("```java\n```", cbEmpty.render());

        CodeBlock.Builder builder = new CodeBlock.Builder().setLanguage("java");
        assertEquals("```java\n```", builder.build().render());
    }

    @Test
    @DisplayName("Проверка получения языка и количества строк")
    void testGetters() {
        CodeBlock cb = new CodeBlock("1\n2\n3", "java");
        assertEquals("java", cb.getLanguage());
        assertEquals(3, cb.getLineCount());
    }

    @Test
    @DisplayName("Проверка Builder: добавление нескольких строк разом")
    void testBuilderAddLines() {
        CodeBlock cb = new CodeBlock.Builder()
                .addLine("line1")
                .addLine("line2")
                .build();

        assertTrue(cb.render().contains("line1\nline2"));
    }
}